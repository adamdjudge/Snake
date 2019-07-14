import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Keeps track of the state of the game board.
 */
class Board {

    /**
     * Possible states for each position on the board.
     */
    public enum PositionState {
        FREE, SNAKE, FOOD, WALL
    }

    /**
     * Possible directions for the snake to go in.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /* Internal variables */
    private int size;
    private int length;
    private int startLength;
    private int curX;
    private int curY;
    private Direction direction;
    private boolean directionChangedThisStep;
    private ArrayList<Position> positions = new ArrayList<>();

    /**
     * Initialize a new game state as a square of position states.
     * @param size Length of a side of the square grid.
     * @param length Initial length of the snake.
     * @param border Whether to enable the surrounding border.
     */
    Board(int size, int length, boolean border) {
        this.size = size;
        this.direction = Direction.RIGHT;
        this.length = this.startLength = length;
        this.curX = this.curY = size / 2;

        for (int i = 0; i < size*size; i++)
            positions.add(new Position());

        if (border) {
            for (int i = 0; i < size; i++) {
                positions.get(getCoordinateIndex(i, 0)).setWall();
                positions.get(getCoordinateIndex(i, size-1)).setWall();
                positions.get(getCoordinateIndex(0, i)).setWall();
                positions.get(getCoordinateIndex(size-1, i)).setWall();
            }
        }

        positions.get(getCoordinateIndex(curX, curY)).setHead(length);
        putRandomFood();
    }

    /**
     * Step the state of the game. Advances the snake and returns whether the game is still active.
     * @return False if the game ends from the snake hitting itself or a wall, true otherwise.
     */
    boolean step() {
        int nextX = curX;
        int nextY = curY;

        /* Go to next position coordinate based on direction */
        switch (this.direction) {
            case UP:
                nextY--;
                break;
            case DOWN:
                nextY++;
                break;
            case LEFT:
                nextX--;
                break;
            case RIGHT:
                nextX++;
        }

        /* Wrap-around */
        if (nextX == size) nextX = 0;
        if (nextX < 0) nextX = size-1;
        if (nextY == size) nextY = 0;
        if (nextY < 0) nextY = size-1;

        /* Handle special position cases */
        switch (getPositionState(getCoordinateIndex(nextX, nextY))) {
            case SNAKE:
            case WALL:
                return false;
            case FOOD:
                this.length++;
                putRandomFood();
        }

        /* Update all positions, causes snake tail to pass through positions so they eventually become free */
        for (Position p : positions)
            p.update();

        /* Set next position of the head of the snake */
        int index = getCoordinateIndex(nextX, nextY);
        positions.get(index).setHead(this.length);

        /* Update variables and return */
        curX = nextX;
        curY = nextY;
        directionChangedThisStep = false;
        return true;
    }

    /**
     * Places food at a random location on the board, avoiding the snake and walls.
     */
    private void putRandomFood() {
        int location;
        PositionState posState;
        do {
            location = ThreadLocalRandom.current().nextInt(0, size*size);
            posState = getPositionState(location);
        } while (posState == PositionState.SNAKE || posState == PositionState.WALL);
        positions.get(location).setFood();
    }

    /**
     * Sets the direction for the snake to go in.
     * @param newDirection Direction for the snake;
     */
    void setDirection(Direction newDirection) {
        if (directionChangedThisStep) return;
        if (direction == Direction.UP && newDirection == Direction.DOWN) return;
        if (direction == Direction.DOWN && newDirection == Direction.UP) return;
        if (direction == Direction.RIGHT && newDirection == Direction.LEFT) return;
        if (direction == Direction.LEFT && newDirection == Direction.RIGHT) return;
        direction = newDirection;
        directionChangedThisStep = true;
    }

    /**
     * Calculates the position index given coordinates.
     * @param x X coordinate
     * @param y Y coordinate
     * @return Index within the ArrayList of positions.
     */
    private int getCoordinateIndex(int x, int y) {
        return y * size + x;
    }

    /**
     * Gets the state of the position at the given index.
     * @param index Index of position within the ArrayList of positions.
     * @return Current state of the position on the board.
     */
    PositionState getPositionState(int index) {
        return positions.get(index).getState();
    }

    /**
     * Returns the current score for the game, which is the current length minus the starting length.
     * @return Current score.
     */
    int getScore() {
        return length - startLength;
    }

    /**
     * Contains the state of a single position on the board.
     */
    private class Position {
        private PositionState state;
        private int fadeout;
        Position() {
            state = PositionState.FREE;
            fadeout = 0;
        }
        PositionState getState() {
            return state;
        }
        void setFood() {
            state = PositionState.FOOD;
        }
        void setHead(int length) {
            state = PositionState.SNAKE;
            fadeout = length - 1;
        }
        void setWall() {
            state = PositionState.WALL;
        }
        void update() {
            if (state == PositionState.FOOD)
                return;
            if (state == PositionState.WALL)
                return;
            if (fadeout == 0)
                state = PositionState.FREE;
            else
                fadeout--;
        }
    }
}

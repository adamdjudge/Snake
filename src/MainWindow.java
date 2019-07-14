/*
 * Simple Java Snake Game
 * Copyleft 2019 Adam Judge
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * The main window of the game.
 */
class MainWindow extends JFrame implements KeyListener {

    // Color theme settings
    // Index key:
    //    0 - Default
    //    1 - Black on white
    //    2 - White on black
    //    3 - Retro green
    //    4 - Retro amber
    //    5 - Pastel
    private static final Color[] BACKGROUND_COLORS = {
            new Color(255, 255, 255),
            new Color(255, 255, 255),
            new Color(0, 0, 0),
            new Color(0, 0, 0),
            new Color(0, 0, 0),
            new Color(255, 250, 250)
    };
    private static final Color[] SNAKE_COLORS = {
            new Color(0, 255, 0),
            new Color(0, 0, 0),
            new Color(255, 255, 255),
            new Color(36, 204, 68),
            new Color(255, 191, 0),
            new Color(203, 241, 245)
    };
    private static final Color[] FOOD_COLORS = {
            new Color(255, 0, 0),
            new Color(0, 0, 0),
            new Color(255, 255, 255),
            new Color(36, 204, 68),
            new Color(255, 191, 0),
            new Color(255, 206, 206)
    };
    private static final Color[] WALL_COLORS = {
            new Color(64, 64, 64),
            new Color(0, 0, 0),
            new Color(255, 255, 255),
            new Color(36, 204, 68),
            new Color(255, 191, 0),
            new Color(202, 171, 216)
    };

    /* Internal variables */
    private Color SNAKE_COLOR;
    private Color FOOD_COLOR;
    private Color BACKGROUND_COLOR;
    private Color WALL_COLOR;
    private int size;
    private ArrayList<JPanel> pixels = new ArrayList<>();
    private Board board;

    /**
     * Constructs the main window with a square grid of pixels of a given size.
     * @param size How many pixels on a side of the square.
     * @param screenSize How many (real) pixels on a side of the window.
     * @param board Board containing state.
     * @param colorTheme Selects color theme to use.
     */
    MainWindow(int size, int screenSize, Board board, int colorTheme) {
        super("Snake - Food: 0");
        setLayout(new GridLayout(size, size));
        this.size = size;
        this.board = board;
        this.BACKGROUND_COLOR = BACKGROUND_COLORS[colorTheme];
        this.SNAKE_COLOR = SNAKE_COLORS[colorTheme];
        this.FOOD_COLOR = FOOD_COLORS[colorTheme];
        this.WALL_COLOR = WALL_COLORS[colorTheme];

        /* Fill in the frame with panels, which serve as the pixels */
        JPanel cur;
        for (int i = 0; i < size*size; i++) {
            cur = new JPanel();
            pixels.add(cur);
            add(cur);
        }

        /* Finish setting up and show window */
        pack();
        setSize(screenSize, screenSize);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setVisible(true);
    }

    /**
     * Renders the screen based on the current game state.
     */
    void render() {
        for (int i = 0; i < size*size; i++) {
            switch (board.getPositionState(i)) {
                case FREE:
                    pixels.get(i).setBackground(BACKGROUND_COLOR);
                    break;
                case SNAKE:
                    pixels.get(i).setBackground(SNAKE_COLOR);
                    break;
                case FOOD:
                    pixels.get(i).setBackground(FOOD_COLOR);
                    break;
                case WALL:
                    pixels.get(i).setBackground(WALL_COLOR);
            }
        }
        setTitle("Snake - Food: " + board.getScore());
    }

    /**
     * Keypress handler for setting direction based on arrow keys.
     * @param e KeyEvent to be processed
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                board.setDirection(Board.Direction.UP);
                break;
            case KeyEvent.VK_DOWN:
                board.setDirection(Board.Direction.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                board.setDirection(Board.Direction.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                board.setDirection(Board.Direction.RIGHT);
                break;
        }
    }

    /* Other key handlers not needed */
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}

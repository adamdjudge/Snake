/*
 * Simple Java Snake Game
 * Copyleft 2019 Adam Judge
 */

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Main class that launches the game and controls timing.
 */
public class Game extends JFrame {

    /* Configuration constants */
    private static final String TITLE = "Snake v1.0";
    private static final String[] difficulties = {"Easy", "Medium", "Hard"};
    private static final String[] themes = {
            "Default",
            "Black on white",
            "White on black",
            "Retro green",
            "Retro amber",
            "Pastel"
    };

    /**
     * Container class to hold the difficulty and color theme configuration selected on startup.
     */
    private static class Configuration {
        int difficulty, theme;
        Configuration(int difficulty, int theme) {
            this.difficulty = difficulty;
            this.theme = theme;
        }
    }

    /**
     * Prompts the user for game configuration on startup.
     * @return Configuration info object.
     */
    private static Configuration getConfiguration() {
        JPanel mainPanel = new JPanel();
        JComboBox selectDifficulty = new JComboBox(difficulties);
        JComboBox selectTheme = new JComboBox(themes);

        mainPanel.setLayout(new GridLayout(2, 2));
        mainPanel.add(new JLabel("Difficulty: "));
        mainPanel.add(selectDifficulty);
        mainPanel.add(new JLabel("Color Theme: "));
        mainPanel.add(selectTheme);

        int response = JOptionPane.showConfirmDialog(null, mainPanel, TITLE, JOptionPane.OK_CANCEL_OPTION);
        if (response != JOptionPane.OK_OPTION)
            System.exit(0);
        return new Configuration(selectDifficulty.getSelectedIndex(), selectTheme.getSelectedIndex());
    }

    /* Game constants */
    private static final int SIZE = 40;
    private static final int SCREEN_SIZE = 600;

    /* Difficulty settings */
    private static final int[] INITIAL_LENGTH = {3, 10, 20};
    private static final int[] START_DELAY_MS = {200, 175, 145};
    private static final int[] SPEED_INCREASE = {2, 2, 3};

    /**
     * Starts a new game and runs it until it's done. Displays a game over message after the game ends.
     * @param difficulty Difficulty mode, from 0 (easy) to 2 (hard).
     * @param theme Color theme selection.
     */
    private static void runGame(int difficulty, int theme) {

        /* Create new board and game window */
        Board board = new Board(SIZE, INITIAL_LENGTH[difficulty], (difficulty > 0));
        MainWindow window = new MainWindow(SIZE, SCREEN_SIZE, board, theme);
        int delay = START_DELAY_MS[difficulty];

        /* Render and step the game while it's still going */
        do {
            window.render();
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            }
            catch (InterruptedException e) {}
            delay = START_DELAY_MS[difficulty] - board.getScore() * SPEED_INCREASE[difficulty];
        } while (board.step());

        /* Game over message */
        String gameOver = "Food eaten: " + board.getScore() +
                "\nDifficulty: " + difficulties[difficulty] +
                "\nFinal score: " + (board.getScore() * (difficulty + 1));
        JOptionPane.showMessageDialog(null, gameOver, "Game over!", JOptionPane.ERROR_MESSAGE);
        window.dispose();
    }

    /**
     * Starts the application.
     */
    public static void main(String[] args) {
        Configuration config = getConfiguration();
        runGame(config.difficulty, config.theme);
    }
}

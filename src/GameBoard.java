package org.cis120.chess;

/**
 * CIS 120 HW09
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class instantiates a Chess object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games.
 * 
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Chess chess; // model for the game
    private JLabel status; // current status text
    private Square sq1;
    private Square sq2;

    // Game constants
    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        chess = new Chess(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                if (sq1 == null) {
                    sq1 = getSquareFromClick(p);
                    System.out.println("p1: " + p.toString());

                } else if (sq2 == null) {
                    sq2 = getSquareFromClick(p);
                    System.out.println("p2: " + p.toString());
                    Object result = null;
                    if (chess.checkPromotion(sq1, sq2)) {
                        String[] possibleValues = { "Queen", "Rook", "Bishop", "Knight" };
                        result = JOptionPane.showInputDialog(
                                null, "Choose one", "Input",
                                JOptionPane.INFORMATION_MESSAGE, null,
                                possibleValues, possibleValues[0]
                        ).toString();

                    }
                    System.out.println(chess.playTurn(sq1, sq2, result));
                    if (!chess.isGameOver()) {
                        if (chess.isKingAttacked()) {
                            if (chess.isWhiteToMove()) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "White, your king is in check! " +
                                                "Protect it or move it to safety."
                                );
                            } else {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "Black, your king is in check! " +
                                                "Protect it or move it to safety."
                                );
                            }
                        }
                    } else {
                        status.setText("Game over! Press reset board to start a new one.");

                    }
                    sq1 = null;
                    sq2 = null;
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    private Square getSquareFromClick(Point p) {
        double scaledX = p.getX() / 75;
        double scaledY = p.getY() / 75;
        return chess.getBoard()[(int) scaledX][7 - (int) scaledY];
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        chess.reset();
        status.setText("White to move.");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undoMove() {
        chess.undo();
        repaint();
        updateStatus();
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (chess.isGameOver()) {
            status.setText("Game over! Press reset board to start a new one.");
            return;
        }
        if (chess.isWhiteToMove()) {
            if (sq1 == null) {
                status.setText("White to move. Click the starting square.");
            } else {
                status.setText("White to move. Click the ending square.");
            }

        } else {
            if (sq1 == null) {
                status.setText("Black to move. Click the starting square.");
            } else {
                status.setText("Black to move. Click the ending square.");
            }
        }
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Square[][] board = chess.getBoard();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square square = board[x][y];
                square.paintComponent(g);
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}

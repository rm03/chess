package org.cis120.chess;

import javax.swing.*;
import java.awt.*;

public class Square extends JComponent {

    private Piece piece;
    private int rank;
    private int file;
    private int boardX;
    private int boardY;
    private boolean isLight;

    public Square(Piece piece, int rank, int file, int boardX, int boardY, boolean isLight) {
        this.piece = piece;
        this.rank = rank;
        this.file = file;
        this.boardX = boardX;
        this.boardY = boardY;
        this.isLight = isLight;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece p) {
        this.piece = p;
    }

    public boolean containsPiece() {
        return this.piece != null;
    }

    public int getRank() {
        return this.rank;
    }

    public int getFile() {
        return this.file;
    }

    public int getBoardX() {
        return this.boardX;
    }

    public int getBoardY() {
        return this.boardY;
    }

    public boolean getIsLight() {
        return this.isLight;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.isLight) {
            g.setColor(new Color(236, 218, 185));
        } else {
            g.setColor(new Color(178, 138, 104));
        }
        g.fillRect(this.boardX, this.boardY, 75, 75);
        if (this.containsPiece()) {
            this.getPiece().paintComponent(g, this);
        }
    }

    @Override
    public String toString() {
        if (!containsPiece()) {
            return "file: " + file + "\nrank: " + rank + "\ncontains piece: false";
        } else {
            return "file: " + file + "\nrank: " + rank + "\npiece: " + piece.toString();
        }
    }

}

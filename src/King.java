package org.cis120.chess;

import java.util.HashSet;

public class King extends Piece {

    boolean hasCastled;
    boolean hasMoved;
    Square currSquare;

    public King(boolean isWhite, String imgPath) {
        super(isWhite, imgPath);
        this.hasCastled = false;
        this.hasMoved = false;
        this.currSquare = null;
    }

    public Square getCurrSquare() {
        return this.currSquare;
    }

    public void setCurrSquare(Square sq) {
        this.currSquare = sq;
    }

    public void setCastled(boolean b) {
        this.hasCastled = b;
    }

    public void setHasMoved(boolean b) {
        this.hasMoved = b;
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }

    @Override
    public boolean canLegallyMove(Square start, Square end, Square[][] board) {

        int rankMovement = Math.abs(end.getRank() - start.getRank());
        int fileMovement = Math.abs(end.getFile() - start.getFile());

        if (rankMovement > 1 || fileMovement > 1 || rankMovement + fileMovement == 0) {
            return false;
        }

        if (end.containsPiece()) {
            if (this.isPieceWhite() == end.getPiece().isPieceWhite()
                    || end.getPiece() instanceof King) {
                return false;
            }
        }

        return true;
    }

    @Override
    public HashSet<Square> getAttackingSquares(Square curr, Square[][] board) {
        HashSet<Square> attacked = new HashSet<Square>();
        for (int i = curr.getFile() - 1; i <= curr.getFile() + 1; i++) {
            for (int j = curr.getRank() - 1; j <= curr.getRank() + 1; j++) {
                if (i >= 0 && j >= 0 && i <= 7 && j <= 7) {
                    if (!board[i][j].containsPiece()) {
                        attacked.add(board[i][j]);
                    }
                }
            }
        }
        return attacked;
    }

}

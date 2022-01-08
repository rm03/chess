package org.cis120.chess;

import java.util.HashSet;

public class Rook extends Piece {

    private boolean hasMoved;

    public Rook(boolean isWhite, String imgPath) {
        super(isWhite, imgPath);
        this.hasMoved = false;
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public void setMoved(boolean b) {
        this.hasMoved = b;
    }

    @Override
    public boolean canLegallyMove(Square start, Square end, Square[][] board) {

        int rankMovement = Math.abs(end.getRank() - start.getRank());
        int fileMovement = Math.abs(end.getFile() - start.getFile());

        if ((rankMovement > 0 && fileMovement != 0) ||
                (fileMovement > 0 && rankMovement != 0) ||
                (fileMovement == 0 && rankMovement == 0)) {
            return false;
        }

        if (end.containsPiece()) {
            if (this.isPieceWhite() == end.getPiece().isPieceWhite()
                    || end.getPiece() instanceof King) {
                return false;
            }
        }

        int x = start.getFile();
        int y = start.getRank();

        if (rankMovement > 0) {
            if (checkRank(end, board, x, y)) {
                return false;
            }

        } else {
            if (checkFile(end, board, x, y)) {
                return false;
            }

        }

        return true;
    }

    @Override
    public HashSet<Square> getAttackingSquares(Square curr, Square[][] board) {
        HashSet<Square> attacked = new HashSet<Square>();
        attacked.addAll(checkAttackingFile(curr, board));
        attacked.addAll(checkAttackingRank(curr, board));
        return attacked;
    }
}

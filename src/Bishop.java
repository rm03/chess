package org.cis120.chess;

import java.util.HashSet;

public class Bishop extends Piece {

    public Bishop(boolean isWhite, String imgPath) {
        super(isWhite, imgPath);
    }

    @Override
    public boolean canLegallyMove(Square start, Square end, Square[][] board) {

        int rankMovement = Math.abs(end.getRank() - start.getRank());
        int fileMovement = Math.abs(end.getFile() - start.getFile());

        if (rankMovement != fileMovement || fileMovement <= 0) {
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
        return !checkDiagonal(end, board, x, y);
    }

    @Override
    public HashSet<Square> getAttackingSquares(Square curr, Square[][] board) {
        HashSet<Square> attacked = new HashSet<Square>();
        attacked.addAll(checkAttackingDiagonal(curr, board));
        return attacked;
    }
}

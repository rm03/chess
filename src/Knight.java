package org.cis120.chess;

import java.util.HashSet;

public class Knight extends Piece {
    public Knight(boolean isWhite, String imgPath) {
        super(isWhite, imgPath);
    }

    @Override
    public boolean canLegallyMove(Square start, Square end, Square[][] board) {

        int rankMovement = Math.abs(end.getRank() - start.getRank());
        int fileMovement = Math.abs(end.getFile() - start.getFile());

        if (!((fileMovement == 2 && rankMovement == 1)
                || (fileMovement == 1 && rankMovement == 2))) {
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

        if (curr.getFile() - 1 >= 0 && curr.getRank() - 2 >= 0) {
            attacked.add(board[curr.getFile() - 1][curr.getRank() - 2]);
        }
        if (curr.getFile() - 1 >= 0 && curr.getRank() + 2 <= 7) {
            attacked.add(board[curr.getFile() - 1][curr.getRank() + 2]);
        }

        if (curr.getFile() + 1 <= 7 && curr.getRank() + 2 <= 7) {
            attacked.add(board[curr.getFile() + 1][curr.getRank() + 2]);
        }
        if (curr.getFile() + 1 <= 7 && curr.getRank() - 2 >= 0) {
            attacked.add(board[curr.getFile() + 1][curr.getRank() - 2]);
        }

        if (curr.getFile() - 2 >= 0 && curr.getRank() - 1 >= 0) {
            attacked.add(board[curr.getFile() - 2][curr.getRank() - 1]);
        }
        if (curr.getFile() - 2 >= 0 && curr.getRank() + 1 <= 7) {
            attacked.add(board[curr.getFile() - 2][curr.getRank() + 1]);
        }

        if (curr.getFile() + 2 <= 7 && curr.getRank() + 1 <= 7) {
            attacked.add(board[curr.getFile() + 2][curr.getRank() + 1]);
        }
        if (curr.getFile() + 2 <= 7 && curr.getRank() - 1 >= 0) {
            attacked.add(board[curr.getFile() + 2][curr.getRank() - 1]);
        }

        return attacked;
    }
}

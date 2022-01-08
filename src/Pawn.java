package org.cis120.chess;

import java.util.HashSet;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, String imgPath) {
        super(isWhite, imgPath);
    }

    @Override
    public boolean canLegallyMove(Square start, Square end, Square[][] board) {

        int rankMovement = Math.abs(end.getRank() - start.getRank());
        int fileMovement = Math.abs(end.getFile() - start.getFile());

        int nonAbsRankMovement = end.getRank() - start.getRank();
        if (end.containsPiece()) {
            if (this.isPieceWhite() == end.getPiece().isPieceWhite()
                    || end.getPiece() instanceof King) {
                return false;
            }
        }

        if ((rankMovement > 2 || fileMovement > 1) ||
                ((fileMovement == 0 || fileMovement == 1) && rankMovement == 0)) {
            return false;
        }

        int x = start.getFile();
        int y = start.getRank();

        if (this.isPieceWhite()) {
            if (nonAbsRankMovement == 2) {
                return !(y != 1 || checkRank(end, board, x, y) || end.containsPiece());
            } else if (nonAbsRankMovement == 1 && fileMovement == 1) {
                return !(end.getPiece() == null);
            } else if (nonAbsRankMovement == 1) {
                return !(end.containsPiece());
            } else {
                return false;
            }
        } else {
            if (nonAbsRankMovement == -2) {
                return !(y != 6 || checkRank(end, board, x, y) || end.containsPiece());
            } else if (nonAbsRankMovement == -1 && fileMovement == 1) {
                return !(end.getPiece() == null);
            } else if (nonAbsRankMovement == -1) {
                return !end.containsPiece();
            } else {
                return false;
            }
        }
    }

    @Override
    public HashSet<Square> getAttackingSquares(Square curr, Square[][] board) {
        HashSet<Square> attacked = new HashSet<Square>();
        if (this.isPieceWhite()) {
            if (curr.getFile() - 1 >= 0 && curr.getRank() + 1 <= 7) {
                attacked.add(board[curr.getFile() - 1][curr.getRank() + 1]);
            }
            if (curr.getFile() + 1 <= 7 && curr.getRank() + 1 <= 7) {
                attacked.add(board[curr.getFile() + 1][curr.getRank() + 1]);
            }

        } else {
            if (curr.getFile() - 1 >= 0 && curr.getRank() - 1 >= 0) {
                attacked.add(board[curr.getFile() - 1][curr.getRank() - 1]);
            }
            if (curr.getFile() + 1 <= 7 && curr.getRank() - 1 >= 0) {
                attacked.add(board[curr.getFile() + 1][curr.getRank() - 1]);
            }
        }
        return attacked;
    }
}

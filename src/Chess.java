package org.cis120.chess;

import javax.swing.*;
import java.util.LinkedList;
import java.util.HashSet;

public class Chess {

    private Square[][] board;
    private boolean whiteToMove;
    private boolean gameOver;
    private LinkedList<Move> moveList;
    private HashSet<Square> attackedByBlack;
    private HashSet<Square> attackedByWhite;
    private King whiteKing;
    private King blackKing;
    private boolean toUndo;

    public Chess() {
        reset();
    }

    public Square[][] getBoard() {
        return this.board;
    }

    public void reset() {
        this.board = new Square[8][8];
        this.whiteToMove = true;
        this.gameOver = false;
        this.moveList = new LinkedList<Move>();
        this.attackedByBlack = new HashSet<Square>();
        this.attackedByWhite = new HashSet<Square>();
        this.whiteKing = new King(true, "files/wK.png");
        this.blackKing = new King(false, "files/bK.png");

        for (int i = 0; i < 8; i++) {
            boolean squareLight = (i % 2 == 0);
            board[i][1] = new Square(
                    new Pawn(true, "files/wP.png"), 1, i, i * 75, 450, squareLight
            );
            for (int j = 2; j <= 5; j++) {
                boolean sqLt = (j % 2 == 1);
                if (!squareLight) {
                    sqLt = !sqLt;
                }
                board[i][j] = new Square(null, j, i, i * 75, 525 - 75 * j, sqLt);
            }
            board[i][6] = new Square(
                    new Pawn(false, "files/bP.png"), 6, i, i * 75, 75, !squareLight
            );
        }
        board[0][0] = new Square(new Rook(true, "files/wR.png"), 0, 0, 0, 525, false);
        board[7][0] = new Square(new Rook(true, "files/wR.png"), 0, 7, 525, 525, true);
        board[1][0] = new Square(new Knight(true, "files/wN.png"), 0, 1, 75, 525, true);
        board[6][0] = new Square(new Knight(true, "files/wN.png"), 0, 6, 450, 525, false);
        board[2][0] = new Square(new Bishop(true, "files/wB.png"), 0, 2, 150, 525, false);
        board[5][0] = new Square(new Bishop(true, "files/wB.png"), 0, 5, 375, 525, true);
        board[3][0] = new Square(new Queen(true, "files/wQ.png"), 0, 3, 225, 525, true);
        board[4][0] = new Square(whiteKing, 0, 4, 300, 525, false);
        whiteKing.setCurrSquare(board[4][0]);

        board[0][7] = new Square(new Rook(false, "files/bR.png"), 7, 0, 0, 0, true);
        board[7][7] = new Square(new Rook(false, "files/bR.png"), 7, 7, 525, 0, false);
        board[1][7] = new Square(new Knight(false, "files/bN.png"), 7, 1, 75, 0, false);
        board[6][7] = new Square(new Knight(false, "files/bN.png"), 7, 6, 450, 0, true);
        board[2][7] = new Square(new Bishop(false, "files/bB.png"), 7, 2, 150, 0, true);
        board[5][7] = new Square(new Bishop(false, "files/bB.png"), 7, 5, 375, 0, false);
        board[3][7] = new Square(new Queen(false, "files/bQ.png"), 7, 3, 225, 0, false);
        board[4][7] = new Square(blackKing, 7, 4, 300, 0, true);
        blackKing.setCurrSquare(board[4][7]);
        updateAttackedSquares();

    }

    public void updateAttackedSquares() {
        attackedByWhite.clear();
        attackedByBlack.clear();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (this.board[j][i].containsPiece()) {
                    if (this.board[j][i].getPiece().isPieceWhite()) {
                        attackedByWhite.addAll(
                                this.board[j][i].getPiece().getAttackingSquares(board[j][i], board)
                        );
                    } else {
                        attackedByBlack.addAll(
                                this.board[j][i].getPiece().getAttackingSquares(board[j][i], board)
                        );
                    }
                }
            }
        }
    }

    public HashSet<Square> getAttackedByBlack() {
        return this.attackedByBlack;
    }

    public HashSet<Square> getAttackedByWhite() {
        return this.attackedByWhite;
    }

    public LinkedList<Move> getMoveList() {
        return this.moveList;
    }

    public boolean isKingAttacked() {
        if (this.whiteToMove) {
            if (getAttackedByBlack().contains(whiteKing.getCurrSquare())) {
                return true;
            }
        } else {
            if (getAttackedByWhite().contains(blackKing.getCurrSquare())) {
                return true;
            }
        }
        return false;
    }

    public boolean isWhiteKingAttacked() {
        if (getAttackedByBlack().contains(whiteKing.getCurrSquare())) {
            return true;
        }
        return false;
    }

    public boolean isBlackKingAttacked() {
        if (getAttackedByWhite().contains(blackKing.getCurrSquare())) {
            return true;
        }
        return false;
    }

    public boolean isGameOver() {
        return this.gameOver;
    }

    public void undo() {
        try {
            Move last = this.moveList.pop();
            if (last.getIsEnPassant()) {
                this.board[last.getStart().getFile()][last.getStart().getRank()]
                        .setPiece(last.getMovedPiece());
                this.board[last.getEnd().getFile()][last.getEnd().getRank()].setPiece(null);
                if (last.getMovedPiece().isPieceWhite()) {
                    this.board[last.getEnd().getFile()][last.getEnd().getRank() - 1]
                            .setPiece(last.getPiecePresentBefore());
                } else {
                    this.board[last.getEnd().getFile()][last.getEnd().getRank() + 1]
                            .setPiece(last.getPiecePresentBefore());
                }
                this.whiteToMove = !this.whiteToMove;
            } else if (last.getIsCastling()) {
                if (last.getStart().getRank() == 0) {
                    whiteKing.setCurrSquare(board[4][0]);
                    this.board[4][0].setPiece(whiteKing);
                    if (last.getEnd().getFile() - last.getStart().getFile() == 2) {
                        this.board[6][0].setPiece(null);
                        this.board[7][0].setPiece(board[5][0].getPiece());
                        this.board[5][0].setPiece(null);
                    }
                    if (last.getEnd().getFile() - last.getStart().getFile() == -2) {
                        this.board[2][0].setPiece(null);
                        this.board[0][0].setPiece(board[3][0].getPiece());
                        this.board[3][0].setPiece(null);
                    }
                    whiteKing.setCastled(false);
                }
                if (last.getStart().getRank() == 7) {
                    blackKing.setCurrSquare(board[4][7]);
                    this.board[4][7].setPiece(blackKing);
                    if (last.getEnd().getFile() - last.getStart().getFile() == 2) {
                        this.board[6][7].setPiece(null);
                        this.board[7][7].setPiece(board[5][7].getPiece());
                        this.board[5][7].setPiece(null);
                    }
                    if (last.getEnd().getFile() - last.getStart().getFile() == -2) {
                        this.board[2][7].setPiece(null);
                        this.board[0][7].setPiece(board[3][7].getPiece());
                        this.board[3][7].setPiece(null);
                    }
                    blackKing.setCastled(false);
                }
                this.whiteToMove = !this.whiteToMove;
            } else if (last.getIsPromotion()) {
                this.board[last.getStart().getFile()][last.getStart().getRank()]
                        .setPiece(last.getStart().getPiece());
                this.board[last.getEnd().getFile()][last.getEnd().getRank()]
                        .setPiece(last.getPiecePresentBefore());
                this.whiteToMove = !this.whiteToMove;
            } else {
                if (last.getMovedPiece() instanceof Rook) {
                    ((Rook) last.getMovedPiece()).setMoved(false);
                }
                this.board[last.getStart().getFile()][last.getStart().getRank()]
                        .setPiece(last.getMovedPiece());
                this.board[last.getEnd().getFile()][last.getEnd().getRank()]
                        .setPiece(last.getPiecePresentBefore());
                if (last.getMovedPiece() instanceof King) {
                    if (last.getMovedPiece().isPieceWhite()) {
                        whiteKing.setCurrSquare(last.getStart());
                    } else {
                        blackKing.setCurrSquare(last.getStart());
                    }
                }
                this.whiteToMove = !this.whiteToMove;
            }
            if (this.gameOver) {
                this.gameOver = false;

            }
            updateAttackedSquares();
        } catch (Exception e) {
            System.out.println("No moves to undo.");
        }
    }

    public boolean checkPromotion(Square start, Square end) {
        if (!start.containsPiece()) {
            return false;
        }
        if (start.getPiece().isPieceWhite() != this.whiteToMove) {
            return false;
        }
        if (whiteToMove && start.getPiece() instanceof Pawn) {
            if (end.getRank() == 7 && start.getRank() == 6) {
                if (Math.abs(end.getFile() - start.getFile()) == 1 && end.containsPiece()) {
                    if (!end.getPiece().isPieceWhite() && !(end.getPiece() instanceof King)) {
                        return true;
                    }
                } else if (end.getFile() - start.getFile() == 0 && !end.containsPiece()) {
                    return true;
                }
                return false;
            }
        }
        if (!whiteToMove && start.getPiece() instanceof Pawn) {
            if (end.getRank() == 0 && start.getRank() == 1) {
                if (Math.abs(end.getFile() - start.getFile()) == 1 && end.containsPiece()) {
                    if (end.getPiece().isPieceWhite() && !(end.getPiece() instanceof King)) {
                        return true;
                    }
                } else if (end.getFile() - start.getFile() == 0 && !end.containsPiece()) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean canCastle(Square start, Square end) {
        if (whiteToMove) {
            if (!((start.getPiece() instanceof King) && start.getPiece().isPieceWhite())) {
                return false;
            }
            if (whiteKing.hasCastled || whiteKing.hasMoved || isWhiteKingAttacked()) {
                return false;
            }
            if (start.getRank() == 0 && end.getRank() == 0) {
                if (end.getFile() - start.getFile() == 2) {
                    if (getAttackedByBlack().contains(board[5][0])
                            || getAttackedByBlack().contains(board[6][0])) {
                        return false;
                    }
                    if (board[5][0].containsPiece() || board[6][0].containsPiece()) {
                        return false;
                    }
                    if (board[7][0].containsPiece()) {
                        if (board[7][0].getPiece() instanceof Rook &&
                                board[7][0].getPiece().isPieceWhite()) {
                            return !(((Rook) board[7][0].getPiece()).hasMoved());
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                if (end.getFile() - start.getFile() == -2) {
                    if (getAttackedByBlack().contains(board[2][0])
                            || getAttackedByBlack().contains(board[3][0])) {
                        return false;
                    }
                    if (board[2][0].containsPiece() || board[3][0].containsPiece()) {
                        return false;
                    }
                    if (board[0][0].containsPiece()) {
                        if (board[0][0].getPiece() instanceof Rook &&
                                board[0][0].getPiece().isPieceWhite()) {
                            return !(((Rook) board[0][0].getPiece()).hasMoved());
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            return false;
        } else {
            if (!((start.getPiece() instanceof King) && !start.getPiece().isPieceWhite())) {
                return false;
            }
            if (blackKing.hasCastled || blackKing.hasMoved || isBlackKingAttacked()) {
                return false;
            }
            if (start.getRank() == 7 && end.getRank() == 7) {
                if (end.getFile() - start.getFile() == 2) {
                    if (getAttackedByWhite().contains(board[5][7])
                            || getAttackedByWhite().contains(board[6][7])) {
                        return false;
                    }
                    if (board[5][7].containsPiece() || board[6][7].containsPiece()) {
                        return false;
                    }
                    if (board[7][7].containsPiece()) {
                        if (board[7][7].getPiece() instanceof Rook &&
                                !board[7][7].getPiece().isPieceWhite()) {
                            return !(((Rook) board[7][7].getPiece()).hasMoved());
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                if (end.getFile() - start.getFile() == -2) {
                    if (getAttackedByWhite().contains(board[2][7])
                            || getAttackedByWhite().contains(board[3][7])) {
                        return false;
                    }
                    if (board[2][7].containsPiece() || board[3][7].containsPiece()) {
                        return false;
                    }
                    if (board[0][7].containsPiece()) {
                        if (board[0][7].getPiece() instanceof Rook &&
                                !board[0][7].getPiece().isPieceWhite()) {
                            return !(((Rook) board[0][7].getPiece()).hasMoved());
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }
    }

    public boolean isStalemated() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (whiteToMove && this.board[j][i].containsPiece()) {
                    if (this.board[j][i].getPiece().isPieceWhite()) {
                        for (int k = 0; k <= 7; k++) {
                            for (int l = 0; l <= 7; l++) {
                                if (this.board[j][i].getPiece().canLegallyMove(
                                        this.board[j][i], this.board[k][l], this.board
                                )) {
                                    checkMatePlayTurn(this.board[j][i], this.board[k][l], null);
                                    if (isWhiteKingAttacked() || !toUndo) {
                                        if (toUndo) {
                                            undo();
                                        }
                                    } else {
                                        if (toUndo) {
                                            undo();
                                        }
                                        return false;
                                    }
                                }

                            }
                        }
                    }
                }
                if (!whiteToMove && this.board[j][i].containsPiece()) {
                    if (!this.board[j][i].getPiece().isPieceWhite()) {
                        for (int k = 0; k <= 7; k++) {
                            for (int l = 0; l <= 7; l++) {
                                if (this.board[j][i].getPiece().canLegallyMove(
                                        this.board[j][i], this.board[k][l], this.board
                                )) {

                                    checkMatePlayTurn(this.board[j][i], this.board[k][l], null);
                                    if (isBlackKingAttacked() || !toUndo) {
                                        if (toUndo) {
                                            undo();
                                        }
                                    } else {
                                        if (toUndo) {
                                            undo();
                                        }
                                        return false;
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
        this.gameOver = true;

        return true;
    }

    public boolean isCheckmated() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (whiteToMove && this.board[j][i].containsPiece()) {
                    if (this.board[j][i].getPiece().isPieceWhite()) {
                        for (int k = 0; k <= 7; k++) {
                            for (int l = 0; l <= 7; l++) {
                                if (this.board[j][i].getPiece().canLegallyMove(
                                        this.board[j][i], this.board[k][l], this.board
                                )) {
                                    checkMatePlayTurn(this.board[j][i], this.board[k][l], null);
                                    if (!isWhiteKingAttacked()) {
                                        undo();
                                        return false;
                                    }
                                    if (toUndo) {
                                        undo();
                                    }
                                }

                            }
                        }
                    }
                }
                if (!whiteToMove && this.board[j][i].containsPiece()) {
                    if (!this.board[j][i].getPiece().isPieceWhite()) {
                        for (int k = 0; k <= 7; k++) {
                            for (int l = 0; l <= 7; l++) {
                                if (this.board[j][i].getPiece().canLegallyMove(
                                        this.board[j][i], this.board[k][l], this.board
                                )) {
                                    checkMatePlayTurn(this.board[j][i], this.board[k][l], null);
                                    if (!isBlackKingAttacked()) {
                                        undo();
                                        return false;
                                    }
                                    if (toUndo) {
                                        undo();
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        this.gameOver = true;

        return true;
    }

    public boolean checkEnPassant(Square start, Square end) {
        if (whiteToMove && start.getPiece() instanceof Pawn) {
            try {
                Move prev = moveList.peek();
                if (prev.getMovedPiece() instanceof Pawn
                        && prev.getEnd().getRank() - prev.getStart().getRank() == -2) {
                    return (Math.abs(start.getFile() - prev.getStart().getFile()) == 1
                            && end.getRank() - start.getRank() == 1
                            && Math.abs(end.getFile() - start.getFile()) == 1
                            && end.getFile() - prev.getStart().getFile() == 0
                            && !end.containsPiece());
                }
            } catch (Exception e) {
                System.out.println("No moves have been made, so en passant cannot occur.");
                return false;
            }
        } else if (!whiteToMove && start.getPiece() instanceof Pawn) {
            try {
                Move prev = moveList.peek();
                if (prev.getMovedPiece() instanceof Pawn
                        && prev.getEnd().getRank() - prev.getStart().getRank() == 2) {
                    return (Math.abs(start.getFile() - prev.getStart().getFile()) == 1
                            && end.getRank() - start.getRank() == -1
                            && Math.abs(end.getFile() - start.getFile()) == 1
                            && end.getFile() - prev.getStart().getFile() == 0
                            && !end.containsPiece());
                }
            } catch (Exception e) {
                System.out.println("No moves have been made, so en passant cannot occur.");
                return false;
            }
        }
        return false;
    }

    public boolean playTurn(Square start, Square end, Object toPromote) {
        if (gameOver) {
            return false;
        }

        if (!start.containsPiece()) {
            return false;
        }
        if (start.getPiece().isPieceWhite() != this.whiteToMove) {
            return false;
        }

        if (whiteToMove && start.getPiece() instanceof King) {
            if (attackedByBlack.contains(end)) {
                return false;
            }
        }
        if (!whiteToMove && start.getPiece() instanceof King) {
            if (attackedByWhite.contains(end)) {
                return false;
            }
        }

        if (checkEnPassant(start, end)) {
            Move prev = moveList.peek();
            Piece moved = start.getPiece();
            moveList.push(new Move(start, end, moved, prev.getMovedPiece(), true, false, false));
            board[prev.getEnd().getFile()][prev.getEnd().getRank()].setPiece(null);
            board[start.getFile()][start.getRank()].setPiece(null);
            board[end.getFile()][end.getRank()].setPiece(moved);
            this.whiteToMove = !this.whiteToMove;
            updateAttackedSquares();
            return true;
        }
        if (canCastle(start, end)) {
            Piece moved = start.getPiece();
            moveList.push(new Move(start, end, moved, null, false, true, false));
            board[start.getFile()][start.getRank()].setPiece(null);
            if (whiteToMove) {
                if (end.getFile() - start.getFile() == 2) {
                    whiteKing.setCurrSquare(board[6][0]);
                    board[6][0].setPiece(whiteKing);
                    board[5][0].setPiece(board[7][0].getPiece());
                    board[7][0].setPiece(null);
                }
                if (end.getFile() - start.getFile() == -2) {
                    whiteKing.setCurrSquare(board[2][0]);
                    board[2][0].setPiece(whiteKing);
                    board[3][0].setPiece(board[0][0].getPiece());
                    board[0][0].setPiece(null);
                }
                whiteKing.setCastled(true);
                whiteKing.setHasMoved(true);
            } else {
                if (end.getFile() - start.getFile() == 2) {
                    blackKing.setCurrSquare(board[6][7]);
                    board[6][7].setPiece(blackKing);
                    board[5][7].setPiece(board[7][7].getPiece());
                    board[7][7].setPiece(null);
                }
                if (end.getFile() - start.getFile() == -2) {
                    blackKing.setCurrSquare(board[2][7]);
                    board[2][7].setPiece(blackKing);
                    board[3][7].setPiece(board[0][7].getPiece());
                    board[0][7].setPiece(null);
                }
                blackKing.setCastled(true);
                blackKing.setHasMoved(true);
            }
            this.whiteToMove = !this.whiteToMove;
            updateAttackedSquares();
            return true;
        }

        if (start.getPiece().canLegallyMove(start, end, board)) {
            Piece moved = start.getPiece();
            if (checkPromotion(start, end)) {
                Piece p = null;
                if (toPromote.toString() == "Queen") {
                    if (whiteToMove) {
                        p = new Queen(true, "files/wQ.png");
                    } else {
                        p = new Queen(false, "files/bQ.png");
                    }
                } else if (toPromote.toString() == "Rook") {
                    if (whiteToMove) {
                        p = new Rook(true, "files/wR.png");
                    } else {
                        p = new Rook(false, "files/bR.png");
                    }
                } else if (toPromote.toString() == "Bishop") {
                    if (whiteToMove) {
                        p = new Bishop(true, "files/wB.png");
                    } else {
                        p = new Bishop(false, "files/bB.png");
                    }
                } else if (toPromote.toString() == "Knight") {
                    if (whiteToMove) {
                        p = new Knight(true, "files/wN.png");
                    } else {
                        p = new Knight(false, "files/bN.png");
                    }
                }
                moveList.push(
                        new Move(
                                new Square(
                                        start.getPiece(), start.getRank(), start.getFile(),
                                        start.getBoardX(), start.getBoardY(), start.getIsLight()
                                ),
                                end, moved, end.getPiece(), false, false, true
                        )
                );
                board[start.getFile()][start.getRank()].setPiece(null);
                board[end.getFile()][end.getRank()].setPiece(p);
                this.whiteToMove = !this.whiteToMove;
                updateAttackedSquares();
                return true;
            } else {
                moveList.push(new Move(start, end, moved, end.getPiece(), false, false, false));
                if (moved instanceof Rook) {
                    ((Rook) moved).setMoved(true);
                }
                board[start.getFile()][start.getRank()].setPiece(null);
                board[end.getFile()][end.getRank()].setPiece(moved);
                if (moved instanceof King) {
                    if (moved.isPieceWhite()) {
                        whiteKing.setCurrSquare(board[end.getFile()][end.getRank()]);
                        whiteKing.setHasMoved(true);
                    } else {
                        blackKing.setCurrSquare(board[end.getFile()][end.getRank()]);
                        blackKing.setHasMoved(true);
                    }
                }
                this.whiteToMove = !this.whiteToMove;
                updateAttackedSquares();
                if (isStalemated() && !isKingAttacked()) {
                    JOptionPane.showMessageDialog(null, "Stalemate.");
                    return true;
                }
                if (isKingAttacked() && isCheckmated()) {
                    if (whiteToMove) {
                        JOptionPane.showMessageDialog(null, "Checkmate. Black wins.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Checkmate. White wins.");
                    }
                    return true;
                }
                if (!this.whiteToMove) {
                    if (isWhiteKingAttacked()) {
                        undo();
                        return false;
                    }
                } else {
                    if (isBlackKingAttacked()) {
                        undo();
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void checkMatePlayTurn(Square start, Square end, Object toPromote) {
        if (!start.containsPiece()) {
            toUndo = false;
            return;
        }
        if (start.getPiece().isPieceWhite() != this.whiteToMove) {
            toUndo = false;
            return;
        }

        if (whiteToMove && start.getPiece() instanceof King) {
            if (attackedByBlack.contains(end)) {
                toUndo = false;
                return;
            }
        }
        if (!whiteToMove && start.getPiece() instanceof King) {
            if (attackedByWhite.contains(end)) {
                toUndo = false;
                return;
            }
        }
        toUndo = true;

        if (checkEnPassant(start, end)) {
            Move prev = moveList.peek();
            Piece moved = start.getPiece();
            moveList.push(new Move(start, end, moved, prev.getMovedPiece(), true, false, false));
            board[prev.getEnd().getFile()][prev.getEnd().getRank()].setPiece(null);
            board[start.getFile()][start.getRank()].setPiece(null);
            board[end.getFile()][end.getRank()].setPiece(moved);
            this.whiteToMove = !this.whiteToMove;
            updateAttackedSquares();
        }
        if (start.getPiece().canLegallyMove(start, end, board)) {
            Piece moved = start.getPiece();
            moveList.push(new Move(start, end, moved, end.getPiece(), false, false, false));
            board[start.getFile()][start.getRank()].setPiece(null);
            board[end.getFile()][end.getRank()].setPiece(moved);
            if (moved instanceof King) {
                if (moved.isPieceWhite()) {
                    whiteKing.setCurrSquare(board[end.getFile()][end.getRank()]);
                } else {
                    blackKing.setCurrSquare(board[end.getFile()][end.getRank()]);
                }
            }
            this.whiteToMove = !this.whiteToMove;
            updateAttackedSquares();
        }
    }

    public boolean isWhiteToMove() {
        return this.whiteToMove;
    }

}

package org.cis120.chess;

public class Move {
    private Square start;
    private Square end;
    private Piece moved;
    private Piece presentBefore;
    private boolean isEnPassant;
    private boolean isCastling;
    private boolean isPromotion;

    public Move(
            Square start, Square end, Piece moved, Piece presentBefore,
            boolean isEnPassant, boolean isCastling, boolean isPromotion
    ) {
        this.start = start;
        this.end = end;
        this.moved = moved;
        this.presentBefore = presentBefore;
        this.isEnPassant = isEnPassant;
        this.isCastling = isCastling;
        this.isPromotion = isPromotion;
    }

    public Square getStart() {
        return this.start;
    }

    public Square getEnd() {
        return this.end;
    }

    public Piece getMovedPiece() {
        return this.moved;
    }

    public Piece getPiecePresentBefore() {
        return this.presentBefore;
    }

    public boolean getIsEnPassant() {
        return this.isEnPassant;
    }

    public boolean getIsCastling() {
        return this.isCastling;
    }

    public boolean getIsPromotion() {
        return this.isPromotion;
    }
}

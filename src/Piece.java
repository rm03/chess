package org.cis120.chess;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public abstract class Piece extends JComponent {
    private boolean isWhite;
    private BufferedImage img;

    public Piece(boolean isWhite, String imgFile) {
        this.isWhite = isWhite;
        try {
            if (img == null) {
                img = ImageIO.read(new File(imgFile));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    public boolean isPieceWhite() {
        return this.isWhite;
    }

    public static boolean checkRank(Square end, Square[][] board, int x, int y) {
        while (Math.abs(end.getRank() - y) != 1) {
            if (y < end.getRank()) {
                y += 1;
            } else {
                y -= 1;
            }
            Square currSquare = board[x][y];
            if (currSquare.containsPiece()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkFile(Square end, Square[][] board, int x, int y) {
        while (Math.abs(end.getFile() - x) != 1) {
            if (x < end.getFile()) {
                x += 1;
            } else {
                x -= 1;
            }
            Square currSquare = board[x][y];
            if (currSquare.containsPiece()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkDiagonal(Square end, Square[][] board, int x, int y) {
        while (Math.abs(end.getFile() - x) != 1 && Math.abs(end.getRank() - y) != 1) {
            if (x < end.getFile()) {
                if (y < end.getRank()) {
                    x += 1;
                    y += 1;
                } else {
                    x += 1;
                    y -= 1;
                }
            } else {
                if (y < end.getRank()) {
                    x -= 1;
                    y += 1;
                } else {
                    x -= 1;
                    y -= 1;
                }
            }
            Square currSquare = board[x][y];
            if (currSquare.containsPiece()) {
                return true;
            }
        }
        return false;
    }

    public static HashSet<Square> checkAttackingRank(Square curr, Square[][] board) {
        HashSet<Square> attacked = new HashSet<Square>();
        int file = curr.getFile();
        if (file < 7) {
            for (int i = file + 1; i <= 7; i++) {
                if (board[i][curr.getRank()].containsPiece()) {
                    if (board[i][curr.getRank()].getPiece() instanceof King &&
                            board[i][curr.getRank()].getPiece().isPieceWhite() != curr.getPiece()
                                    .isPieceWhite()) {
                        attacked.add(board[i][curr.getRank()]);
                    } else {
                        attacked.add(board[i][curr.getRank()]);
                        break;
                    }
                } else {
                    attacked.add(board[i][curr.getRank()]);
                }
            }
        }
        file = curr.getFile();
        if (file > 0) {
            for (int j = file - 1; j >= 0; j--) {
                if (board[j][curr.getRank()].containsPiece()) {
                    if (board[j][curr.getRank()].getPiece() instanceof King &&
                            board[j][curr.getRank()].getPiece().isPieceWhite() != curr.getPiece()
                                    .isPieceWhite()) {
                        attacked.add(board[j][curr.getRank()]);
                    } else {
                        attacked.add(board[j][curr.getRank()]);
                        break;
                    }
                } else {
                    attacked.add(board[j][curr.getRank()]);
                }
            }
        }
        return attacked;
    }

    public static HashSet<Square> checkAttackingFile(Square curr, Square[][] board) {
        HashSet<Square> attacked = new HashSet<Square>();
        int rank = curr.getRank();
        if (rank < 7) {
            for (int i = rank + 1; i <= 7; i++) {
                if (board[curr.getFile()][i].containsPiece()) {
                    if (board[curr.getFile()][i].getPiece() instanceof King &&
                            board[curr.getFile()][i].getPiece().isPieceWhite() != curr.getPiece()
                                    .isPieceWhite()) {
                        attacked.add(board[curr.getFile()][i]);
                    } else {
                        attacked.add(board[curr.getFile()][i]);
                        break;
                    }
                } else {
                    attacked.add(board[curr.getFile()][i]);
                }
            }
        }
        rank = curr.getRank();
        if (rank > 0) {
            for (int j = rank - 1; j >= 0; j--) {
                if (board[curr.getFile()][j].containsPiece()) {
                    if (board[curr.getFile()][j].getPiece() instanceof King &&
                            board[curr.getFile()][j].getPiece().isPieceWhite() != curr.getPiece()
                                    .isPieceWhite()) {
                        attacked.add(board[curr.getFile()][j]);
                    } else {
                        attacked.add(board[curr.getFile()][j]);
                        break;
                    }
                } else {
                    attacked.add(board[curr.getFile()][j]);
                }
            }
        }

        return attacked;
    }

    public static HashSet<Square> checkAttackingDiagonal(Square curr, Square[][] board) {
        HashSet<Square> attacked = new HashSet<Square>();
        int rank = curr.getRank();
        int file = curr.getFile();
        while (rank != 0 && file != 0) {
            rank -= 1;
            file -= 1;
            if (board[file][rank].containsPiece()) {
                if (!((board[file][rank].getPiece() instanceof King)
                        && curr.getPiece().isPieceWhite() != board[file][rank].getPiece()
                                .isPieceWhite())) {
                    if (curr.getPiece().isPieceWhite() == board[file][rank].getPiece()
                            .isPieceWhite()) {
                        attacked.add(board[file][rank]);
                    }
                    break;
                }
            }
            attacked.add(board[file][rank]);
        }
        rank = curr.getRank();
        file = curr.getFile();
        while (rank != 7 && file != 7) {
            rank += 1;
            file += 1;
            if (board[file][rank].containsPiece()) {
                if (!((board[file][rank].getPiece() instanceof King)
                        && curr.getPiece().isPieceWhite() != board[file][rank].getPiece()
                                .isPieceWhite())) {
                    if (curr.getPiece().isPieceWhite() == board[file][rank].getPiece()
                            .isPieceWhite()) {
                        attacked.add(board[file][rank]);
                    }
                    break;
                }
            }
            attacked.add(board[file][rank]);
        }
        rank = curr.getRank();
        file = curr.getFile();
        while (rank != 7 && file != 0) {
            rank += 1;
            file -= 1;
            if (board[file][rank].containsPiece()) {
                if (!((board[file][rank].getPiece() instanceof King)
                        && curr.getPiece().isPieceWhite() != board[file][rank].getPiece()
                                .isPieceWhite())) {
                    if (curr.getPiece().isPieceWhite() == board[file][rank].getPiece()
                            .isPieceWhite()) {
                        attacked.add(board[file][rank]);
                    }
                    break;
                }
            }
            attacked.add(board[file][rank]);
        }
        rank = curr.getRank();
        file = curr.getFile();
        while (rank != 0 && file != 7) {
            rank -= 1;
            file += 1;
            if (board[file][rank].containsPiece()) {
                if (!((board[file][rank].getPiece() instanceof King)
                        && curr.getPiece().isPieceWhite() != board[file][rank].getPiece()
                                .isPieceWhite())) {
                    if (curr.getPiece().isPieceWhite() == board[file][rank].getPiece()
                            .isPieceWhite()) {
                        attacked.add(board[file][rank]);
                    }
                    break;
                }
            }
            attacked.add(board[file][rank]);
        }
        return attacked;
    }

    public void paintComponent(Graphics g, Square curr) {
        g.drawImage(img, curr.getBoardX(), curr.getBoardY(), null);
    }

    public abstract boolean canLegallyMove(Square start, Square end, Square[][] board);

    public abstract HashSet<Square> getAttackingSquares(Square curr, Square[][] board);

}

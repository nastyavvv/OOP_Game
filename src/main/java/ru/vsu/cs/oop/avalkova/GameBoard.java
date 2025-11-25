package ru.vsu.cs.oop.avalkova;

import java.util.Arrays;

public class GameBoard {
    private final Player[][] board;
    private final int size;

    public GameBoard(int size) {
        this.size = size;
        this.board = new Player[size][size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], Player.EMPTY);
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = Player.PLAYER1;
                }
            }
        }

        for (int i = size - 2; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = Player.PLAYER2;
                }
            }
        }
    }

    public Player getPieceAt(Position pos) {
        if (!pos.isValid(size)) return null;
        return board[pos.y()][pos.x()];
    }

    public void setPieceAt(Position pos, Player player) {
        if (pos.isValid(size)) {
            board[pos.y()][pos.x()] = player;
        }
    }

    public void applyMove(Move move) {
        setPieceAt(move.from(), Player.EMPTY);
        setPieceAt(move.to(), move.player());
    }

    public void display() {
        System.out.println("\n  " + "0 1 2 3 4 5");
        for (int i = 0; i < size; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public int getSize() {
        return size;
    }
}

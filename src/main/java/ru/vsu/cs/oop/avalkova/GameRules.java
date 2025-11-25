package ru.vsu.cs.oop.avalkova;

import java.util.*;

public class GameRules {
    private static final List<Position> DIRECTIONS = Arrays.asList(
            new Position(1, 0), new Position(-1, 0),
            new Position(0, 1), new Position(0, -1),
            new Position(1, 1), new Position(-1, -1),
            new Position(1, -1), new Position(-1, 1)
    );

    public boolean isValidMove(Move move, GameBoard board) { // Исправлен параметр
        if (!move.from().isValid(board.getSize()) || !move.to().isValid(board.getSize())) {
            return false;
        }

        if (board.getPieceAt(move.from()) != move.player()) {
            return false;
        }

        if (board.getPieceAt(move.to()) != Player.EMPTY) {
            return false;
        }

        return move.isJump() ? isValidJump(move, board) : move.isValidStep();
    }

    private boolean isValidJump(Move move, GameBoard board) {
        Position middle = move.from().midpoint(move.to());
        if (!middle.isValid(board.getSize())) return false;

        Player middlePiece = board.getPieceAt(middle);
        return middlePiece != Player.EMPTY;
    }

    public List<Move> getValidMoves(Player player, GameBoard board) {
        List<Move> moves = new ArrayList<>();

        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                Position from = new Position(x, y);
                if (board.getPieceAt(from) == player) {
                    moves.addAll(getMovesFromPosition(from, player, board));
                }
            }
        }
        return moves;
    }

    private List<Move> getMovesFromPosition(Position from, Player player, GameBoard board) {
        List<Move> moves = new ArrayList<>();

        for (Position dir : DIRECTIONS) {
            Position to = from.add(dir);
            Move move = new Move(from, to, player);
            if (isValidMove(move, board)) {
                moves.add(move);
            }
        }

        findJumpMoves(from, from, player, board, moves, new HashSet<>());
        return moves;
    }

    private void findJumpMoves(Position originalFrom, Position current, Player player,
                               GameBoard board, List<Move> moves, Set<Position> visited) {
        visited.add(current);

        for (Position dir : DIRECTIONS) {
            Position middle = current.add(dir);
            Position to = middle.add(dir);

            if (!visited.contains(to) && to.isValid(board.getSize())) {
                Move jumpMove = new Move(originalFrom, to, player);
                if (isValidMove(jumpMove, board)) {
                    moves.add(jumpMove);
                    findJumpMoves(originalFrom, to, player, board, moves, visited);
                }
            }
        }
    }

    public boolean isGameOver(GameBoard board, Player currentPlayer) {
        int targetRow = (currentPlayer == Player.PLAYER1) ? board.getSize() - 1 : 0;

        for (int x = 0; x < board.getSize(); x++) {
            if (board.getPieceAt(new Position(x, targetRow)) == currentPlayer) {
                return true;
            }
        }
        return false;
    }
}
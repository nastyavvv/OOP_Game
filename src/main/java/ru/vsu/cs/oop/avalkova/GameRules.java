package ru.vsu.cs.oop.avalkova;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, содержащий правила игры в китайские шашки.
 * Проверяет корректность ходов и определяет победителя.
 */
public class GameRules {
    private static final int[][] DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}
    };

    /**
     * Проверяет, является ли ход допустимым.
     * @param move Ход для проверки
     * @param board Игровая доска
     * @return true если ход допустим, false в противном случае
     */
    public boolean isValidMove(Move move, GameBoard board) {
        if (move == null) return false;

        Position from = move.getFrom();
        Position to = move.getTo();

        if (!board.isValidPosition(from) || !board.isValidPosition(to)) {
            return false;
        }

        Player playerAtFrom = board.getPieceAt(from);
        if (playerAtFrom == null || !playerAtFrom.equals(move.getPlayer())) {
            return false;
        }

        if (!board.isEmpty(to)) {
            return false;
        }

        int dx = Math.abs(to.getX() - from.getX());
        int dy = Math.abs(to.getY() - from.getY());

        if (dx <= 1 && dy <= 1 && dx + dy > 0) {
            return true;
        }

        if (dx == 2 && dy == 0) {
            Position middle = new Position((from.getX() + to.getX()) / 2, from.getY());
            return board.isValidPosition(middle) && !board.isEmpty(middle);
        }

        if (dx == 0 && dy == 2) {
            Position middle = new Position(from.getX(), (from.getY() + to.getY()) / 2);
            return board.isValidPosition(middle) && !board.isEmpty(middle);
        }

        if (dx == 2 && dy == 2) {
            Position middle = new Position((from.getX() + to.getX()) / 2, (from.getY() + to.getY()) / 2);
            return board.isValidPosition(middle) && !board.isEmpty(middle);
        }

        return false;
    }

    /**
     * Возвращает список всех допустимых ходов для указанного игрока.
     * @param player Игрок, для которого ищутся ходы
     * @param board Игровая доска
     * @return Список допустимых ходов
     */
    public List<Move> getAllValidMoves(Player player, GameBoard board) {
        List<Move> moves = new ArrayList<>();
        List<Position> pieces = board.getAllPieces(player);

        for (Position from : pieces) {
            for (int[] dir : DIRECTIONS) {
                Position stepTo = new Position(from.getX() + dir[0], from.getY() + dir[1]);
                Move stepMove = new Move(from, stepTo, player);
                if (isValidMove(stepMove, board)) {
                    moves.add(stepMove);
                }

                Position jumpTo = new Position(from.getX() + dir[0] * 2, from.getY() + dir[1] * 2);
                Move jumpMove = new Move(from, jumpTo, player);
                if (isValidMove(jumpMove, board)) {
                    moves.add(jumpMove);
                }
            }
        }

        return moves;
    }

    /**
     * Проверяет, выиграл ли указанный игрок.
     * @param player Игрок для проверки
     * @param board Игровая доска
     * @return true если игрок выиграл, false в противном случае
     */
    public boolean hasWon(Player player, GameBoard board) {
        List<Position> pieces = board.getAllPieces(player);
        if (pieces.size() < 10) return false;

        for (Position pos : pieces) {
            if (!board.isInGoalArea(pos, player)) {
                return false;
            }
        }
        return true;
    }
}
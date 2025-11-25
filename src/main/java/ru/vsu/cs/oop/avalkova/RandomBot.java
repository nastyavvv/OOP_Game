package ru.vsu.cs.oop.avalkova;

import java.util.List;
import java.util.Random;

public class RandomBot implements PlayerStrategy {
    private final Random random;
    private final GameRules rules;

    public RandomBot(GameRules rules) {
        this.random = new Random();
        this.rules = rules;
    }

    @Override
    public Move makeMove(GameBoard board, Player player) {
        List<Move> validMoves = rules.getValidMoves(player, board);

        if (validMoves.isEmpty()) {
            throw new IllegalStateException("Нет возможных ходов!");
        }

        Move move = validMoves.get(random.nextInt(validMoves.size()));
        System.out.println("Бот ходит: " + move.from() + " -> " + move.to());
        return move;
    }

    @Override
    public String getName() {
        return "Бот";
    }
}
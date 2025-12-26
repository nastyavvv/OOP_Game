package ru.vsu.cs.oop.avalkova;

import java.util.List;
import java.util.Random;

/**
 * Стратегия случайного бота. Бот выбирает случайный допустимый ход.
 */
public class RandomBot implements PlayerStrategy {
    private final Random random;
    private final String name;

    /**
     * Создает бота с указанным номером.
     * @param number Номер бота
     */
    public RandomBot(int number) {
        this.random = new Random();
        this.name = "Бот " + number;
    }

    /**
     * Выбирает случайный ход из доступных допустимых ходов.
     * Добавляет искусственную задержку для имитации размышлений.
     * @param board Текущее состояние игровой доски
     * @param rules Правила игры
     * @param player Текущий игрок (бот)
     * @return Случайно выбранный допустимый ход
     */
    @Override
    public Move makeMove(GameBoard board, GameRules rules, Player player) {
        List<Move> validMoves = rules.getAllValidMoves(player, board);

        if (validMoves.isEmpty()) {
            return null;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        Move move = validMoves.get(random.nextInt(validMoves.size()));
        System.out.println(name + " ходит: " + move.getFrom() + " → " + move.getTo());

        return move;
    }

    /**
     * Возвращает имя бота.
     * @return Имя бота в формате "Бот X"
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Определяет, что этот игрок является ботом.
     * @return Всегда возвращает false
     */
    @Override
    public boolean isHuman() {
        return false;
    }
}
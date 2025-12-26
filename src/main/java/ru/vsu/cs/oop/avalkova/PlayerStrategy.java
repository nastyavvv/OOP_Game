package ru.vsu.cs.oop.avalkova;

/**
 * Интерфейс стратегии игрока. Определяет поведение игрока (человек или бот).
 */
public interface PlayerStrategy {

    /**
     * Выполняет ход игрока на основе текущего состояния доски.
     * @param board Текущее состояние игровой доски
     * @param rules Правила игры
     * @param player Текущий игрок
     * @return Ход, который совершает игрок
     */
    Move makeMove(GameBoard board, GameRules rules, Player player);

    /**
     * Возвращает имя стратегии/игрока.
     * @return Имя игрока
     */
    String getName();

    /**
     * Определяет, является ли игрок человеком.
     * @return true, если игрок человек, false если бот
     */
    boolean isHuman();
}
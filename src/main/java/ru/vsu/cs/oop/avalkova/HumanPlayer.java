package ru.vsu.cs.oop.avalkova;

import java.util.List;
import java.util.Scanner;

/**
 * Стратегия игрока-человека. Обрабатывает ввод хода от пользователя через консоль.
 */
public class HumanPlayer implements PlayerStrategy {
    private final Scanner scanner;
    private final String name;

    /**
     * Создает нового игрока-человека с указанным сканером для ввода.
     * @param scanner Сканер для чтения ввода пользователя
     */
    public HumanPlayer(Scanner scanner) {
        this.scanner = scanner;
        this.name = "Человек";
    }

    /**
     * Запрашивает у пользователя ввод хода и проверяет его корректность.
     * @param board Текущее состояние игровой доски
     * @param rules Правила игры
     * @param player Текущий игрок (человек)
     * @return Ход, введенный пользователем, или null если нет допустимых ходов
     */
    @Override
    public Move makeMove(GameBoard board, GameRules rules, Player player) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("ВВЕДИТЕ ВАШ ХОД:");
        System.out.println("=".repeat(40));

        List<Move> validMoves = rules.getAllValidMoves(player, board);

        if (validMoves.isEmpty()) {
            System.out.println("Нет доступных ходов!");
            return null;
        }

        System.out.println("Ваши шашки на позициях:");
        List<Position> pieces = board.getAllPieces(player);
        for (Position pos : pieces) {
            System.out.print(pos + " ");
        }
        System.out.println();

        while (true) {
            try {
                System.out.print("\nОткуда ходить (x y): ");
                int fromX = scanner.nextInt();
                int fromY = scanner.nextInt();

                System.out.print("Куда ходить (x y): ");
                int toX = scanner.nextInt();
                int toY = scanner.nextInt();

                scanner.nextLine();

                Position from = new Position(fromX, fromY);
                Position to = new Position(toX, toY);
                Move move = new Move(from, to, player);

                if (rules.isValidMove(move, board)) {
                    System.out.println("✓ Ход принят!");
                    return move;
                } else {
                    System.out.println("✗ Некорректный ход! Попробуйте снова.");
                    System.out.println("Можно: шаг на соседнюю клетку или прыжок через шашку");
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите числа.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Возвращает имя стратегии.
     * @return Всегда возвращает "Человек"
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Определяет, что этот игрок является человеком.
     * @return Всегда возвращает true
     */
    @Override
    public boolean isHuman() {
        return true;
    }
}
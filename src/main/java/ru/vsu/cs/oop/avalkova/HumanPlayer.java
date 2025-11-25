package ru.vsu.cs.oop.avalkova;

import java.util.Scanner;

public class HumanPlayer implements PlayerStrategy {
    private final Scanner scanner;
    private final GameRules rules;

    public HumanPlayer(Scanner scanner, GameRules rules) {
        this.scanner = scanner;
        this.rules = rules;
    }

    @Override
    public Move makeMove(GameBoard board, Player player) {
        System.out.println("Ваш ход, игрок " + player.getSymbol());

        while (true) {
            try {
                System.out.print("Введите откуда ходить (x y): ");
                int fromX = scanner.nextInt();
                int fromY = scanner.nextInt();

                System.out.print("Введите куда ходить (x y): ");
                int toX = scanner.nextInt();
                int toY = scanner.nextInt();
                scanner.nextLine();

                Move move = new Move(
                        new Position(fromX, fromY),
                        new Position(toX, toY),
                        player
                );

                if (rules.isValidMove(move, board)) {
                    return move;
                } else {
                    System.out.println("Неверный ход! Попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка ввода! Введите числа.");
                scanner.nextLine();
            }
        }
    }

    @Override
    public String getName() {
        return "Человек";
    }
}
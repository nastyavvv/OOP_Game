package ru.vsu.cs.oop.avalkova;

import java.util.Scanner;

public class ChineseCheckers {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в Китайские шашки!");
        System.out.println("Режимы: 'Хочу играть!' или 'Я наблюдатель'");

        String input = scanner.nextLine().trim();

        if (input.equals("Хочу играть!")) {
            startInteractiveMode(scanner);
        } else if (input.equals("Я наблюдатель")) {
            startObserverMode();
        } else {
            System.out.println("Неизвестная команда!");
        }

        scanner.close();
    }

    private static void startInteractiveMode(Scanner scanner) {
        System.out.println("Выберите цвет (1-игрок1 ●, 2-игрок2 ○, или Enter для случайного):");
        String choice = scanner.nextLine();

        Game game = new Game();
        game.startInteractive(choice);
    }

    private static void startObserverMode() {
        System.out.println("Запуск режима наблюдателя...");
        Game game = new Game();
        game.startObserver();
    }
}
package ru.vsu.cs.oop.avalkova;

import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private GameBoard board;
    private GameRules rules;
    private Player currentPlayer;
    private Timer gameTimer;
    private boolean gameOver;

    public Game() {
        this.rules = new GameRules();
        this.board = new GameBoard(6);
        this.currentPlayer = Player.PLAYER1;
        this.gameOver = false;
    }

    public void startInteractive(String playerChoice) {
        Player humanPlayer = determineHumanPlayer(playerChoice);
        PlayerStrategy human = new HumanPlayer(new java.util.Scanner(System.in), rules); // Добавлен rules
        PlayerStrategy bot = new RandomBot(rules);

        System.out.println("Начало игры! Вы играете за " + humanPlayer.getSymbol());
        startGameTimer();

        while (!gameOver) {
            board.display();
            System.out.println("Ход игрока: " + currentPlayer.getSymbol());

            try {
                Move move;
                if (currentPlayer == humanPlayer) {
                    move = human.makeMove(board, currentPlayer);
                } else {
                    move = bot.makeMove(board, currentPlayer);
                }

                board.applyMove(move);

                if (rules.isGameOver(board, currentPlayer)) {
                    System.out.println("Игрок " + currentPlayer.getSymbol() + " победил!");
                    gameOver = true;
                }

                currentPlayer = currentPlayer.next();

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        stopGameTimer();
        board.display();
    }

    public void startObserver() {
        PlayerStrategy bot1 = new RandomBot(rules);
        PlayerStrategy bot2 = new RandomBot(rules);

        System.out.println("Бот vs Бот - наблюдаем за игрой!");
        startGameTimer();

        while (!gameOver) {
            board.display();
            System.out.println("Ход игрока: " + currentPlayer.getSymbol());

            try {
                Thread.sleep(1000);

                Move move = (currentPlayer == Player.PLAYER1) ?
                        bot1.makeMove(board, currentPlayer) :
                        bot2.makeMove(board, currentPlayer);

                board.applyMove(move);

                if (rules.isGameOver(board, currentPlayer)) {
                    System.out.println("Игрок " + currentPlayer.getSymbol() + " победил!");
                    gameOver = true;
                }

                currentPlayer = currentPlayer.next();

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        stopGameTimer();
        board.display();
    }

    private Player determineHumanPlayer(String choice) {
        if ("1".equals(choice)) return Player.PLAYER1;
        if ("2".equals(choice)) return Player.PLAYER2;
        return Math.random() > 0.5 ? Player.PLAYER1 : Player.PLAYER2;
    }

    private void startGameTimer() {
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Время вышло! Ничья.");
                gameOver = true;
            }
        }, 5 * 60 * 1000); // 5 минут
    }

    private void stopGameTimer() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }
}
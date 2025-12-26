package ru.vsu.cs.oop.avalkova;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Основной класс игры для 2 игроков, управляющий игровым процессом.
 * Координирует взаимодействие между человеком и ботом или двумя ботами.
 */
public class Game {
    private GameBoard board;
    private GameRules rules;
    private List<Player> players;
    private List<PlayerStrategy> strategies;
    private Player currentPlayer;
    private boolean gameOver;
    private int turnCount;
    private GameTimer gameTimer;
    private boolean observerMode;

    /**
     * Создает новую игру для 2 игроков.
     * @param playerCount Количество игроков (должен быть 2)
     * @param humanPlayerNumber Номер игрока-человека (1 - красный, 2 - синий, -1 - режим наблюдения)
     */
    public Game(int playerCount, int humanPlayerNumber) {
        this.gameOver = false;
        this.turnCount = 0;
        this.observerMode = (humanPlayerNumber == -1);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Старт игры на " + playerCount + " игроков");
        System.out.println("Режим: " + (observerMode ? "Наблюдение" : "Игра против бота"));

        // Инициализируем таймер на 5 минут
        this.gameTimer = new GameTimer(5);
        this.gameTimer.start(() -> {
            System.out.println("Время вышло! Игра окончена.");
            gameOver = true;
        });

        this.board = new GameBoard(16, playerCount);
        this.rules = new GameRules();
        initializePlayers(playerCount, humanPlayerNumber, scanner);

        currentPlayer = players.get(0);
        System.out.println("Первый ход: " + currentPlayer.getName());
        board.displayBoard();
    }

    /**
     * Выполняет ход, если он допустим по правилам.
     * @param move Ход для выполнения
     */
    public void makeMove(Move move) {
        if (gameOver || gameTimer.isExpired()) return;

        if (move != null && rules.isValidMove(move, board)) {
            System.out.println("Ход: " + move);
            board.applyMove(move);

            if (board.isInGoalArea(move.getTo(), move.getPlayer())) {
                move.getPlayer().pieceReachedGoal();
                System.out.println("Шашка в цели! У " + move.getPlayer().getName() +
                        ": " + move.getPlayer().getPiecesInGoal() + "/10");
            }

            turnCount++;
            board.displayBoard();

            if (rules.hasWon(currentPlayer, board)) {
                gameOver = true;
                gameTimer.stop();
                System.out.println("ПОБЕДИТЕЛЬ: " + currentPlayer.getName());
            } else {
                nextPlayer();
            }
        }
    }

    /**
     * Выполняет ход бота.
     */
    public void makeBotMove() {
        if (gameOver || gameTimer.isExpired()) return;

        List<Move> validMoves = rules.getAllValidMoves(currentPlayer, board);
        if (!validMoves.isEmpty()) {
            int playerIndex = currentPlayer.getNumber() - 1;
            PlayerStrategy strategy = strategies.get(playerIndex);
            Move move = strategy.makeMove(board, rules, currentPlayer);

            if (move != null) {
                System.out.println("Бот " + currentPlayer.getName() + " ходит: " + move);
                board.applyMove(move);

                if (board.isInGoalArea(move.getTo(), currentPlayer)) {
                    currentPlayer.pieceReachedGoal();
                    System.out.println("Шашка бота в цели! У " + currentPlayer.getName() +
                            ": " + currentPlayer.getPiecesInGoal() + "/10");
                }

                turnCount++;
                board.displayBoard();

                if (rules.hasWon(currentPlayer, board)) {
                    gameOver = true;
                    gameTimer.stop();
                    System.out.println("ПОБЕДИТЕЛЬ: " + currentPlayer.getName());
                } else {
                    nextPlayer();
                }
            }
        }
    }

    /**
     * Возвращает текущее состояние игровой доски.
     * @return Объект игровой доски
     */
    public GameBoard getBoard() {
        return board;
    }

    /**
     * Возвращает текущего игрока (чей ход).
     * @return Текущий игрок (Красный или Синий)
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Возвращает список всех игроков в игре.
     * @return Список из двух игроков: Красный и Синий
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Проверяет, завершена ли игра.
     * @return true если игра завершена или время вышло, false в противном случае
     */
    public boolean isGameOver() {
        return gameOver || gameTimer.isExpired();
    }

    /**
     * Возвращает количество сделанных ходов.
     * @return Номер текущего хода
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     * Возвращает правила игры.
     * @return Объект правил игры
     */
    public GameRules getRules() {
        return rules;
    }

    /**
     * Возвращает оставшееся время в минутах.
     * @return Количество оставшихся минут
     */
    public int getRemainingMinutes() {
        return gameTimer.getMinutes();
    }

    /**
     * Возвращает оставшееся время в секундах.
     * @return Количество оставшихся секунд в текущей минуте
     */
    public int getRemainingSeconds() {
        return gameTimer.getSeconds();
    }

    /**
     * Возвращает таймер игры.
     * @return Объект таймера
     */
    public GameTimer getGameTimer() {
        return gameTimer;
    }

    /**
     * Проверяет, является ли игра режимом наблюдения.
     * @return true если режим наблюдения, false если игра против бота
     */
    public boolean isObserverMode() {
        return observerMode;
    }

    private void initializePlayers(int playerCount, int humanPlayerNumber, Scanner scanner) {
        players = new ArrayList<>();
        strategies = new ArrayList<>();

        for (int i = 1; i <= playerCount; i++) {
            Player player = new Player(i);
            players.add(player);

            if (observerMode) {
                // В режиме наблюдения оба игрока - боты
                strategies.add(new RandomBot(i));
                System.out.println("Игрок " + i + ": Бот");
            } else if (i == humanPlayerNumber) {
                strategies.add(new HumanPlayer(scanner));
                System.out.println("Игрок " + i + ": Человек");
            } else {
                strategies.add(new RandomBot(i));
                System.out.println("Игрок " + i + ": Бот");
            }
        }
    }

    private void nextPlayer() {
        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();
        currentPlayer = players.get(nextIndex);
        System.out.println("Следующий ход: " + currentPlayer.getName());
    }
}
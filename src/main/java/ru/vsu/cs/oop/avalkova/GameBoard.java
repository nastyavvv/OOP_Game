package ru.vsu.cs.oop.avalkova;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий игровую доску для китайских шашек на 2 игроков.
 * Управляет расстановкой шашек, валидными клетками и целевыми областями.
 * Доска имеет форму ромба размером 16x16 клеток.
 * Красный игрок (1) стартует в нижней части, Синий игрок (2) в верхней.
 */
public class GameBoard {
    private final Player[][] board;
    private final int size;
    private final int playerCount;
    private boolean[][] validCells;
    private Color[][] goalColors;

    /**
     * Создает новую игровую доску для 2 игроков.
     * @param size Размер доски (должен быть 16)
     * @param playerCount Количество игроков (должен быть 2)
     */
    public GameBoard(int size, int playerCount) {
        this.size = size;
        this.playerCount = playerCount;
        this.board = new Player[size][size];
        this.validCells = new boolean[size][size];
        this.goalColors = new Color[size][size];
        initializeBoardShape();
        initializeGoalAreas();
        initializePlayers();
    }

    /**
     * Возвращает шашку на указанной позиции.
     * @param pos Позиция для проверки
     * @return Игрока, чья шашка находится на позиции, или null если клетка пуста
     */
    public Player getPieceAt(Position pos) {
        if (!isValidPosition(pos)) return null;
        return board[pos.getY()][pos.getX()];
    }

    /**
     * Устанавливает шашку на указанную позицию.
     * @param pos Позиция для установки шашки
     * @param player Игрок, чью шашку нужно установить
     */
    public void setPieceAt(Position pos, Player player) {
        if (isValidPosition(pos)) {
            board[pos.getY()][pos.getX()] = player;
        }
    }

    /**
     * Применяет ход на доске: перемещает шашку с начальной на конечную позицию.
     * @param move Ход для применения
     */
    public void applyMove(Move move) {
        if (move == null) return;
        setPieceAt(move.getFrom(), null);
        setPieceAt(move.getTo(), move.getPlayer());
    }

    /**
     * Проверяет, является ли указанная позиция пустой.
     * @param pos Позиция для проверки
     * @return true если позиция пуста, false в противном случае
     */
    public boolean isEmpty(Position pos) {
        return getPieceAt(pos) == null;
    }

    /**
     * Возвращает список всех позиций, занятых указанным игроком.
     * @param player Игрок, чьи шашки нужно найти
     * @return Список позиций с шашками игрока
     */
    public List<Position> getAllPieces(Player player) {
        List<Position> pieces = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (validCells[y][x] && board[y][x] != null && board[y][x].equals(player)) {
                    pieces.add(new Position(x, y));
                }
            }
        }
        return pieces;
    }

    /**
     * Проверяет, является ли позиция валидной (находится на доске в пределах ромба).
     * @param pos Позиция для проверки
     * @return true если позиция валидна, false в противном случае
     */
    public boolean isValidPosition(Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        if (x < 0 || x >= size || y < 0 || y >= size) return false;
        return validCells[y][x];
    }

    /**
     * Проверяет, находится ли позиция в целевой области указанного игрока.
     * @param pos Позиция для проверки
     * @param player Игрок, чью целевую область проверяем
     * @return true если позиция в целевой области игрока, false в противном случае
     */
    public boolean isInGoalArea(Position pos, Player player) {
        if (!isValidPosition(pos)) return false;

        Color goalColor = goalColors[pos.getY()][pos.getX()];
        if (goalColor == null) return false;

        int playerNum = player.getNumber();

        switch (playerNum) {
            case 1: return goalColor.getRed() > 200;
            case 2: return goalColor.getBlue() > 200;
            default: return false;
        }
    }

    /**
     * Возвращает размер доски.
     * @return Размер доски (16 клеток по каждой стороне)
     */
    public int getSize() {
        return size;
    }

    /**
     * Возвращает количество игроков.
     * @return Количество игроков в игре (2)
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * Возвращает матрицу валидных клеток.
     * @return Двумерный массив, где true означает валидную клетку внутри ромба
     */
    public boolean[][] getValidCells() {
        return validCells;
    }

    /**
     * Возвращает матрицу цветов целевых областей.
     * @return Двумерный массив цветов для каждой клетки
     */
    public Color[][] getGoalColors() {
        return goalColors;
    }

    /**
     * Выводит текстовое представление доски в консоль.
     * Отображает координаты, шашки игроков и целевые области.
     */
    public void displayBoard() {
        System.out.println("\nДоска:");
        System.out.print("   ");
        for (int x = 0; x < size; x++) {
            System.out.printf("%2d ", x);
        }
        System.out.println();

        for (int y = 0; y < size; y++) {
            System.out.printf("%2d ", y);
            for (int x = 0; x < size; x++) {
                if (validCells[y][x]) {
                    if (board[y][x] != null) {
                        System.out.print(board[y][x].getSymbol() + " ");
                    } else if (goalColors[y][x] != null) {
                        System.out.print("◌ ");
                    } else {
                        System.out.print("· ");
                    }
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }

    private void initializeBoardShape() {
        int center = size / 2;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int dx = Math.abs(x - center);
                int dy = Math.abs(y - center);

                if (dx + dy <= 8) {
                    validCells[y][x] = true;
                }
            }
        }
    }

    private void initializeGoalAreas() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                goalColors[y][x] = null;
            }
        }

        if (playerCount >= 1) {
            setGoalAreaForPlayer(1, new Color(255, 200, 200, 80));
        }
        if (playerCount >= 2) {
            setGoalAreaForPlayer(2, new Color(200, 220, 255, 80));
        }
    }

    private void setGoalAreaForPlayer(int playerNum, Color color) {
        int center = size / 2;
        List<int[]> positions = new ArrayList<>();

        if (playerNum == 1) {
            // Верхняя область для красных (цель)
            for (int i = 0; i < 4; i++) {
                for (int j = -i; j <= i; j++) {
                    positions.add(new int[]{center + j, i});
                }
            }
        } else if (playerNum == 2) {
            // Нижняя область для синих (цель)
            for (int i = 0; i < 4; i++) {
                for (int j = -i; j <= i; j++) {
                    positions.add(new int[]{center + j, size - 1 - i});
                }
            }
        }

        for (int[] pos : positions) {
            int x = pos[0];
            int y = pos[1];
            if (x >= 0 && x < size && y >= 0 && y < size && validCells[y][x]) {
                goalColors[y][x] = color;
            }
        }
    }

    private void initializePlayers() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                board[y][x] = null;
            }
        }

        for (int i = 1; i <= Math.min(playerCount, 2); i++) {
            placePlayer(i);
        }
    }

    private void placePlayer(int playerNum) {
        Player player = new Player(playerNum);
        int[][] positions = getStartPositions(playerNum);

        int placed = 0;
        for (int[] pos : positions) {
            int x = pos[0];
            int y = pos[1];
            if (placed >= 10) break;

            if (x >= 0 && x < size && y >= 0 && y < size && validCells[y][x]) {
                board[y][x] = player;
                placed++;
            }
        }

        System.out.println("Игрок " + playerNum + " разместил " + placed + " шашек");
    }

    private int[][] getStartPositions(int playerNum) {
        List<int[]> positions = new ArrayList<>();
        int center = size / 2;

        if (playerNum == 1) {
            // Красные стартуют в нижнем углу ромба
            for (int i = 0; i < 4; i++) {
                for (int j = -i; j <= i; j++) {
                    positions.add(new int[]{center + j, size - 1 - i});
                }
            }
        } else if (playerNum == 2) {
            // Синие стартуют в верхнем углу ромба
            for (int i = 0; i < 4; i++) {
                for (int j = -i; j <= i; j++) {
                    positions.add(new int[]{center + j, i});
                }
            }
        }

        int[][] result = new int[Math.min(positions.size(), 10)][2];
        for (int i = 0; i < Math.min(positions.size(), 10); i++) {
            result[i] = positions.get(i);
        }
        return result;
    }
}
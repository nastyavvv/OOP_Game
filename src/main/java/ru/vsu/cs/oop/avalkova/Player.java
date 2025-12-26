package ru.vsu.cs.oop.avalkova;

/**
 * Класс, представляющий игрока в игре для 2 игроков.
 * Хранит информацию об игроке: номер, символ, имя и количество шашек в цели.
 * Красный игрок (номер 1) и Синий игрок (номер 2).
 */
public class Player {
    private final int number;
    private final char symbol;
    private final String name;
    private int piecesInGoal;

    /**
     * Создает нового игрока с указанным номером.
     * @param number Номер игрока (1 для красного, 2 для синего)
     */
    public Player(int number) {
        this.number = number;
        this.symbol = getSymbolByNumber(number);
        this.name = getNameByNumber(number);
        this.piecesInGoal = 0;
    }

    /**
     * Возвращает номер игрока.
     * @return Номер игрока (1 или 2)
     */
    public int getNumber() {
        return number;
    }

    /**
     * Возвращает символ игрока для отображения.
     * Красный игрок: '●', Синий игрок: '○'
     * @return Символ игрока
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Возвращает имя игрока.
     * Красный игрок: "Красный", Синий игрок: "Синий"
     * @return Имя игрока
     */
    public String getName() {
        return name;
    }

    /**
     * Увеличивает счетчик шашек, достигших цели.
     */
    public void pieceReachedGoal() {
        piecesInGoal++;
    }

    /**
     * Возвращает количество шашек, достигших цели.
     * @return Количество шашек в целевой области (0-10)
     */
    public int getPiecesInGoal() {
        return piecesInGoal;
    }

    /**
     * Сбрасывает счетчик шашек в цели (для новой игры).
     */
    public void reset() {
        piecesInGoal = 0;
    }

    /**
     * Проверяет, выиграл ли игрок.
     * @return true если все 10 шашек достигли цели, false в противном случае
     */
    public boolean hasWon() {
        return piecesInGoal >= 10;
    }

    /**
     * Сравнивает этого игрока с другим объектом.
     * @param obj Объект для сравнения
     * @return true если объекты представляют одного игрока, false в противном случае
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return number == player.number;
    }

    /**
     * Возвращает хэш-код игрока.
     * @return Хэш-код, основанный на номере игрока
     */
    @Override
    public int hashCode() {
        return number;
    }

    /**
     * Возвращает строковое представление игрока.
     * @return Строка с именем, символом и количеством шашек в цели
     */
    @Override
    public String toString() {
        return name + " (" + symbol + "): " + piecesInGoal + "/10 в цели";
    }

    private char getSymbolByNumber(int number) {
        switch (number) {
            case 1: return '●';
            case 2: return '○';
            default: return '?';
        }
    }

    private String getNameByNumber(int number) {
        switch (number) {
            case 1: return "Красный";
            case 2: return "Синий";
            default: return "Игрок " + number;
        }
    }
}
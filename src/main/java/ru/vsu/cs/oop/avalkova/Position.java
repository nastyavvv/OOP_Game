package ru.vsu.cs.oop.avalkova;

/**
 * Класс для представления позиции на игровой доске.
 * Хранит координаты x и y, предоставляет методы для работы с позициями.
 */
public class Position {
    private final int x;
    private final int y;

    /**
     * Создает новую позицию с указанными координатами.
     * @param x Координата X (горизонталь)
     * @param y Координата Y (вертикаль)
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату X позиции.
     * @return Координата X
     */
    public int getX() {
        return x;
    }

    /**
     * Возвращает координату Y позиции.
     * @return Координата Y
     */
    public int getY() {
        return y;
    }

    /**
     * Проверяет, является ли позиция валидной для доски указанного размера.
     * @param boardSize Размер доски
     * @return true если позиция в пределах доски, false в противном случае
     */
    public boolean isValid(int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    /**
     * Вычисляет расстояние Чебышева до другой позиции.
     * @param other Другая позиция
     * @return Расстояние Чебышева между позициями
     */
    public int distance(Position other) {
        return Math.max(Math.abs(x - other.x), Math.abs(y - other.y));
    }

    /**
     * Проверяет, является ли позиция смежной с другой позицией.
     * @param other Другая позиция
     * @return true если позиции смежные (дистанция 1), false в противном случае
     */
    public boolean isAdjacent(Position other) {
        int dx = Math.abs(x - other.x);
        int dy = Math.abs(y - other.y);
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1) ||
                (dx == 1 && dy == 1);
    }

    /**
     * Сравнивает эту позицию с другим объектом.
     * @param obj Объект для сравнения
     * @return true если объекты представляют одинаковую позицию, false в противном случае
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    /**
     * Возвращает хэш-код позиции.
     * @return Хэш-код, основанный на координатах x и y
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    /**
     * Возвращает строковое представление позиции.
     * @return Строка в формате "(x,y)"
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
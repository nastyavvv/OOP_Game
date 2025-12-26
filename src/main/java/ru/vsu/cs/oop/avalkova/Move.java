package ru.vsu.cs.oop.avalkova;

/**
 * Класс, представляющий ход в игре.
 * Хранит информацию о начальной и конечной позиции, а также о игроке, совершающем ход.
 */
public class Move {
    private final Position from;
    private final Position to;
    private final Player player;

    /**
     * Создает новый ход.
     * @param from Начальная позиция шашки
     * @param to Конечная позиция шашки
     * @param player Игрок, совершающий ход
     */
    public Move(Position from, Position to, Player player) {
        this.from = from;
        this.to = to;
        this.player = player;
    }

    /**
     * Возвращает начальную позицию хода.
     * @return Позиция, откуда движется шашка
     */
    public Position getFrom() {
        return from;
    }

    /**
     * Возвращает конечную позицию хода.
     * @return Позиция, куда движется шашка
     */
    public Position getTo() {
        return to;
    }

    /**
     * Возвращает игрока, совершающего ход.
     * @return Игрок, которому принадлежит шашка
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Проверяет, является ли ход прыжком (через шашку).
     * @return true если расстояние между позициями больше 1, false для обычного шага
     */
    public boolean isJump() {
        return from.distance(to) > 1;
    }

    /**
     * Возвращает строковое представление хода.
     * @return Строка в формате "Имя_игрока: (x1,y1) → (x2,y2)"
     */
    @Override
    public String toString() {
        return player.getName() + ": " + from + " → " + to;
    }
}
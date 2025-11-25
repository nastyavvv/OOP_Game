package ru.vsu.cs.oop.avalkova;

public record Move(Position from, Position to, Player player) {
    public boolean isJump() {
        int dx = Math.abs(to.x() - from.x());
        int dy = Math.abs(to.y() - from.y());
        return dx == 2 || dy == 2 || (dx == 2 && dy == 2);
    }

    public boolean isValidStep() {
        int dx = Math.abs(to.x() - from.x());
        int dy = Math.abs(to.y() - from.y());
        return (dx <= 1 && dy <= 1) && !(dx == 0 && dy == 0);
    }

    @Override
    public String toString() {
        return player.getSymbol() + " " + from + " → " + to;
    }
}

package ru.vsu.cs.oop.avalkova;

public record Position(int x, int y) {
    public boolean isValid(int boardSize) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }

    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    public Position midpoint(Position other) {
        return new Position((x + other.x) / 2, (y + other.y) / 2);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

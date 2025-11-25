package ru.vsu.cs.oop.avalkova;

public enum Player {
    PLAYER1('●'),
    PLAYER2('○'),
    EMPTY('·');

    private final char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public Player next() {
        return this == PLAYER1 ? PLAYER2 : PLAYER1;
    }
}
package ru.vsu.cs.oop.avalkova;

public interface PlayerStrategy {
    Move makeMove(GameBoard board, Player player);
    String getName();
}

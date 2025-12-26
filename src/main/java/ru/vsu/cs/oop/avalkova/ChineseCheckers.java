package ru.vsu.cs.oop.avalkova;

import javax.swing.*;

/**
 * Главный класс приложения для запуска игры в китайские шашки.
 * Запускает графический интерфейс пользователя.
 */
public class ChineseCheckers {

    /**
     * Точка входа в приложение.
     * Создает и отображает графический интерфейс игры.
     * @param args Аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChineseCheckersGUI gui = new ChineseCheckersGUI();
            gui.setVisible(true);
        });
    }
}
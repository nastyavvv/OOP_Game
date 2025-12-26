package ru.vsu.cs.oop.avalkova;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс для отслеживания времени игры.
 * Предоставляет возможность установить лимит времени на игру.
 */
public class GameTimer {
    private Timer timer;
    private boolean timeExpired;
    private long startTime;
    private final int totalMinutes;

    /**
     * Создает новый таймер с указанным лимитом времени.
     * @param minutes Лимит времени в минутах
     */
    public GameTimer(int minutes) {
        this.totalMinutes = minutes;
        this.timeExpired = false;
    }

    /**
     * Запускает таймер. По истечении времени выполняется указанное действие.
     * @param onTimeout Действие, выполняемое при истечении времени
     */
    public void start(Runnable onTimeout) {
        startTime = System.currentTimeMillis();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeExpired = true;
                if (onTimeout != null) {
                    onTimeout.run();
                }
            }
        }, totalMinutes * 60 * 1000L);
    }

    /**
     * Останавливает таймер.
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * Проверяет, истекло ли время.
     * @return true если время истекло, false в противном случае
     */
    public boolean isExpired() {
        return timeExpired;
    }

    /**
     * Возвращает количество оставшихся минут.
     * @return Количество оставшихся минут до истечения времени
     */
    public int getMinutes() {
        if (timeExpired) return 0;

        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = totalMinutes * 60 * 1000L - elapsed;
        return (int) (remaining / (60 * 1000));
    }

    /**
     * Возвращает количество оставшихся секунд.
     * @return Количество оставшихся секунд в текущей минуте
     */
    public int getSeconds() {
        if (timeExpired) return 0;

        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = totalMinutes * 60 * 1000L - elapsed;
        return (int) ((remaining / 1000) % 60);
    }
}
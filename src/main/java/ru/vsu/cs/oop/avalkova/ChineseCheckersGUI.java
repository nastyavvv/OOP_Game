package ru.vsu.cs.oop.avalkova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Графический интерфейс для игры в китайские шашки.
 * Предоставляет два режима: игра против бота и режим наблюдения (оба бота).
 */
public class ChineseCheckersGUI extends JFrame {
    private Game game;
    private GameBoard board;
    private JPanel gamePanel;
    private JLabel statusLabel;
    private JLabel turnLabel;
    private JLabel timerLabel;
    private Position selectedPosition;
    private int humanPlayerNumber;
    private boolean observerMode;
    private boolean gameOver;
    private static final int CELL_SIZE = 40;
    private static final Color[] PLAYER_COLORS = {
            new Color(220, 60, 60),
            new Color(60, 130, 220)
    };
    private Timer uiTimer;
    private Timer gameLoopTimer;
    private JScrollPane scrollPane;
    private int offsetX = 0;
    private int offsetY = 0;
    private Point dragStart;

    /**
     * Создает и отображает главное окно игры.
     * Предоставляет выбор режима игры.
     */
    public ChineseCheckersGUI() {
        setTitle("Китайские Шашки");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        showMainMenu();
    }

    /**
     * Точка входа для графического интерфейса игры.
     * @param args Аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChineseCheckersGUI gui = new ChineseCheckersGUI();
            gui.setVisible(true);
        });
    }

    private void showMainMenu() {
        getContentPane().removeAll();

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        panel.setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("КИТАЙСКИЕ ШАШКИ", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(new Color(30, 80, 160));
        panel.add(title);

        JButton vsBotBtn = new JButton("ИГРАТЬ ПРОТИВ БОТА");
        vsBotBtn.setFont(new Font("Arial", Font.BOLD, 20));
        vsBotBtn.addActionListener(e -> startVsBotGame());

        JButton observerBtn = new JButton("РЕЖИМ НАБЛЮДЕНИЯ");
        observerBtn.setFont(new Font("Arial", Font.BOLD, 20));
        observerBtn.addActionListener(e -> startObserverGame());

        JButton exitBtn = new JButton("ВЫХОД");
        exitBtn.setFont(new Font("Arial", Font.BOLD, 20));
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(vsBotBtn);
        panel.add(observerBtn);
        panel.add(exitBtn);

        setContentPane(panel);
        revalidate();
        repaint();
    }

    private void startVsBotGame() {
        humanPlayerNumber = 1;
        observerMode = false;
        initGameInterface();
        startGame();
    }

    private void startObserverGame() {
        humanPlayerNumber = -1;
        observerMode = true;
        initGameInterface();
        startGame();
    }

    private void initGameInterface() {
        getContentPane().removeAll();

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(240, 248, 255));

        JButton menuBtn = new JButton("МЕНЮ");
        menuBtn.addActionListener(e -> showMainMenu());

        JButton resetViewBtn = new JButton("СБРОСИТЬ ВИД");
        resetViewBtn.addActionListener(e -> {
            offsetX = 0;
            offsetY = 0;
            gamePanel.repaint();
        });

        statusLabel = new JLabel(observerMode ? "Режим наблюдения" : "Ваш ход");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        turnLabel = new JLabel("Ход: Красный");
        turnLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        timerLabel = new JLabel("Время: 05:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timerLabel.setForeground(Color.RED);

        topPanel.add(menuBtn);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(resetViewBtn);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(statusLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(turnLabel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(timerLabel);

        add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }

            @Override
            public Dimension getPreferredSize() {
                // Делаем панель больше, чем видимая область, для прокрутки
                return new Dimension(1200, 1200);
            }
        };

        gamePanel.setBackground(new Color(240, 248, 255));

        // Добавляем обработчики мыши для перемещения
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    offsetX += dx;
                    offsetY += dy;
                    dragStart = e.getPoint();
                    gamePanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    handleClick(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Прокрутка колесика для зума (опционально)
                int notches = e.getWheelRotation();
                // Можно добавить зум здесь если нужно
                gamePanel.repaint();
            }
        };

        gamePanel.addMouseListener(mouseAdapter);
        gamePanel.addMouseMotionListener(mouseAdapter);
        gamePanel.addMouseWheelListener(mouseAdapter);

        // Создаем JScrollPane для прокрутки
        scrollPane = new JScrollPane(gamePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(new Color(240, 248, 255));

        // Устанавливаем начальную позицию прокрутки в центр
        SwingUtilities.invokeLater(() -> {
            JViewport viewport = scrollPane.getViewport();
            Dimension viewSize = viewport.getSize();
            Dimension panelSize = gamePanel.getPreferredSize();
            viewport.setViewPosition(new Point(
                    (panelSize.width - viewSize.width) / 2,
                    (panelSize.height - viewSize.height) / 2
            ));
        });

        add(scrollPane, BorderLayout.CENTER);

        // Добавляем панель с инструкциями
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 248, 255));
        JLabel instructionLabel = new JLabel(
                "Инструкция: Зажмите и перетаскивайте для перемещения по доске | ЛКМ: выбор шашки и ход"
        );
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(instructionLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void startGame() {
        gameOver = false;
        offsetX = 0;
        offsetY = 0;

        game = new Game(2, humanPlayerNumber);
        board = game.getBoard();
        selectedPosition = null;

        if (observerMode) {
            statusLabel.setText("Режим наблюдения");
            turnLabel.setText("Ход: Красный");
            startObserverLoop();
        } else {
            statusLabel.setText("Ваш ход (Красный)");
            turnLabel.setText("Ход: Красный");
        }

        timerLabel.setText("Время: 05:00");
        startUITimer();

        gamePanel.repaint();
    }

    private void startUITimer() {
        if (uiTimer != null) {
            uiTimer.cancel();
        }

        uiTimer = new Timer();
        uiTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (game != null && !gameOver) {
                        // Обновляем таймер
                        int minutes = game.getRemainingMinutes();
                        int seconds = game.getRemainingSeconds();
                        timerLabel.setText(String.format("Время: %02d:%02d", minutes, seconds));

                        // Обновляем текущего игрока
                        turnLabel.setText("Ход: " + game.getCurrentPlayer().getName());

                        // Проверяем, не закончилось ли время
                        if (game.getGameTimer().isExpired()) {
                            gameOver = true;
                            JOptionPane.showMessageDialog(ChineseCheckersGUI.this,
                                    "Время вышло! Игра окончена.");
                            statusLabel.setText("Время вышло!");
                        }
                    }
                });
            }
        }, 0, 500);
    }

    private void startObserverLoop() {
        if (gameLoopTimer != null) {
            gameLoopTimer.cancel();
        }

        gameLoopTimer = new Timer();
        gameLoopTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (game != null && !game.isGameOver()) {
                        game.makeBotMove();
                        gamePanel.repaint();

                        if (game.isGameOver()) {
                            gameOver = true;
                            Player winner = game.getCurrentPlayer();
                            JOptionPane.showMessageDialog(ChineseCheckersGUI.this,
                                    "Победитель: " + winner.getName());
                            statusLabel.setText("Игра окончена!");
                            gameLoopTimer.cancel();
                        }
                    }
                });
            }
        }, 1000, 2000); // Задержка 1 секунда, интервал 2 секунды
    }

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (board == null) return;

        int centerX = gamePanel.getWidth() / 2 + offsetX;
        int centerY = gamePanel.getHeight() / 2 + offsetY;
        int boardSize = 16;

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                Position pos = new Position(x, y);
                if (board.isValidPosition(pos)) {
                    int drawX = centerX + (x - 8) * CELL_SIZE;
                    int drawY = centerY + (y - 8) * CELL_SIZE;

                    // Рисуем клетку
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(drawX - 18, drawY - 18, 36, 36);
                    g2d.setColor(Color.GRAY);
                    g2d.drawOval(drawX - 18, drawY - 18, 36, 36);

                    // Рисуем шашку
                    Player piece = board.getPieceAt(pos);
                    if (piece != null) {
                        int playerNum = piece.getNumber();
                        if (playerNum >= 1 && playerNum <= 2) {
                            g2d.setColor(PLAYER_COLORS[playerNum - 1]);
                            g2d.fillOval(drawX - 15, drawY - 15, 30, 30);
                        }
                    }

                    // Выделение выбранной шашки
                    if (selectedPosition != null &&
                            selectedPosition.getX() == x &&
                            selectedPosition.getY() == y) {
                        g2d.setColor(Color.YELLOW);
                        g2d.drawOval(drawX - 20, drawY - 20, 40, 40);
                    }

                    // Отображаем координаты (опционально, для отладки)
                    if (CELL_SIZE > 35) {
                        g2d.setColor(Color.BLACK);
                        g2d.setFont(new Font("Arial", Font.PLAIN, 8));
                        String coord = x + "," + y;
                        FontMetrics fm = g2d.getFontMetrics();
                        int textWidth = fm.stringWidth(coord);
                        g2d.drawString(coord, drawX - textWidth/2, drawY + 3);
                    }
                }
            }
        }

        // Рисуем сетку координат (опционально)
        g2d.setColor(new Color(200, 200, 200, 100));
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i < boardSize; i++) {
            int xPos = centerX + (i - 8) * CELL_SIZE;
            int yPos = centerY + (i - 8) * CELL_SIZE;

            // Вертикальные линии
            g2d.drawLine(xPos, centerY - 8 * CELL_SIZE, xPos, centerY + 8 * CELL_SIZE);
            // Горизонтальные линии
            g2d.drawLine(centerX - 8 * CELL_SIZE, yPos, centerX + 8 * CELL_SIZE, yPos);

            // Подписи координат
            if (i % 2 == 0) {
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawString(String.valueOf(i), xPos - 5, centerY - 8 * CELL_SIZE - 5);
                g2d.drawString(String.valueOf(i), centerX - 8 * CELL_SIZE - 15, yPos + 3);
                g2d.setColor(new Color(200, 200, 200, 100));
            }
        }
    }

    private void handleClick(int x, int y) {
        if (observerMode || game == null || gameOver || game.getGameTimer().isExpired()) return;

        int centerX = gamePanel.getWidth() / 2 + offsetX;
        int centerY = gamePanel.getHeight() / 2 + offsetY;

        int boardX = Math.round((float)(x - centerX) / CELL_SIZE + 8);
        int boardY = Math.round((float)(y - centerY) / CELL_SIZE + 8);

        Position clicked = new Position(boardX, boardY);

        if (!board.isValidPosition(clicked)) {
            return;
        }

        if (selectedPosition == null) {
            Player piece = board.getPieceAt(clicked);
            if (piece != null && piece.getNumber() == humanPlayerNumber) {
                selectedPosition = clicked;
                statusLabel.setText("Выбрана шашка. Куда ходить?");
                gamePanel.repaint();
            }
        } else {
            Player currentPlayer = game.getCurrentPlayer();
            Move move = new Move(selectedPosition, clicked, currentPlayer);

            if (game.getRules().isValidMove(move, board)) {
                game.makeMove(move);
                selectedPosition = null;

                if (game.isGameOver()) {
                    gameOver = true;
                    JOptionPane.showMessageDialog(this,
                            "Победитель: " + currentPlayer.getName());
                    statusLabel.setText("Игра окончена!");
                    if (uiTimer != null) uiTimer.cancel();
                } else {
                    if (currentPlayer.getNumber() == 1) {
                        statusLabel.setText("Ход Синего (бот)");
                        makeBotMoveWithDelay();
                    } else {
                        statusLabel.setText("Ваш ход (Красный)");
                    }
                }

                gamePanel.repaint();
            } else {
                selectedPosition = null;
                statusLabel.setText("Невозможный ход");
                gamePanel.repaint();
            }
        }
    }

    private void makeBotMoveWithDelay() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    if (game != null && !game.isGameOver() && !game.getGameTimer().isExpired()) {
                        game.makeBotMove();

                        if (game.isGameOver()) {
                            gameOver = true;
                            Player winner = game.getCurrentPlayer();
                            JOptionPane.showMessageDialog(ChineseCheckersGUI.this,
                                    "Победитель: " + winner.getName());
                            statusLabel.setText("Игра окончена!");
                            if (uiTimer != null) uiTimer.cancel();
                        } else {
                            statusLabel.setText("Ваш ход (Красный)");
                            gamePanel.repaint();
                        }
                    }
                });
            }
        }, 1000);
    }
}
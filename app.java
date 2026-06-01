import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class app extends JPanel implements settings {

    private engine core;

    private static final Color BACKGROUND_LAYER = new Color(24, 24, 30);

    public app() {
        setPreferredSize(new Dimension(settings.WIDTH, settings.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        core = new engine(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();

                if (mx >= CLOSE_BTN_X && mx <= CLOSE_BTN_X + CLOSE_BTN_W &&
                        my >= CLOSE_BTN_Y && my <= CLOSE_BTN_Y + CLOSE_BTN_H) {
                    core.stopProcess();
                    System.exit(0);
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                    core.setMoveLeft(true);
                    core.setMoveRight(false);
                } else if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                    core.setMoveRight(true);
                    core.setMoveLeft(false);
                } else if (key == KEY_ACTION) {
                    core.launchSphere();
                } else if (key == KEY_RESTART) {
                    core.tryRestart();
                } else if (key == KEY_CLOSE) {
                    core.stopProcess();
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
                    core.setMoveLeft(false);
                }
                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
                    core.setMoveRight(false);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(BACKGROUND_LAYER);
        g2.fillRect(0, 0, settings.WIDTH, settings.HEIGHT);

        synchronized (core) {
            for (int i = 0; i < core.getBlocks().size(); i++) {
                Rectangle block = (Rectangle) core.getBlocks().get(i);
                if (block.y < 40) continue;

                int rowIndex = (int) core.getBlockRows().get(i);
                Color baseColor = ROW_COLORS[rowIndex % ROW_COLORS.length];

                g2.setColor(baseColor);
                g2.fillRect(block.x, block.y, block.width, block.height);

                g2.setColor(BACKGROUND_LAYER);
                g2.fillRect(block.x, block.y, 4, 4);
                g2.fillRect(block.x + block.width - 4, block.y, 4, 4);
                g2.fillRect(block.x, block.y + block.height - 4, 4, 4);
                g2.fillRect(block.x + block.width - 4, block.y + block.height - 4, 4, 4);

                boolean isBonus = (boolean) core.getHasBonus().get(i);
                if (isBonus) {
                    int bx = block.x + (block.width - BALL_SIZE) / 2;
                    int by = block.y + (block.height - BALL_SIZE) / 2;
                    drawFatPixelBall(g2, bx, by, baseColor);
                }
            }

            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, settings.WIDTH, 45);

            g2.setColor(new Color(90, 90, 95));
            g2.fillRect(CLOSE_BTN_X, CLOSE_BTN_Y, CLOSE_BTN_W, CLOSE_BTN_H);
            g2.setColor(new Color(130, 130, 135));
            g2.drawRect(CLOSE_BTN_X + 1, CLOSE_BTN_Y + 1, CLOSE_BTN_W - 3, CLOSE_BTN_H - 3);

            g2.setColor(Color.WHITE);
            for (int offset = 5; offset <= 15; offset++) {
                g2.fillRect(CLOSE_BTN_X + offset, CLOSE_BTN_Y + offset, 1, 1);
            }
            for (int offset = 5; offset <= 15; offset++) {
                g2.fillRect(CLOSE_BTN_X + 20 - offset, CLOSE_BTN_Y + offset, 1, 1);
            }

            for (int i = 0; i < core.getActiveBalls().size(); i++) {
                engine.Ball b = (engine.Ball) core.getActiveBalls().get(i);
                drawFatPixelBall(g2, (int) b.x, (int) b.y, b.ballColor);
            }

            g2.setColor(PADDLE_COLOR);
            g2.fillRect(core.getBoardX() + 4, PADDLE_Y_POS, PADDLE_W - 8, PADDLE_H);
            g2.fillRect(core.getBoardX(), PADDLE_Y_POS + 4, PADDLE_W, PADDLE_H - 8);

            for (int i = 0; i < core.getLives(); i++) {
                drawFatPixelBall(g2, 20 + (i * 24), 14, Color.WHITE);
            }

            g2.setFont(INTERFACE_FONT);
            String formattedScore = String.format("%05d", core.getTotalScore());
            int scoreX = (settings.WIDTH - g2.getFontMetrics().stringWidth(formattedScore)) / 2;
            g2.drawString(formattedScore, scoreX, 30);

            renderAlerts(g2);
        }
    }

    private void drawFatPixelBall(Graphics2D g2, int x, int y, Color ballColor) {
        g2.setColor(ballColor);
        g2.fillRect(x + 4, y, 8, 16);
        g2.fillRect(x, y + 4, 16, 8);

        g2.setColor(ballColor.darker().darker());
        g2.fillRect(x + 4, y, 8, 1);
        g2.fillRect(x + 4, y + 15, 8, 1);
        g2.fillRect(x, y + 4, 1, 8);
        g2.fillRect(x + 15, y + 4, 1, 8);

        g2.fillRect(x + 1, y + 3, 3, 1);
        g2.fillRect(x + 12, y + 3, 3, 1);
        g2.fillRect(x + 1, y + 12, 3, 1);
        g2.fillRect(x + 12, y + 12, 3, 1);

        g2.fillRect(x + 3, y + 1, 1, 2);
        g2.fillRect(x + 12, y + 1, 1, 2);
        g2.fillRect(x + 3, y + 13, 1, 2);
        g2.fillRect(x + 12, y + 13, 1, 2);
    }

    private void renderAlerts(Graphics2D g2) {
        String alertText = "";

        switch (core.getState()) {
            case MENU:
                alertText = "START: SPACE";
                break;
            case READY:
                alertText = "LAUNCH: SPACE";
                break;
            case OVER:
                alertText = "GAME OVER! PRESS 'R'";
                break;
            default:
                return;
        }

        g2.setColor(Color.WHITE);
        int textX = (settings.WIDTH - g2.getFontMetrics().stringWidth(alertText)) / 2;
        g2.drawString(alertText, textX, 360);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            app panel = new app();

            frame.setContentPane(panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            panel.core.startProcess();
        });
    }
}
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class engine implements Runnable, settings {

    public enum State { MENU, PLAYING, READY, OVER }

    public static class Ball {
        public double x, y, dx, dy;
        public boolean isMain;
        public Color ballColor;

        public Ball(double x, double y, double dx, double dy, boolean isMain, Color ballColor) {
            this.x = x; this.y = y; this.dx = dx; this.dy = dy; this.isMain = isMain;
            this.ballColor = ballColor;
        }
    }

    private State state;
    private int totalScore;
    private int lives;
    private boolean active;
    private Thread loopThread;

    private int boardX;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private int currentBlockYOffset = -200;
    private boolean isDroppingNewWave = false;
    private int lowestBrickY = 0;

    private int lastSpeedMilestone = 0;

    private final List<Ball> activeBalls = new ArrayList<Ball>();
    private final List<Rectangle> blocks = new ArrayList<Rectangle>();
    private final List<Integer> blockRows = new ArrayList<Integer>();
    private final List<Boolean> hasBonus = new ArrayList<Boolean>();

    private final app view;
    private final Random rand = new Random();

    public engine(app view) {
        this.view = view;
        initEngine();
    }

    private synchronized void initEngine() {
        totalScore = 0;
        lives = 3;
        lastSpeedMilestone = 0;
        boardX = (WIDTH - PADDLE_W) / 2;
        state = State.MENU;
        activeBalls.clear();
        moveLeft = false;
        moveRight = false;

        spawnNewWave();
        replaceSphere();
    }

    public synchronized void startProcess() {
        if (active) return;
        active = true;
        loopThread = new Thread(this, "CalculationThread");
        loopThread.start();
    }

    public synchronized void stopProcess() {
        active = false;
    }

    public synchronized void tryRestart() {
        if (state == State.OVER) {
            initEngine();
        }
    }

    public synchronized void replaceSphere() {
        activeBalls.clear();
        double startX = boardX + (PADDLE_W - BALL_SIZE) / 2.0;
        double startY = PADDLE_Y_POS - BALL_SIZE - 1;

        double currentBonusScale = 1.0 + (lastSpeedMilestone * SPEED_MULTIPLIER);
        activeBalls.add(new Ball(startX, startY, BALL_SPEED_X * currentBonusScale, BALL_SPEED_Y * currentBonusScale, true, BALL_COLOR));

        if (state == State.PLAYING) {
            state = State.READY;
        }
    }

    public synchronized void launchSphere() {
        if (state == State.MENU || state == State.READY) {
            state = State.PLAYING;
        }
    }

    public synchronized void setMoveLeft(boolean move) { this.moveLeft = move; }
    public synchronized void setMoveRight(boolean move) { this.moveRight = move; }

    private void spawnNewWave() {
        blocks.clear();
        blockRows.clear();
        hasBonus.clear();
        currentBlockYOffset = -180;
        isDroppingNewWave = true;
        lowestBrickY = START_Y + currentBlockYOffset + (ROWS * (BRICK_H + GAP)) - GAP;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = START_X + c * (BRICK_W + GAP);
                int y = currentBlockYOffset + r * (BRICK_H + GAP);
                blocks.add(new Rectangle(x, y, BRICK_W, BRICK_H));
                blockRows.add(r);
                hasBonus.add(rand.nextFloat() < 0.25f);
            }
        }
    }

    @Override
    public void run() {
        while (active) {
            long timeStart = System.currentTimeMillis();

            synchronized(this) {
                if (state == State.PLAYING || state == State.READY || state == State.MENU) {
                    if (moveLeft) boardX -= PADDLE_SPEED;
                    if (moveRight) boardX += PADDLE_SPEED;

                    if (boardX < 0) boardX = 0;
                    if (boardX > WIDTH - PADDLE_W) boardX = WIDTH - PADDLE_W;
                }

                if (isDroppingNewWave) {
                    currentBlockYOffset += 3;
                    int index = 0;
                    for (int r = 0; r < ROWS; r++) {
                        for (int c = 0; c < COLS; c++) {
                            if (index < blocks.size()) {
                                Rectangle b = blocks.get(index);
                                b.y = START_Y + currentBlockYOffset + r * (BRICK_H + GAP);
                                index++;
                            }
                        }
                    }
                    lowestBrickY = START_Y + currentBlockYOffset + (ROWS * (BRICK_H + GAP)) - GAP;

                    if (currentBlockYOffset >= 0) {
                        currentBlockYOffset = 0;
                        isDroppingNewWave = false;
                    }
                }

                if (state == State.PLAYING) {
                    calcPhysics();
                } else if (state == State.READY || state == State.MENU) {
                    if (!activeBalls.isEmpty()) {
                        activeBalls.get(0).x = boardX + (PADDLE_W - BALL_SIZE) / 2.0;
                        activeBalls.get(0).y = PADDLE_Y_POS - BALL_SIZE - 1;
                    }
                }
            }

            view.repaint();

            long timePassed = System.currentTimeMillis() - timeStart;
            long timeSleep = DELAY - timePassed;

            if (timeSleep > 0) {
                try {
                    Thread.sleep(timeSleep);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private void calcPhysics() {
        Rectangle boardBounds = new Rectangle(boardX, PADDLE_Y_POS, PADDLE_W, PADDLE_H);

        for (int i = activeBalls.size() - 1; i >= 0; i--) {
            Ball b = activeBalls.get(i);
            b.x += b.dx;
            b.y += b.dy;

            if (b.x <= 0) { b.x = 0; b.dx = -b.dx; }
            if (b.x >= WIDTH - BALL_SIZE) { b.x = WIDTH - BALL_SIZE; b.dx = -b.dx; }

            if (b.y <= 45) {
                b.y = 45;
                b.dy = -b.dy;
            }

            if (isDroppingNewWave && b.y <= lowestBrickY && b.dy < 0) {
                b.y = lowestBrickY;
                b.dy = -b.dy;
            }

            if (b.y >= HEIGHT) {
                if (b.isMain) {
                    boolean bonusExists = false;
                    for (Ball other : activeBalls) {
                        if (!other.isMain) { bonusExists = true; break; }
                    }

                    if (bonusExists) {
                        activeBalls.remove(i);
                    } else {
                        lives--;
                        if (lives > 0) {
                            state = State.READY;
                            replaceSphere();
                        } else {
                            activeBalls.clear();
                            state = State.OVER;
                            return;
                        }
                    }
                } else {
                    activeBalls.remove(i);
                    if (activeBalls.isEmpty()) {
                        lives--;
                        if (lives > 0) {
                            state = State.READY;
                            replaceSphere();
                        } else {
                            state = State.OVER;
                            return;
                        }
                    }
                }
                continue;
            }

            Rectangle ballBounds = new Rectangle((int) b.x, (int) b.y, BALL_SIZE, BALL_SIZE);

            if (ballBounds.intersects(boardBounds) && b.dy > 0) {
                double boardCenter = boardX + (PADDLE_W / 2.0);
                double ballCenter = b.x + (BALL_SIZE / 2.0);
                double hitFactor = (ballCenter - boardCenter) / (PADDLE_W / 2.0);

                double currentBonusScale = 1.0 + (lastSpeedMilestone * SPEED_MULTIPLIER);
                b.dx = hitFactor * 5.0 * currentBonusScale;
                b.dy = -b.dy;
                b.y = PADDLE_Y_POS - BALL_SIZE;
            }

            if (!isDroppingNewWave) {
                for (int j = 0; j < blocks.size(); j++) {
                    Rectangle currentBlock = blocks.get(j);
                    if (ballBounds.intersects(currentBlock)) {
                        bounceBall(b, currentBlock);

                        if (hasBonus.get(j)) {
                            int rowIndex = blockRows.get(j);
                            Color parentColor = ROW_COLORS[rowIndex % ROW_COLORS.length];

                            double currentBonusScale = 1.0 + (lastSpeedMilestone * SPEED_MULTIPLIER);
                            activeBalls.add(new Ball(
                                    currentBlock.x + (BRICK_W - BALL_SIZE) / 2.0,
                                    currentBlock.y + BRICK_H,
                                    (rand.nextBoolean() ? BALL_SPEED_X : -BALL_SPEED_X) * currentBonusScale,
                                    Math.abs(BALL_SPEED_Y) * currentBonusScale,
                                    false,
                                    parentColor
                            ));
                        }

                        blocks.remove(j);
                        blockRows.remove(j);
                        hasBonus.remove(j);
                        totalScore += POINTS;
                        int currentMilestone = totalScore / SCORE_SPEED_STEP;
                        if (currentMilestone > lastSpeedMilestone) {
                            int steps = currentMilestone - lastSpeedMilestone;
                            lastSpeedMilestone = currentMilestone;

                            for (Ball currentBall : activeBalls) {
                                currentBall.dx *= (1.0 + (SPEED_MULTIPLIER * steps));
                                currentBall.dy *= (1.0 + (SPEED_MULTIPLIER * steps));
                            }
                        }

                        if (blocks.isEmpty()) {
                            spawnNewWave();
                        }
                        break;
                    }
                }
            }
        }
    }

    private void bounceBall(Ball b, Rectangle currentBlock) {
        double left = b.x;
        double right = b.x + BALL_SIZE;

        if (right - b.dx <= currentBlock.x || left - b.dx >= currentBlock.x + currentBlock.width) {
            b.dx = -b.dx;
        } else {
            b.dy = -b.dy;
        }
    }

    public synchronized State getState() { return state; }
    public synchronized int getTotalScore() { return totalScore; }
    public synchronized int getLives() { return lives; }
    public synchronized int getBoardX() { return boardX; }
    public synchronized List getActiveBalls() { return activeBalls; }
    public synchronized List getBlocks() { return blocks; }
    public synchronized List getBlockRows() { return blockRows; }
    public synchronized List getHasBonus() { return hasBonus; }
}
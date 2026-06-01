import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

public interface settings {

    int WIDTH  = 640;
    int HEIGHT = 640;
    int SCALE  = 1;

    String TITLE = "arka";

    int FPS = 60;
    int DELAY = 1000 / FPS;

    Color BG_COLOR = new Color(24, 24, 30);

    Color PADDLE_COLOR = Color.WHITE;
    Color BALL_COLOR = Color.WHITE;

    Color TEXT_COLOR = Color.WHITE;

    Color[] ROW_COLORS = {
            new Color(196, 42, 42),
            new Color(214, 105, 30),
            new Color(214, 180, 42),
            new Color(42, 160, 80)
    };

    Font INTERFACE_FONT = new Font("Monospaced", Font.BOLD, 24);

    int PADDLE_W = 112;
    int PADDLE_H = 20;
    int PADDLE_Y_POS = 560;
    int PADDLE_SPEED = 11;

    int BALL_SIZE = 16;

    double BALL_SPEED_X = 5.0;
    double BALL_SPEED_Y = -7.4;

    int SCORE_SPEED_STEP = 1000;
    double SPEED_MULTIPLIER = 0.10;

    int CLOSE_BTN_X = 604;
    int CLOSE_BTN_Y = 12;
    int CLOSE_BTN_W = 20;
    int CLOSE_BTN_H = 20;

    int ROWS = 4;
    int COLS = 6;
    int BRICK_W = 80;
    int BRICK_H = 32;
    int START_X = 60;
    int START_Y = 112;
    int GAP = 8;
    int POINTS = 50;

    int KEY_RESTART = KeyEvent.VK_R;
    int KEY_ACTION  = KeyEvent.VK_SPACE;
    int KEY_CLOSE   = KeyEvent.VK_ESCAPE;
}

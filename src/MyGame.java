import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.*;

public class MyGame extends JPanel {
    private final ArrayList<Point> points = new ArrayList<>();
    private final ArrayList<Area> borders = new ArrayList<>();

    private boolean width = false;
    private final int WIDTH = 500;
    private final int HEIGHT = 1000;
    private boolean next = false;
    private final int THICKNESS = 70;

    //Player values
    private final Point playerPos;

    private int playerDirectionX = 0;
    private int playerDirectionY = 0;

    private final int PLAYER_SIZE = 20;

    Thread playerMoveThread;

    public MyGame() {
        setBounds(0,0,500,1000);
        setLayout(null);
        setFocusable(true);
        Point startPoint = new Point(WIDTH / 2, HEIGHT);
        points.add(startPoint);

        playerPos = new Point(WIDTH / 2 - PLAYER_SIZE / 2, HEIGHT - PLAYER_SIZE - 5);

        dungeonGen(startPoint);

        playerMoveThread = new Thread(this::move);
        playerMoveThread.start();
    }

    public void move() {
        while (playerMoveThread.isAlive()) {
            int PLAYER_SPEED = 3;
            playerPos.x += playerDirectionX * PLAYER_SPEED;
            playerPos.y += playerDirectionY * PLAYER_SPEED;

            if (playerPos.y >= HEIGHT - PLAYER_SIZE) playerPos.y = HEIGHT - PLAYER_SIZE;
            if (playerPos.y <= - PLAYER_SIZE) {
                playerPos.x = WIDTH / 2 - PLAYER_SIZE / 2;
                playerPos.y = HEIGHT - PLAYER_SIZE;
                next = true;
                playerMoveThread.stop();
            }

            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> playerDirectionY = -1;
            case KeyEvent.VK_RIGHT -> playerDirectionX = 1;
            case KeyEvent.VK_DOWN -> playerDirectionY = 1;
            case KeyEvent.VK_LEFT -> playerDirectionX = -1;
        }
    }

    public void handleKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_DOWN -> playerDirectionY = 0;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT -> playerDirectionX = 0;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (int i = 0; i < points.size() - 1; i++) {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setColor(Color.YELLOW);
            g2D.setStroke(new BasicStroke(THICKNESS));
            g2D.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
        }
        g.setColor(Color.RED);
        g.fillRect(playerPos.x, playerPos.y, PLAYER_SIZE, PLAYER_SIZE);
    }

    public void dungeonGen(Point startPoint) {
        int lastX = startPoint.x;
        int lastY = startPoint.y;

        while (true) {
            if (width) {
                width = false;
                int length = (int) ((Math.random() * 300) + 100) - (int) ((Math.random() * 300) + 100);
                Point point = new Point(lastX + length, lastY);


                if (point.x <= 75 || point.x >= 425) width = true;
                else {
                    lastX = point.x;
                    points.add(point);
                }
            } else {
                width = true;
                Point point = new Point(lastX, lastY - ((int) (Math.random() * 130) + 80));
                lastY = point.y;
                points.add(point);
                if (point.y < 0) {
                    return;
                }
            }
        }
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }
}

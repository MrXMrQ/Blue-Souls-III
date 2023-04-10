import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends JPanel {
    //Dungeon values
    private final ArrayList<Point> points;
    private boolean width = false;
    private boolean paint = false;
    private int WIDTH = 250;
    private int HEIGHT = 500;

    public Game() {
        setBounds(0, 0, 500, 500);

        points = new ArrayList<>();
        Point startPoint = new Point(WIDTH, HEIGHT);
        points.add(startPoint);

        setFocusable(true);
        dungeonGen(startPoint);
    }

    public void paint(Graphics g) {
        if (paint) {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, 500, 500);

            for (int i = 0; i < points.size() - 1; i++) {
                Graphics2D g2D = (Graphics2D) g;
                g2D.setColor(Color.YELLOW);
                g2D.setStroke(new BasicStroke(50));
                g2D.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
            }
        }
    }

    public void dungeonGen(Point startPoint) {
        int lastX = startPoint.x;
        int lastY = startPoint.y;

        while (true) {
            if (width) {
                width = false;
                int length = (int) ((Math.random() * 300) + 100) - (int) ((Math.random() * 300) + 100);
                Point point = new Point(lastX + length, lastY);

                if (point.x <= 50 || point.x >= 450) width = true;
                else {
                    lastX = point.x;
                    points.add(point);
                }
            } else {
                width = true;
                Point point = new Point(lastX, lastY - ((int) (Math.random() * 75) + 50));
                lastY = point.y;
                points.add(point);
                if (point.y < 0) {
                    System.out.println(points.size());
                    paint = true;
                    return;
                }
            }
        }
    }
}
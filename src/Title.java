import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Title {
    public MyFrame myFrame;
    public JLayeredPane layeredPane;
    public MyGame myGame;
    public Thread nextDungeonThread;

    public Title() {
        myFrame = new MyFrame();
        myFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (myGame != null) {
                    myGame.handleKeyPressed(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (myGame != null) {
                    myGame.handleKeyReleased(e);
                }
            }
        });
        layeredPane = myFrame.getLayeredPane();

        addGame();

        nextDungeonThread = new Thread(this::nextDungeon);
        nextDungeonThread.start();
    }

    public void addGame() {
        layeredPane.removeAll();
        myGame = new MyGame();
        layeredPane.add(myGame, Integer.valueOf(1));
        layeredPane.repaint();
    }

    public void nextDungeon() {
        while (nextDungeonThread.isAlive()) {
            if (myGame != null && myGame.isNext()) {
                myGame.setNext(false);
                addGame();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

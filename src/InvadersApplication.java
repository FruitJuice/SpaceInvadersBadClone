import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.image.*;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

    private static final Dimension WindowSize = new Dimension(800,600);
    private static final int NUMALIENS = 30;
    private Alien[] AliensArray = new Alien[NUMALIENS];
    private Spaceship PlayerShip;
    private PlayerBullet Bullet;
    private boolean bulletExists = false;
    private static String workingDirectory;
    private static boolean isGraphicsInitialised = false;
    private BufferStrategy strategy;


    public InvadersApplication() {

        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        ImageIcon icon = new ImageIcon(workingDirectory + "\\player_ship.png");
        PlayerShip = new Spaceship(icon.getImage());
        PlayerShip.setPosition(300, 530);

        ImageIcon icon2 = new ImageIcon(workingDirectory + "\\alien_ship_1.png");
        ImageIcon icon3 = new ImageIcon(workingDirectory + "\\alien_ship_2.png");
        for(int i=0; i < NUMALIENS; i++) {
            AliensArray[i] = new Alien(icon2.getImage(), icon3.getImage());
            double xx = (i%5)*80 + 70;
            double yy = (i/5)*40 + 50;
            AliensArray[i].setPosition(xx, yy);
        }

        Sprite2D.setWinWidth(WindowSize.width);

        repaint();

        addKeyListener(this);

        Thread t = new Thread(this);
        t.start();

        isGraphicsInitialised = true;

    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }

            boolean alienDirectionReversalNeeded = false;
            for (int i = 0; i < NUMALIENS; i++) {
                if (AliensArray[i].move()) {
                    alienDirectionReversalNeeded = true;
                }
            }

            if (alienDirectionReversalNeeded) {
                Alien.reverseDirection();
                for (int i = 0; i < NUMALIENS; i++) {
                    AliensArray[i].jumpDownwards();
                }
            }

            PlayerShip.move();
            if(bulletExists){
                Bullet.move();
            }
            this.repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                PlayerShip.setXSpeed(-4);
                break;
            case KeyEvent.VK_RIGHT:
                PlayerShip.setXSpeed(4);
                break;
            case KeyEvent.VK_SPACE:
                ImageIcon icon = new ImageIcon(workingDirectory + "\\bullet.png");
                Bullet = new PlayerBullet(icon.getImage());
                double x = PlayerShip.getX();
                double y = PlayerShip.getY();
                Bullet.setPosition(x, y);
                bulletExists = true;


                break;

        }

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT){
            PlayerShip.setXSpeed(0);
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void paint(Graphics g) {
        if(isGraphicsInitialised) {
            g = strategy.getDrawGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WindowSize.width, WindowSize.height);
            for(int i = 0; i < NUMALIENS; i++) {
                AliensArray[i].paint(g);
                PlayerShip.paint(g);
            }
            g.dispose();
            strategy.show();
        }
    }

    public static void main(String[] args) {
        workingDirectory = System.getProperty("user.dir");
        System.out.println("Working Directory = " + workingDirectory);

        InvadersApplication d = new InvadersApplication();
    }


}

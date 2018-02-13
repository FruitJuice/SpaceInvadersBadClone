import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

    private static final Dimension WindowSize = new Dimension(800,600);
    private static final int NUMALIENS = 30;
    private Alien[] AliensArray = new Alien[NUMALIENS];
    private Spaceship PlayerShip;
    private static String workingDirectory;
    private static boolean isGraphicsInitialised = false;
    private BufferStrategy strategy;
    private ArrayList<PlayerBullet> bulletsList = new ArrayList<>();


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
            for(PlayerBullet b: bulletsList){
                b.move();
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
                shootBullet();
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

    public void shootBullet() {
    // add a new bullet to our list
        ImageIcon icon4 = new ImageIcon(workingDirectory + "\\bullet.png");
        Image bulletImage = icon4.getImage();
        PlayerBullet b = new PlayerBullet(bulletImage);
        b.setPosition(PlayerShip.x+24.5, PlayerShip.y);
        bulletsList.add(b);
    }


    public void paint(Graphics g) {
        if(isGraphicsInitialised) {
            g = strategy.getDrawGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WindowSize.width, WindowSize.height);
            for(int i = 0; i < NUMALIENS; i++) {
                AliensArray[i].paint(g);
            }

            Iterator<PlayerBullet> iterator = bulletsList.iterator();
            while(iterator.hasNext()){
                PlayerBullet b = (PlayerBullet) iterator.next();
                b.paint(g);
            }

            PlayerShip.paint(g);

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

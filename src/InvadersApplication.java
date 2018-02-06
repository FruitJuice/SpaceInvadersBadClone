import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.image.*;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

    private static final Dimension WindowSize = new Dimension(700,700);
    private static final int NUMALIENS = 30;
    private Alien[] AliensArray = new Alien[NUMALIENS];
    private Spaceship PlayerShip;
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

        ImageIcon icon2 = new ImageIcon(workingDirectory + "\\player.png");
        PlayerShip = new Spaceship(icon2.getImage(), WindowSize.width);
        PlayerShip.setPosition(350, 600);

        int i = 0;
        int xx = 0;
        int yy = 50;

        ImageIcon icon = new ImageIcon(workingDirectory + "\\invader.png");
        for(i=0; i < NUMALIENS; i++) {
            AliensArray[i] = new Alien(icon.getImage(), WindowSize.width);
            AliensArray[i].setPosition(xx, yy);
            xx+= 70;
            if (xx >= 350) {
                xx = 0;
                yy+= 50;
            }
        }

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
            } catch (InterruptedException e) { }

            for(int i = 0; i < NUMALIENS; i++) {
                if(AliensArray[i].move()) {
                    for(int j = 0; j < NUMALIENS; j++) {
                        AliensArray[j].reverseDirection();
                    }
                    break;
                }
            }

            PlayerShip.move();
            this.repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT :
                PlayerShip.setXSpeed(-4);
                break;
            case KeyEvent.VK_RIGHT :
                PlayerShip.setXSpeed(4);
                break;}

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

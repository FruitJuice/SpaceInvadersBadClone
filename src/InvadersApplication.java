import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

    private static final Dimension WindowSize = new Dimension(800,600);
    private BufferStrategy strategy;
    private Graphics offscreenBuffer;
    private static final int NUMALIENS = 30;
    private ArrayList<Alien> AliensArray = new ArrayList<>();
    private Spaceship PlayerShip;
    private ArrayList<PlayerBullet> bulletsList = new ArrayList<>();
    private static boolean isInitialised = false;
    private static String workingDirectory;




    private boolean isGameInProgress = false;
    private int enemyWave = 0;
    private int score = 0;
    private int highscore = 0;


    public InvadersApplication() {
        //Make a display window centered on the screen
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);
        this.setTitle("Bad Space Invaders Clone");

        //Create the playable spaceship
        ImageIcon icon = new ImageIcon(workingDirectory + "\\player_ship.png");
        PlayerShip = new Spaceship(icon.getImage());
        PlayerShip.setPosition(425, 530);



        //Create and start animation thread
        Thread t = new Thread(this);
        t.start();


        addKeyListener(this);


        createBufferStrategy(2);
        strategy = getBufferStrategy();
        offscreenBuffer = strategy.getDrawGraphics();


        isInitialised = true;

    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) { }

            if (isGameInProgress){

                boolean alienDirectionReversalNeeded = false;
                for (int i = 0; i < AliensArray.size(); i++) {
                    if (AliensArray.get(i).move()) {
                        alienDirectionReversalNeeded = true;
                    }

                    if(isCollision(PlayerShip.x, AliensArray.get(i).x, PlayerShip.y, AliensArray.get(i).y,54,50,32,32)){
                        isGameInProgress = false;
                    }
                }

                if (alienDirectionReversalNeeded) {
                    for (int i = 0; i < AliensArray.size(); i++) {
                        AliensArray.get(i).reverseDirection();
                    }
                }

                if(AliensArray.size() == 0){
                    enemyWave++;
                    startNewWave();
                }

                PlayerShip.move();

                Iterator iterator = bulletsList.iterator();
                while(iterator.hasNext()){
                    PlayerBullet b = (PlayerBullet) iterator.next();
                    if(b.move()){
                        //removing bullet if it has gone offscreen
                        iterator.remove();
                    }

                    else{
                        //checking for collision between any alien and this bullet
                        double x2 = b.x, y2 = b.y;
                        double w1 = 50, h1 = 32;
                        double w2 = 6, h2 = 16;
                        for (Alien t: AliensArray){
                            double x1 = t.x;
                            double y1 = t.y;
                            if(isCollision(x1,x2,y1,y2,w1,w2,h1,h2)){
                                AliensArray.remove(t);
                                iterator.remove();
                                score+=10;
                                if(score>highscore){
                                    highscore=score;
                                }
                                break;
                            }
                        }
                    }

                }

            }

            this.repaint();
        }
    }

    public void startNewGame(){

        enemyWave = 1;
        score = 0;
        isGameInProgress = true;
        startNewWave();
    }

    public void startNewWave(){
        ImageIcon icon2 = new ImageIcon(workingDirectory + "\\alien_ship_1.png");
        ImageIcon icon3 = new ImageIcon(workingDirectory + "\\alien_ship_2.png");
        for(int i=0; i < NUMALIENS; i++) {
            Alien tempAlien = new Alien(icon2.getImage(), icon3.getImage());
            AliensArray.add(tempAlien);
            double xx = (i % 5) * 80 + 70;
            double yy = (i / 5) * 40 + 50;
            AliensArray.get(i).setPosition(xx, yy);
            AliensArray.get(i).setXSpeed(1+enemyWave);
        }
        PlayerShip.setPosition(425, 530);
    }

    private boolean isCollision(double x1, double x2, double y1, double y2, double w1, double w2, double h1, double h2){
        if(((x1<x2 && x1+w1>x2) || (x2<x1 && x2+w2>x1)) && ((y1<y2 && y1+h1>y2) || (y2<y1 && y2+h2>y1)))
            return true;
        else
            return false;
    }

    public void keyPressed(KeyEvent e) {
        if(isGameInProgress) {
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
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
            }
        }
        else{
            startNewGame();
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
        ImageIcon icon4 = new ImageIcon(workingDirectory + "\\bullet.png");
        PlayerBullet tempBullet = new PlayerBullet(icon4.getImage());
        tempBullet.setPosition(PlayerShip.x+24.5, PlayerShip.y);
        bulletsList.add(tempBullet);
    }

    public void writeString(Graphics g, int x, int y, int fontSize, String message){
        Font f = new Font("Times", Font.PLAIN, fontSize);
        g.setFont(f);
        FontMetrics fm = getFontMetrics(f);
        int width = fm.stringWidth(message);
        g.drawString(message, x-width/2, y);
    }


    public void paint(Graphics g) {
        if (!isInitialised) {
            return;
        }

        g = offscreenBuffer;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);

        if (isGameInProgress) {
            for (int i = 0; i < AliensArray.size(); i++) {
                AliensArray.get(i).paint(g);
            }
            PlayerShip.paint(g);

            Iterator<PlayerBullet> iterator = bulletsList.iterator();
            while (iterator.hasNext()) {
                PlayerBullet b = iterator.next();
                b.paint(g);
            }

            //score
            g.setColor(Color.white);
            writeString(g, WindowSize.width / 2, 60, 30, "Score: " + score + "        Best " + highscore);
        }
        else{
            g.setColor(Color.white);
            writeString(g,WindowSize.width/2,200,60,"GAME OVER");
            writeString(g,WindowSize.width/2,300,30,"Press any key to play");
            writeString(g,WindowSize.width/2,350,25,"[Arrow keys to move, space to fire]");
        }
            strategy.show();

    }



    public static void main(String[] args) {
        workingDirectory = System.getProperty("user.dir");
        System.out.println("Working Directory = " + workingDirectory);

        InvadersApplication d = new InvadersApplication();
    }


}

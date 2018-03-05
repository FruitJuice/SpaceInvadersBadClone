import java.awt.*;

public class Sprite2D {
    protected double x,y;
    protected double xSpeed=0;
    protected Image myImage;
    protected Image myImage2;
    protected int framesDrawn;
    protected static int winWidth;

    public Sprite2D(Image i) {
        myImage = i;
    }

    public void setPosition(double xx, double yy) {
        x = xx;
        y = yy;
    }

    public void paint(Graphics g) {
        framesDrawn++;
        if(framesDrawn%100<50)
            g.drawImage(myImage, (int)x, (int)y, null);
        else
            g.drawImage(myImage2, (int)x, (int)y, null);
    }

    public void setXSpeed(double dx){
        xSpeed = dx;
    }

    public static void setWinWidth(int w){
        winWidth = w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

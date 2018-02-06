import java.awt.*;

public class Sprite2D {
    protected double x,y;
    protected double xSpeed = 0;
    protected Image myImage;
    int winWidth;

    public Sprite2D(Image i, int windowWidth) {
        myImage = i;
        winWidth = windowWidth;
    }

    public void setPosition(int xx, int yy) {
        x = xx;
        y = yy;
    }

    public void setXSpeed(double dx) {
        xSpeed=dx;
    }

    public void paint(Graphics g) {
        g.drawImage(myImage, (int)x, (int)y, null);
    }


}

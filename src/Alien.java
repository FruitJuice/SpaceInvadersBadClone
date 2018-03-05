import java.awt.Image;

public class Alien extends Sprite2D{
    private static double xSpeed = 0;

    public Alien(Image i, Image i2) {
        super(i);
        myImage2 = i2;
    }

    public boolean move() {
        x+=xSpeed;
        if(x<=0 || x >= winWidth-myImage.getWidth(null)) {
            return true;
        }
        else
            return false;
    }

    public void reverseDirection() {
        xSpeed=-xSpeed;
        y+=20;

    }
}

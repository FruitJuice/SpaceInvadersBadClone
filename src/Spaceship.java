import java.awt.Image;

public class Spaceship extends Sprite2D{

    private double xSpeed = 0;

    public Spaceship(Image i) {
        super(i);
        myImage2 = i;
    }

    public void move() {
        x+=xSpeed;
    }

    public void setXSpeed(double dx) {
        xSpeed=dx;
    }

}

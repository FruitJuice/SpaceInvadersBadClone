import java.awt.Image;

public class PlayerBullet extends Sprite2D{

    private static double ySpeed = -5;

    public PlayerBullet(Image i) {
        super(i);
        myImage2 = i;
    }

    public boolean move() {
        y+=ySpeed;
        return (y<0);
    }
    
}

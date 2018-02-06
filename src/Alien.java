import java.awt.Image;

public class Alien extends Sprite2D{

    public Alien(Image i,int windowWidth ) {
        super(i, windowWidth);
        xSpeed = 4;
        // TODO Auto-generated constructor stub
    }

    public boolean move() {
        x+=xSpeed;
        if(x+70 >= winWidth || x <= 0) {
            return true;
        }
        else return false;
    }

    public void reverseDirection() {
        if (xSpeed == 4) {
            xSpeed = -4;
            y += 40;
        }
        else if (xSpeed == -4) {
            xSpeed = 4 ;
            y += 40;
        }

    }

}

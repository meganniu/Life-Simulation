import java.awt.Point;
import java.awt.image.BufferedImage;

public class Food
{
	BufferedImage img;
    // instance variables - replace the example below with your own
    private Point pos;
    private Hitbox box;
    private int nutrition;
    public Food(int nutritrion, Point pos)
    {
        this.pos = pos;
        box = new Hitbox(pos.x - 8, pos.y - 8, 16, 16);
        img = DrawArea.fImg;
    }

    public Point getPoint(){
        return pos;
    }
    public int getNutrition(){
        return nutrition;
    }
    
	public BufferedImage getImage() {
		return img;
	}

}

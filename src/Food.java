import java.awt.Point;
import java.awt.image.BufferedImage;

public class Food
{
	BufferedImage img;
    // instance variables - replace the example below with your own
    private Point pos;
    private double nutrition;
    public Food(Point pos, double nutrition)
    {
    	this.nutrition = nutrition;
        this.pos = pos;
        img = DrawArea.fImg;
    }

    public Point getPoint(){
        return pos;
    }
    public double getNutrition(){
        return nutrition;
    }
    
	public BufferedImage getImage() {
		return img;
	}

}

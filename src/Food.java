import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Food
{
	BufferedImage img;
    // instance variables - replace the example below with your own
    private Point pos;
    private double nutrition;
    Rectangle hitbox;
    public Food(Point pos, double nutrition)
    {
    	this.nutrition = nutrition;
        this.pos = pos;
        img = DrawArea.fImg;
        hitbox = new Rectangle(pos.x - 8, pos.y - 8, 16, 16);
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
	
	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("<html><pre><span style=\"font-family: arial\">Food\t\t");
		stats.add("<html><pre><span style=\"font-family: arial\">Position\t\t(" + pos.x + ", " + pos.y + ")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Nutrition Value\t" + new DecimalFormat("#.##").format(nutrition) + " </span></pre></html>");
		return stats;
	}

}

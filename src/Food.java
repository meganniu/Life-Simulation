import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Food object gives nutrition when eaten by herbivores
 */
public class Food
{
	/**
	 * image of food
	 */
	private BufferedImage img;
    
	/**
	 * position of food
	 */
	private Point pos;
    
	/**
	 * nutrition value of food
	 */
	private double nutrition;
    
	/**
	 * hitbox of food
	 */
	Rectangle hitbox;
    
	/**
	 * Food constructor
	 * @param pos randomized position of food
	 * @param nutrition randomized nutrition of food
	 */
    public Food(Point pos, double nutrition)
    {
    	this.nutrition = nutrition;
        this.pos = pos;
        img = DrawArea.fImg;
        hitbox = new Rectangle(pos.x - 8, pos.y - 8, 16, 16);
    }

    /**
     * get point of food
     * @return point of food
     */
    public Point getPoint(){
        return pos;
    }
    
    /**
     * get nutrition value of food
     * @return nutrition value of food
     */
    public double getNutrition(){
        return nutrition;
    }
    
    /**
     * incrementally decrease the nutrition of food over time if it is not eaten
     */
    public void decayNutrition(){
    	nutrition-= Main.foodDecayRate;
    	if(nutrition<0)
    		nutrition = 0;
    }
    
    /**
     * get image of food
     * @return image of food
     */
	public BufferedImage getImage() {
		return img;
	}
	
	/**
	 * return arraylist of food stats in html formatting
	 * @return arraylist of food stats in html formatting
	 */
	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("<html><pre><span style=\"font-family: arial\">Food\t\t");
		stats.add("<html><pre><span style=\"font-family: arial\">Position\t\t(" + pos.x + ", " + pos.y + ")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Nutrition Value\t" + new DecimalFormat("#.##").format(nutrition) + " </span></pre></html>");
		return stats;
	}

}

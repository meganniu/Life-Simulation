import java.awt.Point;
import java.awt.image.BufferedImage;

public class Egg
{
    private double hatchTime;
    private Point pos;
    
    private double speed;
    
    private int detectRadius;
    
    private int gen;
    
    BufferedImage img;
    
    /**
     * organism to be born
     */
    private Organism organism;
    
    /**
     * parent of the egg
     */
    private Organism parent;
    
    /**
     * if organism hatched is a herbivore (if false, org is a carnivore)
     */
    private boolean herbivore;
    
    /**
     * counting until timer = hatchtime
     * (when the egg will hatch)
     */
    private int timer;
      
    /**
     * Constructor for objects of class Egg
     */
    public Egg(Point pos, int hatchTime, Organism parent)
    {
    	double ran = Math.random();
    	
    	timer = 0;
    	
    	/**
    	 * if parent is carnivore, offspring is automatically a carnivore
    	 * 1/10 chance of herbivore offspring to be carnivores (evolution)
    	 */
    	herbivore = true;
    	if(parent instanceof Carnivore || (int)(Math.random() * 10) + 1 > 9){ 
    		herbivore = false;
    	}
    	
    	/**
    	 * for herbivores, the addition of speed due to genetic mutation ranges from -0.1 to 0.2
    	 * for carnivores, the addition of speed due to genetic mutation ranges from -0.2 to 0.1
    	 * this way, herbivores will become naturally faster and carnivores will become naturally faster
    	 */
        this.speed = parent.getSpeed();
        
        if(herbivore){
        	this.speed += ran * 1.3 - 0.1;
        }
        else{
        	this.speed += ran * 1.3 - 0.2;
        }
        
        this.detectRadius = parent.detectRadius;
        this.hatchTime = hatchTime; //60 ticks is the hatchtime
        this.pos = pos;
        
    }
    
    public boolean incrementTimer(){
    	timer++;
    	if(timer >= hatchTime){
    		return true;
    	}
    	return false;
    }
    
    public Herbivore getHerbivore(){
    	return new Herbivore(pos, (int)(Math.random()*(1000-16)+8), speed, detectRadius, gen);
    }
    
    public Carnivore getCarnivore(){
    	return new Carnivore(pos, (int)(Math.random()*(1000-16)+8), speed, detectRadius, gen);
    }
    
    public Point getPos(){
    	return pos;
    }
    
    public boolean isHerbivore(){
    	return herbivore;
    }
    
    public BufferedImage getImage() {
		return img;
	}
}

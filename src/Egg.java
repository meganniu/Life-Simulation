import java.awt.Point;

public class Egg
{
    private double hatchTime;
    private Point pos;
    private boolean parent;
    /**
     * Constructor for objects of class Egg
     */
    public Egg(Point pos, int speed, int detectRadius, int gen, int hatchTime)
    {
        this.pos = pos;
        this.speed = speed;
        this.detectRadius = detectRadius;
        this.gen = gen;
        this.hatchTime = hatchTime;
        this.parent = parent;
    }
    
    public void hatch(int i){
        if (parent){
            drawArea.eggs.remove(i);
            //Carnivore baby = new Carnivore(pos, Math.floor(Math.random() * 359) + 0 ,  10, 80, 0, 0);
            drawArea.carnivores.add(baby);
        }
        else{
            drawArea.eggs.remove(i);
            //Herbivore baby = new Herbivore(pos, Math.floor(Math.random() * 359) + 0 ,  10, 80, 0, 0);
            drawArea.herbivores.add(baby);
        }
    }
}

public class Food
{
    // instance variables - replace the example below with your own
    private Point pos;
    private Hitbox box;
    private int nutrition;
    public Food(int nutritrion, Point pos)
    {
        this.pos = pos;
        box = new Hitbox(pos.x - 1, pos.y - 1, 3, 3);
    }

    public Point getPoint(){
        return pos;
    }
    public int getNutrition(){
        return nutrition;
    }
}

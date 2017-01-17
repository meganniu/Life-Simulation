import java.awt.Point;

public abstract class Organism {
	Point pos;
	
	double angle;
	
	public Organism(Point pos, double angle){
		this.angle = angle;
		this.pos = pos;
	}
	
	public void move(int width, int height){
		Point nextPos = nextPos(pos, angle);
		
		if(nextPos.x >= width){
			System.out.println("border encountered X");
			//nextPos = nextPos(posX, posY, 270 - angle);
			//angle = 270 - angle;
			if(angle >= 270 && angle <= 360){
				nextPos = nextPos(pos, 540 - angle);
				angle = 540 - angle;
			}
			else if(angle <= 90 && angle >= 0){
				System.out.print("HERE");
				nextPos = nextPos(pos, 180 - angle);
				angle = 180 - angle;
			}
		}
		if(nextPos.x <= 0){
			System.out.println("border encountered X");
			if(angle >= 180 && angle <= 270){
				nextPos = nextPos(pos, 540 - angle);
				angle = 540 - angle;
			}
			else if(angle < 180 && angle >=90){
				nextPos = nextPos(pos, 360 - angle);
				angle = 360 - angle;
			}
		}
		
		if(nextPos.y >= height){
			System.out.println("border encountered Y, rejX:" + nextPos.x + " rejY:" + nextPos.y);
			//if(angle >= 0 && angle <= 90){
				nextPos = nextPos(pos, 360 - angle);
				angle = 360 - angle;
				
				nextPos.y = pos.y + (nextPos.y - pos.y) * -1;//disregarding cast rule in this case messes up the path of org Restore the cast rule
			//}

		}
		else if(nextPos.y <= 0){
			System.out.println("border encountered Y, rejX:" + nextPos.x + " rejY:" + nextPos.y);
			nextPos = nextPos(pos, 360 - angle);
			angle = 360 - angle;
		}
		pos.x = nextPos.x;
		pos.y = nextPos.y;
		
		System.out.println("X: " + pos.x + " Y: " + pos.y + " angle: " + angle);
	}
	
	public Point nextPos(Point past, double angle){
    	/**
    	 * using tan(x) = rise/run
    	 * assuming run is always 2 (i.e. moving 2 pixels horizontally each time), rise (change in vertical movement) can 
    	 * be expressed as rise = 2tan(x)
    	 * where x is the angle of movement
    	 */
    	
    	double nextYDouble = Math.tan(Math.toRadians(angle)) * 2;//expression of rise in terms of angle
    	int nextY = (int) nextYDouble;
    	
    	if(nextY < 0 && (angle > 0 && angle < 180)){//meaning bug will go down
    		System.out.println("nextY: " + nextY);
    		nextY = nextY * -1;
    		System.out.println("nextY: " + nextY);
    	}
    	if(nextY > 0 && (angle > 180 && angle < 360)){//meaning bug will go down
    		System.out.println("nextY: " + nextY);
    		nextY = nextY * -1;
    		System.out.println("nextY: " + nextY);
    	}
    	
    	//if(angle > 180 && angle < 270){//disregard cast rule
    		//nextY = nextY * (-1);
    	//}
    	
    	//nextY = nextYDouble;
    	
    	if(angle == 270.0){
    		nextY = -2;//if angle is 270 move vertically down by 2
    	}
    	else if(angle == 90.0){ //for tan(x), 90 and 270 are asymptotes
    		nextY = 2;//if angle is 90, move vertically up 2
    	}
    	
    	
    	
    	Point nextPos = new Point(); //{nextX, nextY}  
    	
    	if(angle == 90.0 || angle == 270.0){
    		nextPos.x = past.x;//traveling vertically, no change in x
    	}
    	else if((angle >= 0 && angle < 90.0) || (angle >270.0 && angle <= 360.0)){
        	nextPos.x = past.x + 2; //moving 2 pixels horizontally to the right
    	}
    	else{
    		nextPos.x = past.x - 2; //moving 2 pixels horizontally to the left
    	}
    	
    	nextPos.y = past.y - nextY;//moving the organism vertically (direction dep on earlier calcs)
    	
    	return nextPos;
    }
	
	public Point getPoint(){
		return pos;
	}
	
	public double getAngle(){
		return angle;
	}
	
	
}

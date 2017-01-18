import java.awt.Point;

public class Carnivore extends Organism {
	public Carnivore(Point pos, double angle, int speed, int detectRadius) {
		super(pos, angle, speed, detectRadius);
	}

	public double detectHerbivore() {
		
		
		
		double shortestDistance = -1;
		int indexOfClosest = -1;
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			Point hPoint = DrawArea.herbivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance < detectRadius && (distance < shortestDistance || shortestDistance == -1)) {
				shortestDistance = distance;
				indexOfClosest = i;
			}

		}
		if (shortestDistance == -1)
			return this.angle;
		else {
			//int deltaX = ;
			//int deltaY;
			
			double angle =  Math.atan2(pos.y-DrawArea.herbivores.get(indexOfClosest).getPoint().y, pos.x-DrawArea.herbivores.get(indexOfClosest).getPoint().x);
			angle = Math.toDegrees(angle);
			
			if(angle >= 0 && angle <= 180){
				angle = 180 - angle;
			}
			else if(angle >= -180 && angle <=0){
				angle = 180 - angle;
			}
			
			//angle =  Math.atan2(DrawArea.herbivores.get(indexOfClosest).getPoint().x-pos.x,DrawArea.herbivores.get(indexOfClosest).getPoint().y-pos.y);
			//(angle<0)
				//angle+=360;
			return angle;
		}
	}
}

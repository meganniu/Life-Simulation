import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * Carnivore organism objects
 */
public class Carnivore extends Organism {

	private boolean chasing = false;
	private boolean canChase = true;

	private long chaseStart;
	private long cooldownStart;
	private long timeBorn;
	
	/**
	 * Carnivore constructor
	 * @param generation generation of carnivore
	 * @param pos Point position
	 * @param angle Double angle
	 * @param speed	Int speed
	 * @param detectRadius radius of detection for herbivores
	 * @param eggCycle time till hatching of eggs laid
	 * @param carnivorePoints how quantifiably carnivore the carnivore is
	 * @param energy Double energy of carnivore
	 * @param metabolism Double speed of energy use, metabolism
	 * @param chaseLength Time until carnivore gives up chase of a herbivore
	 */
	public Carnivore(int generation, Point pos, double angle, int speed, int detectRadius, int eggCycle, int carnivorePoints, double energy, double metabolism, long chaseLength) {
		super(generation, pos, angle, speed, detectRadius, eggCycle, carnivorePoints, energy, metabolism, chaseLength);
		timeBorn = GamePane.timeElapsed;
		img = DrawArea.cImg;
	}


	public double detectItem() {

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
		if (shortestDistance == -1) {
			chasing = false;
			return this.angle;
		} else {

			if (!chasing){
				chaseStart = GamePane.timeElapsed;
			}
			chasing = true;

			if (GamePane.timeElapsed < chaseStart + chaseLength && canChase) {

				double angle = 0;
				try{
				angle = Math.atan2(pos.y - DrawArea.herbivores.get(indexOfClosest).getPoint().y,
						pos.x - DrawArea.herbivores.get(indexOfClosest).getPoint().x);
				}catch(NullPointerException e){
					System.out.println("Error here");
				}
				angle = Math.toDegrees(angle);

				if (angle >= 0 && angle <= 180) {
					angle = 180 - angle;
				} else if (angle >= -180 && angle <= 0) {
					angle = 180 - angle;
				}

				return angle;
			} else {
				chaseStart = GamePane.timeElapsed;
				if (canChase){
					canChase = false;
					cooldownStart = GamePane.timeElapsed;
					return (this.angle + Math.random() * 91 - 45) % 360;
				}
				else{
					if(GamePane.timeElapsed > cooldownStart + Main.chaseCD){
						canChase = true;
					}
					return this.angle;
				}
				
				
			}

		}
	}

	public void eat() {
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			Point hPoint = DrawArea.herbivores.get(i).getPoint();
			double distance = Math.hypot(pos.x - hPoint.x, pos.y - hPoint.y);
			if (distance <= 24) {
				energy += (((DrawArea.herbivores.get(i).getEnergy() / 10.0 + 5000.0) * metabolism )/ 100.0);
				if (energy > Main.maximumEnergy)
					energy = Main.maximumEnergy;
				if(DrawArea.herbivores.get(i)==StatsPanel.selectedOrg)
					StatsPanel.selectedOrg = null;
				DrawArea.herbivores.remove(i);
				i--;
				canChase=true;
			}
		}
	}
	
	public void layEgg(){
		if(GamePane.timeElapsed>sinceLastEgg+eggCycle && energy > Main.energyReq){
			sinceLastEgg=GamePane.timeElapsed;
			DrawArea.eggs.add(new Egg(generation + 1, new Point(pos), angle, speed, detectRadius, eggCycle, carnivorePoints, metabolism, chaseLength));
			energy-=Main.energyReq*2/3;
			if (energy < 0)
				energy = 0;

		}
	}

	public ArrayList<String> getStats() {
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("<html><pre><span style=\"font-family: arial\">Type\t\tCarnivore</span></pre><html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Generation\t\t" + String.valueOf(generation) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Position\t\t(" + pos.x + ", " + pos.y + ")</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Angle\t\t" + (int) angle + " deg</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Speed\t\t" + speed + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">R. Detection\t" +  detectRadius + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Egg Counter\t" + eggCycle + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Carnivorism\t" + carnivorePoints + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Energy\t\t" + new DecimalFormat("#.##").format(energy) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Metabolism\t" + new DecimalFormat("#.##").format(metabolism) + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Chase Length\t" + chaseLength + "</span></pre></html>");
		stats.add("<html><pre><span style=\"font-family: arial\">Time Alive\t" + (GamePane.timeElapsed - timeBorn) + "</span></pre></html>");
		return stats;
	}
	
	public ArrayList<String> getFinalStats(){
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("Type:  Carnivore");
		stats.add("Position:  (" + pos.x + ", " + pos.y + ")");
		stats.add("Angle:  " + (int) angle + " deg");
		stats.add("Speed:  " + speed);
		stats.add("R. Detection:  " +  detectRadius);
		stats.add("Egg Counter:  " + eggCycle);
		stats.add("Carnivorism:  " + carnivorePoints);
		stats.add("Energy:  " + new DecimalFormat("#.##").format(energy));
		stats.add("Metabolism:  " + new DecimalFormat("#.##").format(metabolism) );
		stats.add("Chase Length:  " + chaseLength);
		stats.add("Time Alive:  " + (GamePane.timeElapsed - timeBorn));
		return stats;
	}

}

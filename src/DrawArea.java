import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import com.sun.javafx.geom.Rectangle;
import javax.swing.Timer;

public class DrawArea extends JPanel{
	BufferedImage hImg = null;
	BufferedImage cImg = null;
	
	static Timer timer;
	
	private int width;
	private int height;
	
	static ArrayList<Carnivore> carnivores = new ArrayList<Carnivore>();
	static ArrayList<Herbivore> herbivores = new ArrayList<Herbivore>();
	static ArrayList<Food> food = new ArrayList<Food>();
	static ArrayList<Egg> eggs = new ArrayList<Egg>();
	
	
	public DrawArea (int width, int height, BufferedImage hImg, BufferedImage cImg){
		this.width = width;
		this.height = height;
		
		JPanel drawArea= new JPanel();
		
		this.setVisible(true);
		this.setOpaque(true);
        this.setPreferredSize (new Dimension (this.width, this.height)); // size

        this.hImg = hImg;
        this.cImg = cImg;
    	
        //FOR TESTING
    	Herbivore her1 = new Herbivore(new Point(240, 100), 200, 20, 10);
    	Herbivore her2 = new Herbivore(new Point(50, 200), 200, 15, 10);
    	herbivores.add(her1);
    	herbivores.add(her2);
    	Carnivore car1 = new Carnivore(new Point(100, 100), 100, 10, 80);
    	Carnivore car2 = new Carnivore(new Point(80, 32), 100, 18, 150);
    	carnivores.add(car1);
    	//carnivores.add(car2);
    	//END
    	
        timer = new Timer(50, new ActionListener(){
    		public void actionPerformed(ActionEvent ev){
    			for(int i = 0; i < carnivores.size(); i++){
    				double newAngle = carnivores.get(i).detectHerbivore();
    				if(newAngle != -1){
    					carnivores.get(i).setAngle(newAngle);

    				}
    				System.out.println(newAngle);
    				System.out.println(carnivores.get(i).angle);
    				carnivores.get(i).move(width, height);
    			}

    			for(int i = 0; i < herbivores.size(); i++){
    				herbivores.get(i).move(width, height);
    			}
    			repaint();
    		}
    	});
        
    }

    public void paintComponent (Graphics g){
    	super.paintComponent(g);
    	
    	/**
    	 * Draws and paints background
    	 */
    	g.setColor(Color.WHITE);
    	g.drawRect(0, 0, width - 1, height - 1);
    	g.fillRect(0, 0, width - 1, height - 1);
    	
    	/**
    	 * Rotates image of organism. Only works if image is a SQUARE. Otherwise, image will be cropped when rotated
    	 */

    	double locX = cImg.getWidth()/2;
    	double locY = cImg.getHeight()/2;
    	for(int i = 0; i < carnivores.size(); i++){
    		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(360 - carnivores.get(i).getAngle()), locX, locY);
        	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        	g.drawImage(op.filter(cImg, null), carnivores.get(i).getPoint().x-(cImg.getWidth()/2), carnivores.get(i).getPoint().y-(cImg.getHeight()/2), null);
		}
		for(int i = 0; i < herbivores.size(); i++){
			AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(360 - herbivores.get(i).getAngle()), locX, locY);
        	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        	g.drawImage(op.filter(hImg, null), herbivores.get(i).getPoint().x-(hImg.getWidth()/2), herbivores.get(i).getPoint().y-(hImg.getHeight()/2), null);
		}
    }
    
    

}

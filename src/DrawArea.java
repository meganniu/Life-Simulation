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
	BufferedImage img = null;
	
	static Timer timer;
	
	int width;
	int height;
	
	ArrayList<Carnivore> carnivores = new ArrayList<Carnivore>();
	ArrayList<Herbivore> herbivores = new ArrayList<Herbivore>();
	
	
	public DrawArea (int width, int height, BufferedImage img){
		this.width = width;
		this.height = height;
		
		JPanel drawArea= new JPanel();
		
		this.setVisible(true);
		this.setOpaque(true);
        this.setPreferredSize (new Dimension (this.width, this.height)); // size
        //this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); 

        this.img = img;
    	
    	Herbivore org = new Herbivore(new Point(200, 200), 45);
    	herbivores.add(org);
    	
        timer = new Timer(50, new ActionListener(){
    		public void actionPerformed(ActionEvent ev){
    			for(int i = 0; i < carnivores.size(); i++){
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
    	double locX = img.getWidth()/2;
    	double locY = img.getHeight()/2;
    	for(int i = 0; i < carnivores.size(); i++){
    		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(360 - carnivores.get(i).getAngle()), locX, locY);
        	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        	g.drawImage(op.filter(img, null), carnivores.get(i).getPoint().x, carnivores.get(i).getPoint().y, null);
		}
		for(int i = 0; i < herbivores.size(); i++){
			AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(360 - herbivores.get(i).getAngle()), locX, locY);
        	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        	g.drawImage(op.filter(img, null), herbivores.get(i).getPoint().x, herbivores.get(i).getPoint().y, null);
		}
	

    	
    }
    
    

}

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
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import com.sun.javafx.geom.Rectangle;
import javax.swing.Timer;

public class DrawArea extends JPanel{
	BufferedImage img = null;
	
	static Timer timer;
	
	Double angle;
	
	int posX;
	int posY;
	
	int width;
	int height;
	
	
	public DrawArea (int width, int height, BufferedImage img){
		this.width = width;
		this.height = height;
		
		JPanel drawArea= new JPanel();
		
		this.setVisible(true);
		this.setOpaque(true);
        this.setPreferredSize (new Dimension (this.width, this.height)); // size
        //this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); 

        this.img = img;
        
        angle = 45.0;
    	posX = 100;
    	posY = 100;
    	
        timer = new Timer(50, new ActionListener(){
    		public void actionPerformed(ActionEvent ev){
    			
    			System.out.println("X:" + posX + " Y: " + posY + " Angle:" + angle);
    			
    			Point nextPos = nextPos(posX, posY, angle);
    			
    			if(nextPos.getX() >= width){
    				System.out.println("border encountered X");
    				//nextPos = nextPos(posX, posY, 270 - angle);
    				//angle = 270 - angle;
    				if(angle >= 270 && angle <= 360){
    					nextPos = nextPos(posX, posY, 540 - angle);
        				angle = 540 - angle;
    				}
    				else if(angle <= 90 && angle >= 0){
    					nextPos = nextPos(posX, posY, 360 - angle);
    					angle = 360 - angle;
    				}
    			}
    			if(nextPos.getX() <= 0){
    				System.out.println("border encountered X");
    				if(angle >= 180 && angle <= 270){
    					nextPos = nextPos(posX, posY, 540 - angle);
        				angle = 540 - angle;
    				}
    				else if(angle < 180 && angle >=90){
    					nextPos = nextPos(posX, posY, 360 - angle);
    					angle = 360 - angle;
    				}
    			}
    			
    			if(nextPos.getY() >= height){
    				System.out.println("border encountered Y, rejX:" + nextPos.getX() + " rejY:" + nextPos.getY());
    				//if(angle >= 0 && angle <= 90){
    					nextPos = nextPos(posX, posY, 360 - angle);
        				angle = 360 - angle;
        				
        				nextPos.setLocation(nextPos.getX(), posY + (nextPos.getY() - posY) * -1);//disregarding cast rule in this case messes up the path of org Restore the cast rule
    				//}

    			}
    			else if(nextPos.getY() <= 0){
    				System.out.println("border encountered Y, rejX:" + nextPos.getX() + " rejY:" + nextPos.getY());
    				nextPos = nextPos(posX, posY, 360 - angle);
    				angle = 360 - angle;
    			}
    			posX = (int) nextPos.getX();
    			posY = (int) nextPos.getY();
    			//System.out.println("nextPosXAL:" + nextPos[0] + " nextPosYAL:" + nextPos[1]);
    			repaint();
    		}
    	});
        
        //System.out.println("Width: " + drawArea.getWidth() + " Height: " + drawArea.getHeight());
        //System.out.println("Width: " + this.width + " Height: " + this.height);
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
    	AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(360 - angle), locX, locY);
    	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    	g.drawImage(op.filter(img, null), posX, posY, null);
    	
    	
    	
    }
    
    public Point nextPos(int pastX, int pastY, double angle){
    	/**
    	 * using tan(x) = rise/run
    	 * assuming run is always 2 (i.e. moving 2 pixels horizontally each time), rise (change in vertical movement) can 
    	 * be expressed as rise = 2tan(x)
    	 * where x is the angle of movement
    	 */
    	
    	double nextYDouble = Math.tan(Math.toRadians(angle)) * 2;//expression of rise in terms of angle
    	int nextY = (int) nextYDouble;
    	
    	if(angle > 180 && angle < 270){//disregard cast rule
    		nextY = nextY * (-1);
    	}

    	if(angle == 270.0){
    		nextY = -2;//if angle is 270 move vertically down by 2
    	}
    	else if(angle == 90.0){ //for tan(x), 90 and 270 are asymptotes
    		nextY = 2;//if angle is 90, move vertically up 2
    	}
    	
    	
    	Point nextPos = new Point(); //{nextX, nextY}  
    	int x, y;
    	if(angle == 90.0 || angle == 270.0){
    		x = pastX;//traveling vertically, no change in x
    	}
    	else if((angle >= 0 && angle < 90.0) || (angle >270.0 && angle <= 360.0)){
        	x = pastX + 2; //moving 2 pixels horizontally to the right
    	}
    	else{
    		x = pastX - 2; //moving 2 pixels horizontally to the left
    	}
    	
    	y = pastY - nextY;//moving the organism vertically (direction dep on earlier calcs)
    	
    	nextPos.setLocation(x, y);
    	return nextPos;
    }

}

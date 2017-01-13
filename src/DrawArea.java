import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.sun.javafx.geom.Rectangle;

public class DrawArea extends JPanel{
	BufferedImage img = null;
	public DrawArea (int width, int height, BufferedImage img){
		JPanel drawArea= new JPanel();
		
		this.setVisible(true);
		this.setOpaque(true);
        this.setPreferredSize (new Dimension (width, height)); // size
        //this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10)); 
        this.img = img;
    }

    public void paintComponent (Graphics g){
    	super.paintComponent(g);
    	
    	g.setColor(Color.WHITE);
    	g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
    	g.fillRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
    	
    	
    	double locX = img.getWidth()/2;
    	double locY = img.getHeight()/2;
    	AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(180), locX, locY);
    	AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

    	
    	g.drawImage(op.filter(img, null), 200, 200, null);

    	//repaint();
    }

}

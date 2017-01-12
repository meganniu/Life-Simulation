import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class DrawArea extends JPanel{
	BufferedImage img = null;
	public DrawArea (int width, int height, BufferedImage img){
		JPanel drawArea= new JPanel();
		this.setVisible(true);
		this.setBackground(Color.WHITE);
        this.setPreferredSize (new Dimension (width, height)); // size
        this.img = img;
    }

    public void paintComponent (Graphics g){
    	g.drawImage(img, 200, 200, null);
    	//organism.show();
    	
    }

}

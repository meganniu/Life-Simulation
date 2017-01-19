import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.sun.javafx.geom.Rectangle;
import javax.swing.Timer;

public class DrawPane extends JPanel implements MouseListener {

	static Timer timer;

	int width;
	int height;
	private DrawArea drawArea = new DrawArea();

	public DrawPane(int width, int height) {
		
		this.width = width;
		this.height = height;

		this.addMouseListener(this);
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(width, height)); // size

		timer = new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				drawArea.updatePositions();
				repaint();
			}
		});

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(drawArea.getSubimage(Main.xShift, Main.yShift, Main.drawWidth, Main.drawHeight), 0, 0, this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean found = false;
		int x = e.getX() + Main.xShift;
		int y = e.getY() + Main.yShift;
		System.out.println(x + " "+ y);
		for (int i = 0; i < DrawArea.carnivores.size(); i++) {
			if(DrawArea.carnivores.get(i).selected)
				DrawArea.carnivores.get(i).setSelected(false);
			else if (DrawArea.carnivores.get(i).hitbox.contains(x, y) && !found) {
				DrawArea.carnivores.get(i).setSelected(true);
				found = true;
			} else
				DrawArea.carnivores.get(i).setSelected(false);
		}
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			if(DrawArea.herbivores.get(i).selected)
				DrawArea.herbivores.get(i).setSelected(false);
			else if (DrawArea.herbivores.get(i).hitbox.contains(x, y) && !found) {
				DrawArea.herbivores.get(i).setSelected(true);
				found = true;
			} else
				DrawArea.herbivores.get(i).setSelected(false);
			
		}
		drawArea.updateImage();
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}

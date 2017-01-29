import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartScreen extends JPanel implements MouseMotionListener, MouseListener {

	JLabel title = new JLabel("Evolution Simulator");
	JButton startBtn = new JButton("Start");
	JButton instBtn = new JButton("Instructions");
	JButton quitBtn = new JButton("Quit");

	int shiftx = 500, shifty = 400;

	BufferedImage back = null, front = null, frontSel;

	boolean selected = false;

	Rectangle r = new Rectangle((1000 - 700) / 2 - (shiftx - 500) / 15, (800 - 400) / 2 - (shifty - 400) / 12, 700,
			400);

	public StartScreen() {

		addMouseMotionListener(this);
		addMouseListener(this);

		try {
			back = ImageIO.read(new File("images/background.jpg"));
			front = ImageIO.read(new File("images/foreground.png"));
			frontSel = ImageIO.read(new File("images/foregroundS.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setPreferredSize(new Dimension(1000, 800));
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.insets = new Insets(300, 5, 5, 5);

		/*add(title, c);

		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 1;
		add(startBtn, c);

		c.gridy = 2;
		add(instBtn, c);

		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.gridy = 3;
		c.weighty = 1;
		add(quitBtn, c);*/
	}

	public void paintComponent(Graphics g) {
		g.drawImage(back.getSubimage(shiftx / 10, shifty / 8, getWidth(), getHeight()), 0, 0, null);

		if (selected)
			g.drawImage(frontSel, r.x, r.y, null);
		else
			g.drawImage(front, r.x, r.y, null);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(r.contains(e.getPoint()))
			selected = true;
		else
			selected = false;
		
		shiftx = e.getX();
		shifty = e.getY();
		r.x = (1000 - 700) / 2 - (shiftx - 500) / 15;
		r.y = (800 - 400) / 2 - (shifty - 400) / 12;
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		if(r.contains(e.getPoint()))
			selected = true;
		else
			selected = false;
		
		shiftx = e.getX();
		shifty = e.getY();
		r.x = (1000 - 700) / 2 - (shiftx - 500) / 15;
		r.y = (800 - 400) / 2 - (shifty - 400) / 12;
		repaint();

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}

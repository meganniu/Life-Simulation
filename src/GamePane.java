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
import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.sun.javafx.geom.Rectangle;
import javax.swing.Timer;

public class GamePane extends Canvas implements MouseListener, Runnable {

	static boolean running = false;
	private Thread thread;
	static int tickCount;
	static int frameCount;

	int width;
	int height;
	private DrawArea drawArea = new DrawArea();

	public GamePane(int width, int height) {

		this.width = width;
		this.height = height;

		this.addMouseListener(this);
		this.setPreferredSize(new Dimension(width, height)); // size

	}

	public synchronized void start() {
		if (running)
			return;

		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		double delta = 0.0;
		int ticksPerSecond = 500;
		double ns = 1000000000.0 / ticksPerSecond;
		int frames = 0;
		int ticks = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				ticks++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frameCount = frames;
				tickCount = ticks;
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() { // Per tick
		drawArea.updatePositions();
		Main.statsPanel.updateStats();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}
		drawArea.updateImage();
		Graphics g = bs.getDrawGraphics();
		g.drawImage(drawArea.getSubimage(Main.xShift, Main.yShift, Main.drawWidth, Main.drawHeight), 0, 0, this);
		g.drawString("FPS: " + frameCount + " | Ticks: " + GamePane.tickCount, 5, 15);
		g.dispose();
		bs.show();
	}

	public void paint(Graphics g) {
		g.drawImage(drawArea.getSubimage(Main.xShift, Main.yShift, Main.drawWidth, Main.drawHeight), 0, 0, this);
		g.drawString("FPS: " + frameCount + " | Ticks: " + GamePane.tickCount, 5, 15);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean found = false;
		int x = e.getX() + Main.xShift;
		int y = e.getY() + Main.yShift;
		System.out.println(x + " " + y);
		for (int i = 0; i < DrawArea.carnivores.size(); i++) {
			if (DrawArea.carnivores.get(i).selected)
				DrawArea.carnivores.get(i).setSelected(false);
			else if (DrawArea.carnivores.get(i).hitbox.contains(x, y) && !found) {
				DrawArea.carnivores.get(i).setSelected(true);
				found = true;
				StatsPanel.selectedOrg = DrawArea.carnivores.get(i);
			} else
				DrawArea.carnivores.get(i).setSelected(false);
		}
		for (int i = 0; i < DrawArea.herbivores.size(); i++) {
			if (DrawArea.herbivores.get(i).selected)
				DrawArea.herbivores.get(i).setSelected(false);
			else if (DrawArea.herbivores.get(i).hitbox.contains(x, y) && !found) {
				DrawArea.herbivores.get(i).setSelected(true);
				found = true;
				StatsPanel.selectedOrg = DrawArea.herbivores.get(i);
			} else
				DrawArea.herbivores.get(i).setSelected(false);

		}

		if (!found) {
			StatsPanel.selectedOrg = null;
		}

		drawArea.updateImage();
		Main.statsPanel.updateStats();
		render();
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

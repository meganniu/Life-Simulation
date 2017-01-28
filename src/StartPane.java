import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.prism.Graphics;

public class StartPane extends JFrame {

	public StartPane() {
		setSize(1000, 800);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.GREEN);
		g2.fillRect(0, 0, 1000, 800);
	}

	public static void main(String[] args) {
		StartPane sp = new StartPane();
	}

}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartScreen extends JPanel {
	
	JLabel title = new JLabel("Evolution Simulator");
	JButton startBtn = new JButton("Start");
	JButton instBtn = new JButton("Instructions");
	JButton quitBtn = new JButton("Quit");
	
	public StartScreen() {
		setPreferredSize(new Dimension(1000,800));
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx=1;
		c.insets = new Insets(300,5,5,5);
		
		add(title, c);
		
		c.insets = new Insets(5,5,5,5);
		c.gridy=1;
		add(startBtn, c);
		
		c.gridy = 2;
		add(instBtn, c);
		
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.gridy=3;
		c.weighty = 1;
		add(quitBtn, c);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect(0,0,getWidth(),getHeight());
	}
	
	public class StartButton extends JButton{
		
		public StartButton(String text){
			
		}
		
	}

}

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Main extends JFrame{
	static BufferedImage img = null;
	
	boolean startSim; //true to start sim, false to pause sim
	
	public Main(){
		JFrame main = new JFrame();
		main.setSize(1000, 800);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
		main.setResizable(false);
		
		main.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JButton go = new JButton("Go");
		go.addActionListener(new BtnListener());
		startSim = false;
		
		System.out.println("here2");
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		main.add(go, gbc);
		
		DrawArea drawArea = new DrawArea(500, 300, img);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		//gbc.weighty = 0.8;
		
		main.add(drawArea, gbc);
		
	}
	
	public class BtnListener implements ActionListener{
		BtnListener(){
		}
		
		public void actionPerformed(ActionEvent e){
			System.out.println("here1");
			if(startSim == false){
				DrawArea.timer.start();
			}
			else{
				DrawArea.timer.stop();
			}
			startSim = !startSim;
		}
		
	}
	
	public static void run(){
		
	}
	public static void main(String[] args){
		try {
		    img = ImageIO.read(new File("images/organism.png"));
		} catch (IOException e) {
			System.out.println("organism.png not found.");
		}
		
		Main Simulation = new Main();
	}
}

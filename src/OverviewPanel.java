import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Display amount of organisms and resources
 */
public class OverviewPanel extends JPanel{
	/**
	 * counters for amounts of organisms, carnivores, herbivores, eggs and food
	 */
	private int totalO, totalC, totalH, totalE, totalF;
	
	/**
	 * array of JLabels that display overview stats
	 */
	private ArrayList<JLabel> statLabel = new ArrayList<JLabel>();
	
	/**
	 * OverviewPanel constructor
	 */
	public OverviewPanel(){
		this.setPreferredSize(new Dimension(220, 150));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		for (int i = 0; i < 5; i++) {
			statLabel.add(new JLabel());
			statLabel.get(i).setAlignmentX(JLabel.CENTER_ALIGNMENT);
			this.add(statLabel.get(i));
		}
		
		update();
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		Border border = BorderFactory.createTitledBorder(blackline, "Overview");
		((TitledBorder) border).setTitleJustification(TitledBorder.CENTER);
		this.setBorder(border);

		revalidate();
		repaint();
	}
	
	/**
	 * Update overview panel with up to date statistics
	 */
	public void update(){
		totalC = DrawArea.carnivores.size();
		totalH = DrawArea.herbivores.size();
		totalE = DrawArea.eggs.size();
		totalF = DrawArea.food.size();
		totalO = totalC + totalH;
		
		ArrayList<String> statsArr = constructString();
		
		for (int i = 0; i < statLabel.size(); i++) {
			if (i < statsArr.size())
				statLabel.get(i).setText(statsArr.get(i));
			else
				statLabel.get(i).setText(" ");
			
		}
	}
	
	/**
	 * Construct strings of stats in html 
	 * @return arraylist of strings to be displayed in html
	 */
	public ArrayList<String> constructString(){
		ArrayList<String> statsArr = new ArrayList<String>();
		statsArr.add("<html><pre><span style=\"font-family: arial\">Total Organisms\t" + totalO + "</span></pre><html>");
		statsArr.add("<html><pre><span style=\"font-family: arial\">Total Carnivores\t" + totalC + "</span></pre><html>");
		statsArr.add("<html><pre><span style=\"font-family: arial\">Total Herbivores\t" + totalH + "</span></pre><html>");
		statsArr.add("<html><pre><span style=\"font-family: arial\">Total Eggs\t" + totalE + "</span></pre><html>");
		statsArr.add("<html><pre><span style=\"font-family: arial\">Total Food\t" + totalF + "</span></pre><html>");
		
		return statsArr;
	}
}

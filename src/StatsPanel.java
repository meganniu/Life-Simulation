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
 * Displays stats of selected object in simulation
 */
public class StatsPanel extends JPanel {
	/**
	 * title
	 */
	private JLabel title = new JLabel("Organsim Stats");
	
	/**
	 * ArrayList holding stat labels
	 */
	private ArrayList<JLabel> statLabel = new ArrayList<JLabel>();

	/**
	 * ArrayLIst holding stat strings
	 */
	protected static ArrayList<String> temp = new ArrayList<String>();

	/**
	 * selected obj for which to display stats
	 */
	protected static Organism selectedOrg = null;
	protected static Egg selectedEgg = null;
	protected static Food selectedFood = null;
	
	
	/**
	 * StatsPanel constructor
	 */
	public StatsPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		for (int i = 0; i < 11; i++) { // 7 is Number of stats there are NEEDS TO BE CHANGED EACH TIME :((((
			statLabel.add(new JLabel());
			statLabel.get(i).setAlignmentX(JLabel.CENTER_ALIGNMENT);
			this.add(statLabel.get(i));
		}
		
		updateStats();
		
		Border blackline = BorderFactory.createLineBorder(Color.black);
		Border border = BorderFactory.createTitledBorder(blackline, "Stats");
		((TitledBorder) border).setTitleJustification(TitledBorder.CENTER);
		this.setBorder(border);
		
		
		this.setPreferredSize(new Dimension(220, 400));

		revalidate();
		repaint();
	}

	/**
	 * Update stats of object during simulation
	 */
	public void updateStats() {
		if (selectedOrg != null) {
			temp = selectedOrg.getStats();
		}
		else if (selectedEgg != null){
			temp = selectedEgg.getStats();
		}
		else if (selectedFood != null){
			temp = selectedFood.getStats();
		}
		else {
			temp = new ArrayList<String>();
			temp.add("No organsism, egg, or food selected");
		}
		for (int i = 0; i < statLabel.size(); i++) {
			if (i < temp.size())
				statLabel.get(i).setText(temp.get(i));
			else
				statLabel.get(i).setText(" ");
			
		}
		revalidate();
		repaint();
	}
}

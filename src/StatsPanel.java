import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class StatsPanel extends JPanel {
	JLabel title = new JLabel("Organsim Stats");
	ArrayList<JLabel> statLabel = new ArrayList<JLabel>();

	static ArrayList<String> temp = new ArrayList<String>();

	static Organism selectedOrg = null;
	
	static Egg selectedEgg = null;

	static Food selectedFood = null;
	
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

	public void updateStats() {
		
		if (selectedOrg != null) {
			temp = selectedOrg.getStats();
		}

		
		if(temp != null){
			for (int i = 0; i < statLabel.size(); i++) {
				if (i < temp.size())
					statLabel.get(i).setText(temp.get(i));
				else
					statLabel.get(i).setText(" ");
			}
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

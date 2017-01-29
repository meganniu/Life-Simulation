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

	public StatsPanel() {
		if (selectedOrg == null) {
			temp.add("No organsism selected");
		} else {
			temp = selectedOrg.getStats();
		}

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		for (int i = 0; i < 8; i++) { // 7 is Number of stats there are NEEDS TO BE CHANGED EACH TIME :((((
			statLabel.add(new JLabel());
			statLabel.get(i).setAlignmentX(JLabel.CENTER_ALIGNMENT);
			this.add(statLabel.get(i));
		}
		
		updateStats();
		
		//this.add(new JLabel("hi"));
		Border blackline = BorderFactory.createLineBorder(Color.black);
		Border border = BorderFactory.createTitledBorder(blackline, "Stats");
		((TitledBorder) border).setTitleJustification(TitledBorder.CENTER);
		this.setBorder(border);
		
		
		this.setPreferredSize(new Dimension(220, 400));

		revalidate();
		repaint();
	}

	public void updateStats() {
		if (selectedOrg == null) {
			temp = new ArrayList<String>();
			temp.add("No organsism selected");
		} else {
			temp = selectedOrg.getStats();
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

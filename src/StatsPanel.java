import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (int i = 0; i < temp.size(); i++) {
			statLabel.add(new JLabel(temp.get(i)));
			this.add(statLabel.get(i));
		}

		this.setPreferredSize(new Dimension(400, 600));

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

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class StartScreen extends JPanel {

	/**
	 * Create the panel.
	 */
	public StartScreen() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\Desktop.Desktop-PC\\Desktop\\Git\\LifeSimulation\\images\\carnivore.png"));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		JButton startBtn = new JButton("Start");
		GridBagConstraints gbc_startBtn = new GridBagConstraints();
		gbc_startBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_startBtn.gridx = 0;
		gbc_startBtn.gridy = 3;
		add(startBtn, gbc_startBtn);

	}

}

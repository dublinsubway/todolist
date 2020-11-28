package termproject;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MainWindow extends JFrame {
	
	private JLabel nameLabel = new JLabel("Name");
	private JTextField nameTextField = new JTextField(20);
	private JCheckBox qualifiedCheckBox = new JCheckBox("Qualified");
	private JCheckBox completedCheckBox = new JCheckBox("Completed");
	private JRadioButton primaryRadioButton = new JRadioButton("Primary");
	private JRadioButton secondaryRadioButton = new JRadioButton("Secondary");
	private JRadioButton thirdLevelRadioButton = new JRadioButton("Third-level");
	private ButtonGroup educationLevelButtonGroup = new ButtonGroup();
	
	public MainWindow() {
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(nameLabel);
		this.add(nameTextField);
		this.add(qualifiedCheckBox);
		this.add(completedCheckBox);
		this.add(primaryRadioButton);
		this.add(secondaryRadioButton);
		this.add(thirdLevelRadioButton);
		educationLevelButtonGroup.add(primaryRadioButton);
		educationLevelButtonGroup.add(secondaryRadioButton);
		educationLevelButtonGroup.add(thirdLevelRadioButton);
		primaryRadioButton.setSelected(true);
	}
	
	public static void main(String[] args) {
		String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(systemLookAndFeelClassName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MainWindow window = new MainWindow();
		window.setSize(500, 300);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

}

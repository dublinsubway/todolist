package termproject;

import net.miginfocom.swing.MigLayout;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MainWindow extends JFrame {
	
	private JMenuBar menubar = new JMenuBar();
	private JMenu editMenu = new JMenu("Edit");
	private JMenu aboutMenu = new JMenu("About");
	private JMenuItem doneMenuButton = new JMenuItem("Done/undone");
	private JMenuItem addMenuButton = new JMenuItem("Add");
	private JMenuItem deleteMenuButton = new JMenuItem("Delete");
	private JMenuItem editMenuButton = new JMenuItem("Edit");
	private JMenuItem aboutMenuButton = new JMenuItem("About");
	
	private JCheckBox doneCheckBox = new JCheckBox("Done");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton editButton = new JButton("Edit");
	
	private List<Task> tasks = new ArrayList<Task>();
	private TaskTableModel tableModel = new TaskTableModel(tasks);
	private JTable taskTable = new JTable(tableModel);
	private JScrollPane scrollPane = new JScrollPane(taskTable);
	
	public MainWindow(String title) {
		super(title);
		this.setLayout(new MigLayout("insets 20"));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setJMenuBar(menubar);
		menubar.add(editMenu);
		editMenu.add(doneMenuButton);
		editMenu.add(addMenuButton);
		editMenu.add(editMenuButton);
		editMenu.add(deleteMenuButton);
		menubar.add(aboutMenu);
		aboutMenu.add(aboutMenuButton);
		
		
		this.add(addButton, "tag add");
		this.add(editButton, "tag edit");
		this.add(deleteButton, "tag delete");
		this.add(doneCheckBox, "split 4, wrap");
		this.add(scrollPane, "span 4 3, wrap");
	}
	
	public static void main(String[] args) {
		String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(systemLookAndFeelClassName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MainWindow window = new MainWindow("ToDo List");
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

}

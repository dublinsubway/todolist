package termproject;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

public class MainWindow extends JFrame {
	
	private JMenuBar menubar = new JMenuBar();
	private JMenu aboutMenu = new JMenu("About");
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
		menubar.add(aboutMenu);
		aboutMenu.add(aboutMenuButton);
		aboutMenuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this,
						"Developer email: lotterygame@rambler.ua\n\n"
						+ "KShramko developments, 2020\n"
						+ "All rights reserved.",
						"About",
						JOptionPane.PLAIN_MESSAGE);
			}
			
		});
		
		
		this.add(addButton, "tag add");
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TaskDialog dialog = new TaskDialog(MainWindow.this, "New task");
				dialog.setVisible(true);
				
			}
			
		});
		this.add(editButton, "tag edit");
		this.add(deleteButton, "tag delete");
		this.add(doneCheckBox, "split 4, wrap");
		this.add(scrollPane, "span 4 3, wrap");
		tableModel.insertRow(new Task("First task", LocalDateTime.of(2020, 12, 12, 11, 45)));
		tableModel.insertRow(new Task("Second task", LocalDateTime.of(2020, 12, 13, 12, 40)));
		tableModel.insertRow(new Task("Third task", LocalDateTime.of(2020, 12, 25, 13, 30)));
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

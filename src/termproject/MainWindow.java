package termproject;

import net.miginfocom.swing.MigLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class MainWindow extends JFrame {
	
	private JMenuBar menubar = new JMenuBar();
	private JMenu aboutMenu = new JMenu("About");
	private JMenuItem aboutMenuButton = new JMenuItem("About");
	
	private JCheckBox doneButton = new JCheckBox("Done");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton editButton = new JButton("Edit");
	
	private List<Task> tasks = new ArrayList<Task>();
	private TaskTableModel tableModel = new TaskTableModel(tasks);
	private JTable taskTable = new JTable(tableModel);
	private JScrollPane scrollPane = new JScrollPane(taskTable,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	
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
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (taskTable.getSelectedRow() == -1)
					JOptionPane.showMessageDialog(MainWindow.this,
							"No row is selected to be deleted.",
							"Incorrect selection",
							JOptionPane.PLAIN_MESSAGE);
				else {
					int resp = JOptionPane.showConfirmDialog(
							MainWindow.this,
							"Are you sure you want " +
							"to delete chosen task?",
							"Deletion confirmation",
							JOptionPane.YES_NO_OPTION);
					if (resp == 0) 
						tableModel.deleteRow(taskTable.getSelectedRow());
				} // end else
			} // end actionperformed

		});
		this.add(doneButton, "split 4, wrap");
		doneButton.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int row = taskTable.getSelectedRow();
	    		if (row != -1) {
	    			if (e.getStateChange() == ItemEvent.DESELECTED) {
	    				Task aTask = tasks.get(row);
	    				aTask.setIsNotActive(false);
	    				tableModel.updateRow(row, aTask);
	    			}	
	    			else {
	    				Task aTask = tasks.get(row);
	    				aTask.setIsNotActive(true);
	    				tableModel.updateRow(row, aTask);
	    			} // end else if	
	    		} // end tasktable if
			}
		});
			
		this.add(scrollPane, "span 4 3, wrap");
		
		taskTable.setSelectionMode(0); // single selection
		taskTable.setColumnSelectionAllowed(false);
		taskTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = taskTable.getSelectedRow();
				Task aTask = tasks.get(row);
				doneButton.setSelected(aTask.getIsNotActive());
			}
		});
		// size?
	    taskTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
	    taskTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
		scrollPane.setPreferredSize(new Dimension(400, 100));
		taskTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		// hardwiring values
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

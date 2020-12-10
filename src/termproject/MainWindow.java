package termproject;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

public class MainWindow extends JFrame {

	private JMenuBar menubar = new JMenuBar();
	
	private JMenu editMenu = new JMenu("Edit");
	private JMenuItem addMenuButton = new JMenuItem("Add");
	private JMenuItem editMenuButton = new JMenuItem("Edit");
	private JMenuItem deleteMenuButton = new JMenuItem("Delete");
	private JMenuItem doneMenuButton = new JMenuItem("(Un)done");
	
	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem aboutMenuButton = new JMenuItem("About");
	private JMenuItem helpMenuButton = new JMenuItem("Help");

	private JCheckBox doneButton = new JCheckBox("Done");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton editButton = new JButton("Edit");

	private List<Task> tasks = new ArrayList<Task>();
	
	private TaskTableModel tableModel = new TaskTableModel(tasks);
	private JTable taskTable = new JTable(tableModel) {
		public boolean editCellAt(int row, int column, java.util.EventObject e) {
			return false;
		}
		public String getToolTipText(MouseEvent e) {
			String tooltip = "";
			Point p = e.getPoint();
			int rowIndex = rowAtPoint(p);
			int columnIndex = columnAtPoint(p);

			try {
				if (columnIndex == 0)
					tooltip = getValueAt(rowIndex, columnIndex).toString();
				else
					return null; 
				// so blank tooltip does not appear if date is highlighted
			} catch (RuntimeException ex) {
				// do nothing
			}

			return tooltip;
		}
	};
	private JScrollPane scrollPane = new JScrollPane(taskTable,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	public MainWindow(String title) {
		super(title);
		this.setLayout(new MigLayout("insets 20"));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setJMenuBar(menubar);
		menubar.add(editMenu);
		// so we can open this menu from keyboard
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.add(addMenuButton);
		editMenu.add(editMenuButton);
		editMenu.add(deleteMenuButton);
		editMenu.add(doneMenuButton);
		
		menubar.add(helpMenu); 
		helpMenu.setMnemonic(KeyEvent.VK_H);
		// help message
		String helpLine = "This is a simple task list.\n" +
				"You can add new tasks, delete tasks that you don't need or edit existing ones.\n\n" +
				"Tasks are coloured differently, according to the time that is left until deadline.\n" +
				"<html><b><font color=#FF0000>Red</font></b> colour shows that the task is overdue.\n" +
				"Mark it as done or delete it if you do not want to see it.\n" +
				"<html><b><font color=#FFA500>Orange</font></b> colour means you have 24 hours or less left.\n" +
				"<html><b><font color=#FF00FF>Magenta</font> </b>in the table lets you know you have less than 3 days to finish it up.\n" +
				"<html><b><font color=#D3D3D3>Light gray</font> </b>tasks are marked as done by you, the user.\n\n" +
				"Contact details, if needed, can be found in \"About\" menu.\n" +
				"\n";
		helpMenu.add(helpMenuButton);
		helpMenuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this,
						helpLine,
						"Help",
						JOptionPane.PLAIN_MESSAGE);
			}

		});

		Action f1Action = new AbstractAction("showHelp") {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this,
						helpLine,
						"Help",
						JOptionPane.PLAIN_MESSAGE);
			}
		};
		// Calls a help page by action above when F1 button is pressed.
		helpMenuButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "showHelp");
		helpMenuButton.getActionMap().put("showHelp", f1Action);

		// simple about message
		helpMenu.add(aboutMenuButton);
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
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = taskTable.getSelectedRow();
				if (row == -1) // -1 is given when no row is selected
					JOptionPane.showMessageDialog(MainWindow.this,
							"No row is selected to be edited.",
							"Incorrect selection",
							JOptionPane.PLAIN_MESSAGE);
				else {
					TaskDialog dialog = new TaskDialog(MainWindow.this, "Edit task", row);
					dialog.setVisible(true);
				}
			}

		});
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
		/*
		 * Tracks the change of done checkbox. 
		 * If it is checked - task is done, so it is being moved to the end of the list.
		 * Unchecked - list is being sorted again to put it where it belongs.
		 */
		doneButton.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int row = taskTable.getSelectedRow();
				if (row != -1) {
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						Task aTask = tasks.get(row);
						aTask.setIsNotActive(false);
						tableModel.updateRow(row, aTask);
						sortByDate();
						tableModel.fireTableDataChanged();
						System.out.println("deselected event is called");
					}	
					else {
						Task aTask = tasks.get(row);
						aTask.setIsNotActive(true);
						while (row < tasks.size() - 1) {
							Task bottomTask = tasks.get(row + 1);
							tasks.set(row + 1, tasks.get(row));
							tasks.set(row, bottomTask);
							row++;
						}
						System.out.println("selected event is called");
						tableModel.fireTableDataChanged();
					} // end else if	
				} // end -1 if
			}
		});


		this.add(scrollPane, "span 4, wrap");

		taskTable.setSelectionMode(0); // single selection
		taskTable.setColumnSelectionAllowed(false); // cant select columns
		taskTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = taskTable.getSelectedRow();
				if (row != -1) {
					Task aTask = tasks.get(row);
					doneButton.setSelected(aTask.getIsNotActive());
				}
			}
		});
		// Calls an edit dialog if row is double clicked.
		taskTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable source = (JTable) e.getSource();
					int row = source.getSelectedRow();
					TaskDialog dialog = new TaskDialog(MainWindow.this, "Edit task", row);
					dialog.setVisible(true);
				}	
			}
		});

		taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() { // a что а как
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row, int col) {

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

				Task aTask = tasks.get(row);
				LocalDateTime currentDate = LocalDateTime.now();
				Duration duration = Duration.between(currentDate, aTask.getDueDate());

				if (isSelected) {
					setBackground(Color.BLUE);
				}
				else {
					if (aTask.getIsNotActive() == true) // if set as done
						setBackground(Color.LIGHT_GRAY);
					else if (aTask.getDueDate().isBefore(currentDate)) // past deadline
						setBackground(Color.RED);
					else if (duration.toHours() <= 24) // a day or less left
						setBackground(Color.ORANGE);
					else if (duration.toHours() <= 72) // 3 days or less left
						setBackground(Color.MAGENTA);
					else
						setBackground(table.getBackground());
				}
				return this;
			}   
		});
		// size
		scrollPane.setPreferredSize(new Dimension(400, 200));
		taskTable.getColumnModel().getColumn(1).setPreferredWidth(50);
		// hardwiring values
		tableModel.insertRow(new Task("First task", LocalDateTime.of(2020, 12, 8, 12, 10)));
		tableModel.insertRow(new Task("Second task", LocalDateTime.of(2020, 12, 15, 11, 40)));
		tableModel.insertRow(new Task("Third task", LocalDateTime.of(2020, 12, 25, 13, 30)));
	}

	public void insertRow(Task task) {
		this.tableModel.insertRow(task);
	}

	public void updateRow(Task task, int position) {
		this.tableModel.updateRow(position, task);
	}

	public Task getATask(int pos) {
		return tasks.get(pos);
	}

	public void sortByDate() {
		Collections.sort(tasks);
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

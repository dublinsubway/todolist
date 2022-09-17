package termproject;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class MainWindow extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JMenuBar menubar = new JMenuBar();
	
	// first letter is underlined as alt + e opens that menu
	private JMenu editMenu = new JMenu("<html><u>E</u>dit");
	private JMenuItem addMenuButton = new JMenuItem("Add");
	private JMenuItem editMenuButton = new JMenuItem("Edit");
	private JMenuItem deleteMenuButton = new JMenuItem("Delete");
	private JMenuItem doneMenuButton = new JMenuItem("(Un)done");
	private JMenuItem saveMenuButton = new JMenuItem("Save");
	
	private JMenu helpMenu = new JMenu("<html><u>H</u>elp");
	private JMenuItem aboutMenuButton = new JMenuItem("About");
	private JMenuItem helpMenuButton = new JMenuItem("Help");

	private JCheckBox doneButton = new JCheckBox("Done");
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton editButton = new JButton("Edit");
	private JButton saveButton = new JButton("Save");
	
	private List<Task> tasks = new ArrayList<Task>();
	
	private TaskTableModel tableModel = new TaskTableModel(tasks);
	private JTable taskTable = new JTable(tableModel) {
		/**
		 *  Allows the tooltip to appear when task is highlighted.
		 */
		private static final long serialVersionUID = 1L;
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
					// so blank tooltip does not appear if date is highlighted
					return null; 
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
		openFileOnStartup();
		
		this.setJMenuBar(menubar);
		menubar.add(editMenu);
		// so we can open this menu from keyboard
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.add(addMenuButton);
		addMenuButton.addActionListener(this);
		editMenu.add(editMenuButton);
		editMenuButton.addActionListener(this);
		editMenu.add(deleteMenuButton);
		deleteMenuButton.addActionListener(this);
		editMenu.add(doneMenuButton);
		doneMenuButton.addActionListener(this);
		editMenu.add(saveMenuButton);
		saveMenuButton.addActionListener(this);
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

		Action f1Action = new AbstractAction("showHelp") {

			/**
			 * Action for help page.
			 */
			private static final long serialVersionUID = 1L;

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
		
		helpMenu.add(helpMenuButton);
		helpMenuButton.addActionListener(f1Action);
		
		// simple about message
		helpMenu.add(aboutMenuButton);
		aboutMenuButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MainWindow.this,
						"Developer email: lotterygame@rambler.ua\n\n"
								+ "DublinSubway developments, 2020\n"
								+ "All rights reserved.",
								"About",
								JOptionPane.PLAIN_MESSAGE);
			}

		});
		this.add(addButton, "tag add");
		addButton.addActionListener(this);
		this.add(editButton, "tag edit");
		editButton.addActionListener(this);
		this.add(deleteButton, "tag delete");
		deleteButton.addActionListener(this);
		this.add(saveButton);
		saveButton.addActionListener(this);
		this.add(doneButton, "split 5, wrap");
		doneButton.addActionListener(this);
		
		
		this.add(scrollPane, "span 5, wrap");

		taskTable.setSelectionMode(0); // single selection
		taskTable.setColumnSelectionAllowed(false); // cant select columns
		taskTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = taskTable.getSelectedRow();
				if (row >= 0) {
					Task aTask = tasks.get(row);
					doneButton.setSelected(!aTask.isActive());
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

		taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			/**
			 * This is used to color background of the tables depending on date.
			 */
			private static final long serialVersionUID = 1L;

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
					if (!aTask.isActive()) // if set as done
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
	}

	/*
	 * As all items are called by at least two ways to make it more accessible for keyboard users
	 * (menubar and button), the actionperformed is here and not anonymous
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton || e.getSource() == addMenuButton) {
			TaskDialog dialog = new TaskDialog(MainWindow.this, "New task");
			dialog.setVisible(true);
		}
		else if (e.getSource() == editButton || e.getSource() == editMenuButton) {
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
		else if (e.getSource() == deleteButton || e.getSource() == deleteMenuButton) {
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
		} else if (e.getSource() == doneButton || e.getSource() == doneMenuButton) {
			/*
			 * Even though that is a checkbox, it listens to the boolean variable in task class. 
			 * If it is set to false - task is done, so it is being moved to the end of the list.
			 * Otherwise list is being sorted again to put task where it belongs.
			 */
			int row = taskTable.getSelectedRow();
			if (row != -1) {
				Task aTask = tasks.get(row);
				if (!aTask.isActive()) {
					aTask.setActive(true);
					tableModel.updateRow(row, aTask);
				}	
				else {
					aTask.setActive(false);
					tableModel.updateRow(row, aTask);
					while (row < tasks.size() - 1) {
						Task bottomTask = tasks.get(row + 1);
						tasks.set(row + 1, tasks.get(row));
						tasks.set(row, bottomTask);
						row++;
					} // end while
				} // end else if
				sortByDate(); // Collections.sort(tasks);
				tableModel.fireTableDataChanged();
			} // end -1 if
		} else if (e.getSource() == saveButton || e.getSource() == saveMenuButton) {
			// handling of file object
			File file = new File(".", "tasks.dat");
			try {
				if (file.createNewFile())
					; // do nothing
				else {
					file.delete();
					file.createNewFile();
				} // end else
			}  catch (IOException ex) {
			      ex.printStackTrace();
			    }
			// actually writing data
			try ( 
					FileOutputStream fos = new FileOutputStream(file);
					ObjectOutputStream oos = new ObjectOutputStream(fos)
					){
				oos.writeObject(tasks);
				JOptionPane.showMessageDialog(MainWindow.this,
						"Save successful.",
						"Save status",
						JOptionPane.PLAIN_MESSAGE);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	} // end actionperformed
	
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
	
	public void openFileOnStartup () {
		File file = new File("tasks.dat");
		
		if (file.exists()) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
			tasks = (List<Task>) ois.readObject();
			tableModel = new TaskTableModel(tasks);
			taskTable.setModel(tableModel);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // end catch
		} // end if exists
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

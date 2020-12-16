package termproject;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Task> tasks;
	private String[] columnNames = {"Task description", "Due date"};
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm");
	
	public TaskTableModel(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	@Override
	public int getRowCount() {
		return tasks.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int column) {
		return columnNames[column];
	}

	@Override
	public String getValueAt(int rowIndex, int columnIndex) {
		Task aTask = tasks.get(rowIndex);
		String result = null;
		switch (columnIndex) {
		case 0:
			result = aTask.getDescription();
			break;
		case 1:
			result = df.format(aTask.getDueDate());
			break;
		}
		return result;
	}
	
	 public boolean isCellEditable(int row, int col) {
         if (col < 2) { // as we have columns 0 and 1
             return true;
         } else {
             return false;
         }
     }

	public void insertRow(Task task) {
		this.tasks.add(task);
		fireTableRowsInserted(tasks.size(), tasks.size()); // last row
		
	}
	
	public void deleteRow(int position) {
		this.tasks.remove(position);
		fireTableRowsDeleted(position, position);
	}
	
	public void updateRow(int position, Task newTask) {
		tasks.set(position, newTask);
		fireTableRowsUpdated(position, position);
	}
}

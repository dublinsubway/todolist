package termproject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel {

	private List<Task> tasks;
	private String[] columnNames = {"Task description", "Due date"};
	private DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm");
	
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
	
	

}

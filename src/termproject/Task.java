package termproject;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Task {
	
	private String description;
	private Date dueDate;
	
	public Task(String desc, Date dt) {
		this.description = desc;
		this.dueDate = dt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}

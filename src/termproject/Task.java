package termproject;

import java.time.LocalDateTime;

public class Task {
	
	private String description;
	private LocalDateTime dueDate;
	boolean isActive;
	
	public Task(String desc, LocalDateTime dt) {
		this.description = desc;
		this.dueDate = dt;
		this.isActive = true;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}
	
	public boolean getIsActive() {
		return this.isActive;
	}
	
	public void setActive(boolean aa) {
		this.isActive = aa;
	}
}

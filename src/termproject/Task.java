package termproject;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Comparable<Task>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String description;
	private LocalDateTime dueDate;
	boolean isNotActive;
	
	public Task(String desc, LocalDateTime dt) {
		this.description = desc;
		this.dueDate = dt;
		this.isNotActive = false;
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
	
	public boolean getIsNotActive() {
		return this.isNotActive;
	}
	
	public void setIsNotActive(boolean aa) {
		this.isNotActive = aa;
	}
	
	@Override
	  public int compareTo(Task t) {
		if (isNotActive)
			return 1;
		else
			return getDueDate().compareTo(t.getDueDate());
	  }
}

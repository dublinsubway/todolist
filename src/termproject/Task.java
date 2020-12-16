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
	boolean active;
	
	public Task(String desc, LocalDateTime dt) {
		this.description = desc;
		this.dueDate = dt;
		this.active = true;
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
	
	public boolean isActive() {
		return this.active;
	}
	
	public void setActive(boolean aa) {
		this.active = aa;
	}
	
	@Override
	  public int compareTo(Task t) {
		if (!active) {
			LocalDateTime notActiveDate = LocalDateTime.of(9999, 12, 1, 10, 00);
			return notActiveDate.compareTo(t.getDueDate());
		}
		else
			return this.dueDate.compareTo(t.getDueDate());
	  }
}

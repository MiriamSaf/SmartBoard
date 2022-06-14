package model;

public class Task implements java.lang.Comparable<Task>{
	
	private String taskName;
	private String taskDescrip;
	
	public Task(String taskName, String taskDescrip) {
		this.setTaskName(taskName);
		this.setTaskDescrip(taskDescrip);
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescrip() {
		return taskDescrip;
	}

	public void setTaskDescrip(String taskDescrip) {
		this.taskDescrip = taskDescrip;
	}


    public int compareTo(Task task1, Task task2) {
            if (this.getTaskName() == null || task2.getTaskName() == null) {
                return 0;
            }
        return task1.getTaskName().compareTo(task2.getTaskName());
    }

	public int compare(Task o1, Task o2) {
		 return o1.getTaskName().compareTo(o2.getTaskName());
	}

	@Override
	public int compareTo(Task o) {
		return this.getTaskName().compareTo(o.getTaskName());
	}

}

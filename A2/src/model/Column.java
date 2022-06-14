package model;

import java.util.ArrayList;
import java.util.Collections;

/*class holding column and list of tasks*/

public class Column implements Deleteable, java.lang.Comparable<Task> {

	private String columnName;
	private ArrayList<Task> tasks;

	public Column(String columnName, ArrayList<Task> tasks) {
		this.columnName = columnName;
		this.tasks = tasks;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public Task getOneTask(int i) {
		return tasks.get(i);
	}

	public void setTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public void addTaskToBoard(Task addingTask) {
		if (addingTask != null) {
			tasks.add(addingTask);
		}

	}

	public void sortList() {
		Collections.sort(this.tasks, Collections.reverseOrder());
	}

	@Override
	public int compareTo(Task o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteChosen(String remove) {
		for (int i = 0; i < this.tasks.size(); i++) {
			if (this.tasks.get(i).getTaskName().equals(remove)) {
				// if this is the one to remove then remove it
				this.tasks.remove(i);
			}
		}
	}

	public void printTasks() {
		for (int i = 0; i < this.tasks.size(); i++) {
			System.out.println(this.tasks.get(i).getTaskName() + " " + this.tasks.get(i).getTaskDescrip());
		}
	}
}

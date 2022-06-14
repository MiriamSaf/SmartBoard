package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.Column;
import model.Task;

//test the column in the model
public class ColumnTest {
	

	ArrayList<Task> listOfTasks = new ArrayList<Task>();
	ArrayList<Column> column = new ArrayList<Column>();
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
		column = null;
		listOfTasks = null;
	}


	//delete a task from column
	@Test
	public void testDeleteColumn() {
		listOfTasks.add(new Task("taskname1", "descript"));
		listOfTasks.add(new Task("task2", "lengthy description"));
		Column column1 = new Column("namecol", listOfTasks);
		column1.deleteChosen("task2");
	}

	@Test
	//add new task through col
	public void testAddTaskToColumn() {
		Column column1 = new Column("namecol", listOfTasks);
		column1.addTaskToBoard(new Task("task3", "descript"));
		
	}
	
	@Test
	//printing tasks
	public void testPrintingTasks() {
		Column column1 = new Column("namecol", listOfTasks);
		column1.addTaskToBoard(new Task("task3", "descript"));
		column1.printTasks();
	}
	
	//test adding null task 
	@Test
	public void testAddNullTask() {
		Column column1 = new Column("namecol", listOfTasks);
		column1.addTaskToBoard(new Task("task3", "descript"));
		column1.addTaskToBoard(null);
	}
}

package test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Column;
import model.Task;

public class TaskTest {

	//test comparing two different tasks
	@Test
	public void testComparingTwotasks() {
		Task t1 = new Task("taskname1", "descript");
		Task t2 = new Task("task2", "lengthy description");
		
		t1.compare(t1, t2);
	}

}

package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.Column;
import model.Task;
import model.Workspace;

public class WorkspaceTest {

	ArrayList<Task> listOfTasks = new ArrayList<Task>();
	ArrayList<Column> column = new ArrayList<Column>();
	ArrayList<Board> board = new ArrayList<Board>();

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
		column = null;
		listOfTasks = null;
		board = null;
	}

	// test add valid board to workspace
	@Test
	public void testAddBoardToWorkspace() {

		ArrayList<Column> column = new ArrayList<Column>();

		listOfTasks.add(new Task("taskname1", "descript"));
		listOfTasks.add(new Task("task2", "lengthy description"));
		column.add(new Column("col1", listOfTasks));
		Board b1 = new Board("Board", column);

		Workspace ws = new Workspace(board);
		// try adding board to workspace
		ws.addBoardToList(b1);

	}

	// test to add null board to workspace
	@Test
	public void testAddNullBoardToList() {

		Workspace ws = new Workspace(board);
		// try adding board to workspace
		ws.addBoardToList(null);

	}

	// test delete a board from workspace
	@Test
	public void testDelBoardFromWorkspace() {

		ArrayList<Column> column = new ArrayList<Column>();

		listOfTasks.add(new Task("taskname1", "descript"));
		listOfTasks.add(new Task("task2", "lengthy description"));
		column.add(new Column("col1", listOfTasks));
		Board b1 = new Board("Board", column);

		Workspace ws = new Workspace(board);
		// try adding board to workspace
		ws.addBoardToList(b1);
		//then delete it 
		ws.deleteChosen("Board");

	}
	
	//test printing out the boards
	@Test
	public void testPrintBoards() {
		ArrayList<Column> column = new ArrayList<Column>();

		listOfTasks.add(new Task("taskname1", "descript"));
		listOfTasks.add(new Task("task2", "lengthy description"));
		column.add(new Column("col1", listOfTasks));
		Board b1 = new Board("Board", column);
		Board b2 = new Board("Board2", column);

		Workspace ws = new Workspace(board);
		// try adding board to workspace
		ws.addBoardToList(b1);
		ws.addBoardToList(b2);
		//then print these boards
		ws.printBoards();
		
	}

}

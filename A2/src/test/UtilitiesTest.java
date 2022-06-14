package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import application.UserHolder;
import model.Board;
import model.Column;
import model.DefaultUser;
import model.Task;
import model.Utilities;
import model.Workspace;

//utils test class
public class UtilitiesTest {

	Utilities utils = new Utilities();

	ArrayList<Board> boardList = new ArrayList<Board>();
	ArrayList<Task> listOfTasks = null;
	ArrayList<Column> column = new ArrayList<Column>();
	Board board = new Board("board1", column);
	DefaultUser deUser = new DefaultUser("pwd", "uname", "fname", "sname", "");
	UserHolder holder = UserHolder.getUserInstance();

	@Before
	public void start() {
		UserHolder holder = UserHolder.getUserInstance();
	}

	@After
	public void tearDown() {
		deUser = null;
		utils = null;
		holder.getUser().getWorkspace().setClearBoards();
		holder = null;
		boardList = null;
		column = null;
		listOfTasks = null;
		board = null;
	}

	// test no tabs - as in its null
	@Test
	public void testNoTabs() {
		// should return false if no tabs
		assertFalse(utils.checkNoTabs(null));

	}

	// test adding a board to a column
	@Test
	public void testAddingBoardToCol() {
		column.add(new Column("name", listOfTasks));
		Column col = new Column("colname", listOfTasks);

		column.add(new Column("name2", listOfTasks));
		Column col2 = new Column("colname", listOfTasks);
		board.addColumnToBoard(col2);
	}

	// test adding a duplicate tab
	@Test
	public void testAddingDuplicateTab() {

		holder.setUser(deUser);
		holder.getUser().setWorkspace(new Workspace(boardList));
		holder.getUser().getWorkspace().addBoardToList(board);
		boolean testAddDupl = utils.checkDuplicateTabName(holder, "board1");
		assertTrue(testAddDupl);

	}

	// test adding non duplicate tab which is false
	@Test
	public void testAddingNonDuplicateTab() {

		holder.setUser(deUser);
		holder.getUser().setWorkspace(new Workspace(boardList));
		holder.getUser().getWorkspace().addBoardToList(board);
		boolean testAddDupl = utils.checkDuplicateTabName(holder, "board2");
		assertFalse(testAddDupl);

	}

}

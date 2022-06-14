package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Board;
import model.Column;
import model.Task;


//testing board functions
public class BoardTest {

	ArrayList<Task> listOfTasks = null;
	ArrayList<Column> column = new ArrayList<Column>();
	Board board = new Board("board1", column);
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
		column = null;
		listOfTasks = null;
		board = null;
	}

	//test adding a board to a column
	@Test
	public void testAddingBoardToCol() {
		column.add(new Column("name", listOfTasks));
		Column col = new Column("colname", listOfTasks);

		column.add(new Column("name2", listOfTasks));
		Column col2 = new Column("colname", listOfTasks);
		board.addColumnToBoard(col2);
	}
	
	//test adding null column
	@Test
	public void testAddingnullBoard() {
		//it should do nothing as it doesn't allow null to go through
		board.addColumnToBoard(null);
		
	}
	
	@Test
	public void printColumnTest() {

		column.add(new Column("name", listOfTasks));
		column.add(new Column("name2", listOfTasks));
		Column col2 = new Column("colname", listOfTasks);
		board.addColumnToBoard(col2);
		board.printColumnNameInBoard();
	}
	
	//delete column based on name
	@Test 
	public void deleteColByNameTest() {
		board.deleteChosen("name");
	}

}

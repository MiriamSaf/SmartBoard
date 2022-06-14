package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.UserHolder;
import javafx.scene.image.ImageView;
import model.Board;
import model.Column;

//manages the board connection with the database
public class BoardDataBaseManager {

	UserDataBaseManager db = new UserDataBaseManager();

	// adapted from sqlite example
	// https://github.com/xerial/sqlite-jdbc - sample.java
	public void tryCreateBoardDB() {
		Connection connection = null;

		// declare final string with name of user database
		final String TABLE_USER_NAME = "board";
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:user.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			boolean tableUserExists = db.checkTableExistsFirst(TABLE_USER_NAME);
			// boolean tableUserExists = false;

			// if board table doesn't exist then create it with some boards - attached to FK
			// user table
			if (!tableUserExists) {

				statement.executeUpdate("drop table if exists board");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_USER_NAME
						+ "(username string(15) NOT NULL," + "boardname string(20) NOT NULL," + "isdefault int(1),"
						+ "boardnum int(4)," + "PRIMARY KEY (username, boardname)," + "FOREIGN KEY (username) "
						+ "REFERENCES user(username))");

				statement.executeUpdate("insert into board values('miri', 'To-do', 1, 0)");
				statement.executeUpdate("insert into board values('miri', 'doing', 0, 1)");
				ResultSet rs = statement.executeQuery("select * from board");
				while (rs.next()) {
					// read the result set
				}
			}
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				// connection close failed.
				System.err.println(e.getMessage());
			}
		}

	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// insert a new row into the database
	public void insertRowIntoBoardTable(String Username, String boardname, int defbord, int boardNum) {
		final String TABLE_NAME = "board";
		final String username = Username;
		final String Board = boardname;
		final int defaulTb = defbord;
		final int BOARD_NUM = boardNum;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + username + "', '" + boardname + "', '"
					+ defaulTb + "', '" + BOARD_NUM + "')";

			int result = stmt.executeUpdate(query);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// update board name by passing in old board name and setting new board name
	public void updateBoardName(String oldBoardnm, String newBname, String uname) {
		final String TABLE_NAME = "board";
		final String username = uname;
		final String oldBoardName = oldBoardnm;
		final String newBoardName = newBname;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "UPDATE " + TABLE_NAME + " SET boardname = '" + newBoardName + "'" + " WHERE username LIKE '"
					+ username + "'" + " AND boardname = '" + oldBoardName + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// update default status and set to true or faslse
	public void updateSetDefaultBoard(String boardName, String uname, int boolStat) {
		final String TABLE_NAME = "board";
		final String username = uname;
		final String boardname = boardName;
		final int setBool = boolStat;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "UPDATE " + TABLE_NAME + " SET isDefault = '" + setBool + "'" + " WHERE username LIKE '"
					+ username + "'" + " AND boardname = '" + boardname + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 materials from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// delets chosen row from the DB board table - id username and boardname match
	// board is deleted
	public void deleteRowFromBoardTable(String username, String boardName) {
		final String TABLE_NAME = "board";
		final String USER_NAME = username;
		final String BOARD_NAME = boardName;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE username LIKE '" + USER_NAME + "'" + "AND boardname = '"
					+ BOARD_NAME + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// update select query for the board and loads it into the users boards
	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	public void selectBoardDBQuery(UserHolder user) {

		final String TABLE_NAME = "board";

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "SELECT * FROM " + TABLE_NAME;

			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while (resultSet.next()) {
					// it is the users boards
					if (resultSet.getString("username").equals(user.getUser().getUserName())) {
						String uname = resultSet.getString("username");
						String boardName = resultSet.getString("boardname");
						int defaultBool = resultSet.getInt("isdefault");
						int boardNum = resultSet.getInt("boardnum");

						ArrayList<Column> column = new ArrayList<Column>();
						Board board = new Board(boardName, column);

						if (defaultBool == 1) {
							// set it as the default board
							board.setDefault();
						}
						// add board to the list of boards for this user
						user.getUser().getWorkspace().addBoardToList(board);
					}

				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}

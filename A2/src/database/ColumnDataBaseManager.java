package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.UserHolder;
import model.Board;
import model.Column;
import model.Task;

public class ColumnDataBaseManager {

	UserDataBaseManager db = new UserDataBaseManager();

	// adapted from sqlite example
	// https://github.com/xerial/sqlite-jdbc - sample.java
	public void tryCreateColumnDB() {
		Connection connection = null;

		// declare final string with name of user database
		final String TABLE_USER_NAME = "column";
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:user.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			boolean tableUserExists = db.checkTableExistsFirst(TABLE_USER_NAME);
			// if board table doesn't exist then create it with some boards - attached to FK
			// user table
			if (!tableUserExists) {

				statement.executeUpdate("drop table if exists column");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS " + TABLE_USER_NAME + "(username string(15) NOT NULL,"
								+ "boardnum int(4) NOT NULL," + "columname string(20) NOT NULL,"
								+ "colnum int(4) NOT NULL," + "PRIMARY KEY (username, boardnum, columname),"
								+ "FOREIGN KEY (boardnum) REFERENCES board(boardnum),"
								+ "FOREIGN KEY (username) REFERENCES user(username))");

				statement.executeUpdate("insert into column values('miri', '1', 'supper', 0)");
				statement.executeUpdate("insert into column values('miri', '0', 'excercise', 0)");

				ResultSet rs = statement.executeQuery("select * from column");
				while (rs.next()) {
					// read the result set if necessary for debugging
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
	// insert a new row into the database for column
	public void insertRowIntoColTable(String Username, int boardnum, String colname, int colnum) {
		final String TABLE_NAME = "column";
		final String username = Username;
		final int Board = boardnum;
		final String columName = colname;
		final int COL_NUM = colnum;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + username + "', '" + Board + "', '" + colname
					+ "', '" + COL_NUM + "')";

			int result = stmt.executeUpdate(query);

			if (result == 1) {
				// success
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// update board name by passing in old board name and setting new board name
	public void updateColName(String oldColnm, String newColname, String uname, int bnum) {
		final String TABLE_NAME = "column";
		final String username = uname;
		final String oldColName = oldColnm;
		final String newColName = newColname;
		final int boardNum = bnum;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "UPDATE " + TABLE_NAME + " SET columname = '" + newColName + "'" + " WHERE columname LIKE '"
					+ oldColName + "'" + " " + "AND boardnum LIKE '" + boardNum + "'" + "AND username LIKE '" + username
					+ "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 materials from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// delets chosen row from the DB
	public void deleteRowFromColTable(String username, int boardNum, String columname) {
		final String TABLE_NAME = "column";
		final String USER_NAME = username;
		final int BOARD_NAME = boardNum;
		final String COL_NAME = columname;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE username LIKE '" + USER_NAME + "'"
					+ "AND boardnum LIKE '" + BOARD_NAME + "'" + "AND columname LIKE '" + COL_NAME + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 materials from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// delets chosen row from the DB
	public void deleteRowFromColKnowBoard(String username, int boardNum) {
		final String TABLE_NAME = "column";
		final String USER_NAME = username;
		final int BOARD_NAME = boardNum;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE username LIKE '" + USER_NAME + "'"
					+ "AND boardnum LIKE '" + BOARD_NAME + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// update select query for the column
	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	public void selectColDBQuery(UserHolder user) {

		final String TABLE_NAME = "column";

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "SELECT * FROM " + TABLE_NAME;

			try (ResultSet resultSet = stmt.executeQuery(query)) {
				// go through the result set one by one
				while (resultSet.next()) {
					String uname = resultSet.getString("username");
					int boardNum = resultSet.getInt("boardnum");
					String colName = resultSet.getString("columname");
					// int taskAmt = resultSet.getInt("taskinc");
					// it is the users boards
					// if the username matches with current user
					if (resultSet.getString("username").equals(user.getUser().getUserName())) {
						// get their number of boards
						int size = user.getUser().getWorkspace().getBoards().size();
						for (int i = 0; i < size; i++) {
							// for each board if it equals
							if (i == boardNum) {
								ArrayList<Task> tasks = new ArrayList<Task>();
								Column col = new Column(colName, tasks);

								// add this column to the users board
								user.getUser().getWorkspace().getBoards().get(i).addColumnToBoard(col);

							}

						}
					}

				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}

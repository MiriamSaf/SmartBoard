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

public class TaskDataBaseManager {

	UserDataBaseManager db = new UserDataBaseManager();

	// adapted from sqlite example
	// https://github.com/xerial/sqlite-jdbc - sample.java
	public void tryCreateTaskDB() {
		Connection connection = null;

		// declare final string with name of user database
		final String TABLE_USER_NAME = "task";
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

				statement.executeUpdate("drop table if exists task");
				statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + TABLE_USER_NAME
						+ "(username string(15) NOT NULL," + "boardnum int(4) NOT NULL," + "colnum int(4) NOT NULL,"
						+ "taskname string(20) NOT NULL," + "taskdescript string(100) NOT NULL,"
						+ "PRIMARY KEY (username, boardnum, colnum, taskname),"
						+ "FOREIGN KEY (colnum) REFERENCES column(colnum),"
						+ "FOREIGN KEY (boardnum) REFERENCES board(boardnum),"
						+ "FOREIGN KEY (username) REFERENCES user(username))");

				statement.executeUpdate(
						"insert into task values('miri', 0, 1, 'make chicken', 'chicken with rice and veg')");
				statement.executeUpdate(
						"insert into task values('miri', 0,1, '20 min warmup', 'listen to music and warmup excerise')");

				ResultSet rs = statement.executeQuery("select * from task");
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
	public void insertRowIntoTaskTable(String Username, int boardnum, int colnum, String taskname, String taskDesc) {
		final String TABLE_NAME = "task";
		final String USER_NAME = Username;
		final int BOARD_NUM = boardnum;
		final int COLUMN_NUM = colnum;
		final String TASK_NAME = taskname;
		final String TASK_DESC = taskDesc;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + USER_NAME + "', '" + BOARD_NUM + "', '"
					+ COLUMN_NUM + "','" + TASK_NAME + "', '" + TASK_DESC + "')";

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
	public void updateTaskNameAndDesc(String oldTaskName, String newTaskName, String uname, int boardNum, int colNum,
			String desc) {
		final String TABLE_NAME = "task";
		final String USER_NAME = uname;
		final int BOARD_NUM = boardNum;
		final int COLUMN_NUM = colNum;
		final String NEW_TASK_NAME = newTaskName;
		final String OLD_TASK_NAME = oldTaskName;
		final String NEW_DESC = desc;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "UPDATE " + TABLE_NAME + " SET taskname = '" + NEW_TASK_NAME + "'" + ", taskdescript = '"
					+ NEW_DESC + "'" + " WHERE taskname LIKE '" + OLD_TASK_NAME + "'" + " " + "AND boardnum LIKE '"
					+ BOARD_NUM + "'" + "AND colnum LIKE '" + COLUMN_NUM + "'" + "AND username LIKE '" + USER_NAME
					+ "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// update board name by passing in old board name and setting new board name
	public void updateTaskColNum(String taskName, String uname, int boardNum, int colNumOld, int colNumNew) {
		final String TABLE_NAME = "task";
		final String USER_NAME = uname;
		final int BOARD_NUM = boardNum;
		final int COLUMN_NUM_OLD = colNumOld;
		final int COL_NEW_NUM = colNumNew;
		final String TASK_NAME = taskName;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "UPDATE " + TABLE_NAME + " SET colnum = '" + COL_NEW_NUM + "'" + " WHERE taskname LIKE '"
					+ TASK_NAME + "'" + " " + "AND boardnum LIKE '" + BOARD_NUM + "'" + "AND colnum LIKE '"
					+ COLUMN_NUM_OLD + "'" + "AND username LIKE '" + USER_NAME + "'";

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
	public void deleteRowFromTaskTable(String username, int boardNum, int columnum, String taskName) {
		final String TABLE_NAME = "task";
		final String USER_NAME = username;
		final int BOARD_NUM = boardNum;
		final int COL_NUM = columnum;
		final String TASK_NAME = taskName;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE username LIKE '" + USER_NAME + "'"
					+ "AND taskname LIKE '" + TASK_NAME + "'" + "AND boardnum LIKE '" + BOARD_NUM + "'"
					+ "AND colnum LIKE '" + COL_NUM + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
				System.out.println("Delete from table " + TABLE_NAME + " executed successfully");
				System.out.println(result + " row(s) affected");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 materials from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// delets chosen row from the DB
	public void deleteRowFromTaskKnowCol(String username, int boardNum, int columnum) {
		final String TABLE_NAME = "task";
		final String USER_NAME = username;
		final int BOARD_NUM = boardNum;
		final int COL_NUM = columnum;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE username LIKE '" + USER_NAME + "'"
					+ "AND boardnum LIKE '" + BOARD_NUM + "'" + "AND colnum LIKE '" + COL_NUM + "'";

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
	public void deleteRowFromTaskKnowBoard(String username, int boardNum) {
		final String TABLE_NAME = "task";
		final String USER_NAME = username;
		final int BOARD_NUM = boardNum;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE username LIKE '" + USER_NAME + "'"
					+ "AND boardnum LIKE '" + BOARD_NUM + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
				System.out.println("Delete from table " + TABLE_NAME + " executed successfully");
				System.out.println(result + " row(s) affected");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// populates list with tasks for each column
	// update select query for the column
	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	public void selectColDBTaskQuery(UserHolder user) {

		final String TABLE_NAME = "task";

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "SELECT * FROM " + TABLE_NAME;

			try (ResultSet resultSet = stmt.executeQuery(query)) {
				// go through the result set one by one
				while (resultSet.next()) {
					String uname = resultSet.getString("username");
					int boardNum = resultSet.getInt("boardnum");
					int colNum = resultSet.getInt("colnum");
					String taskName = resultSet.getString("taskname");
					String taskDescript = resultSet.getString("taskdescript");
					// it is the users boards
					// if the username matches with current user
					int boardSize = user.getUser().getWorkspace().getBoards().size();
					String usname = user.getUser().getUserName();
					if (resultSet.getString("username").equals(usname)) {
						// loop through users boards
						for (int i = 0; i < boardSize; i++) {
							// if the board number matches with result set boardnum
							if (boardNum == i) {
								int colSize = user.getUser().getWorkspace().getBoards().get(i).getColumn().size();
								// loop through cols
								for (int y = 0; y < colSize; y++) {
									if (colNum == y) {
										// get all the tasks and save in a list

										ArrayList<Task> listTask = new ArrayList<Task>();
										Task task = new Task(taskName, taskDescript);
										listTask.add(task);

										user.getUser().getWorkspace().getBoards().get(i).getColumn().get(y)
												.addTaskToBoard(task);
									}
								}

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

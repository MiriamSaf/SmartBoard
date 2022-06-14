package database;

import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.UserHolder;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Authenticator;
import model.DefaultUser;
import model.User;

public class UserDataBaseManager {

	// adapted from sqlite example
	// https://github.com/xerial/sqlite-jdbc - sample.java
	public void tryConnect() {
		Connection connection = null;

		// declare final string with name of user database
		final String TABLE_USER_NAME = "user";
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:user.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			boolean tableUserExists = checkTableExistsFirst(TABLE_USER_NAME);
			// boolean tableUserExists =false;

			// if user table doesn't exist then create it with some users
			if (!tableUserExists) {

				statement.executeUpdate("drop table if exists user");
				statement.executeUpdate(
						"CREATE TABLE IF NOT EXISTS " + TABLE_USER_NAME + "(username string(15) NOT NULL,"
								+ "firstname string(15) " + "NOT NULL," + "surname string(20) NOT NULL,"
								+ "password VARCHAR(30) NOT NULL," + "image String(200)," + "PRIMARY KEY (username))");
				statement.executeUpdate("insert into user values('miri', 'miriam', 'saf', 'miris', 'j')");
				statement.executeUpdate("insert into user values('test', 'tester', 't', 'test', 'h')");
				ResultSet rs = statement.executeQuery("select * from user");
				while (rs.next()) {
					// read the result set
					/*
					 * System.out.println("username = " + rs.getString("username"));
					 * System.out.println("firstname = " + rs.getString("firstname"));
					 * System.out.println("surname = " + rs.getString("surname"));
					 * System.out.println("password = " + rs.getString("password"));
					 */
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

	// adapted from week 9 sqlitedemo materials from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// checks if DB exists first
	public boolean checkTableExistsFirst(String tablename) {
		final String TABLE_NAME = tablename;
		boolean tableExist = false;

		try (Connection con = DatabaseConnection.getConnection()) {

			DatabaseMetaData dbm = con.getMetaData();

			ResultSet tables = dbm.getTables(null, null, TABLE_NAME, null);

			if (tables != null) {
				if (tables.next()) {
					// the table exists
					tableExist = true;
				} else {
					// table doesnt exist
					tableExist = false;

				}
				tables.close(); // use close method to close ResultSet object
				return tableExist;
			} else {
				System.out.println("Problem with retrieving database metadata");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return tableExist;
	}

	// adapted from week 9 materials from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// delets chosen row from the DB
	public void deleteRowFromTable(String tableName, String tableField, String tableNameIdentifier) {
		final String TABLE_NAME = tableName;
		final String TABLE_FIELD = tableField;
		final String TABLE_ITEM = tableNameIdentifier;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_FIELD + " LIKE '+TABLE_ITEM+'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
				System.out.println("Delete from table " + TABLE_NAME + " executed successfully");
				System.out.println(result + " row(s) affected");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// Deletes a table from the database
	public void deleteTable(String tableName) {
		final String TABLE_NAME = tableName;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			stmt.executeUpdate("DROP TABLE IF EXISTS " + TABLE_NAME);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// insert a new row into the database
	public void insertRowIntoChosenTable(String Username, String firstName, String surName, String password,
			String imgname) {

		final String TABLE_NAME = "user";
		final String username = Username;
		final String firstname = firstName;
		final String surname = surName;
		final String pwd = password;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "INSERT INTO " + TABLE_NAME + " VALUES ('" + username + "', '" + firstname + "', '" + surname
					+ "', '" + pwd + "', '" + imgname + "')";
			// + defaulTb + "', '"+ BOARD_NUM+"')";

			int result = stmt.executeUpdate(query);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	// update user image
	public void updateImageUsernameTable(String uname, String imgstring) {
		final String TABLE_NAME = "user";
		final String username = uname;
		final String IMAGE_STR = imgstring;

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "UPDATE " + TABLE_NAME + " SET image = '" + IMAGE_STR + "'" + " WHERE username LIKE '"
					+ username + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// update user table surname
	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	public void updateUserSurnameFirstNameTable(String sname, String fname, String uname) {
		final String TABLE_NAME = "user";
		final String username = uname;
		final String surname = sname;
		final String firstname = fname;

		// update the surname and firstname in one query
		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String sql = "UPDATE " + TABLE_NAME + " SET surname = '" + surname + "'" + " , firstname = '" + fname + "'"
					+ " WHERE username LIKE '" + username + "'";

			int result = stmt.executeUpdate(sql);

			if (result == 1) {
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// update select query surname
	// adapted from week 9 sqlitedemo material zip folder from canvas
	// https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
	public void selectUserDBQuery(UserHolder user) {

		final String TABLE_NAME = "user";

		try (Connection con = DatabaseConnection.getConnection(); Statement stmt = con.createStatement();) {
			String query = "SELECT * FROM " + TABLE_NAME;

			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while (resultSet.next()) {
					String uname = resultSet.getString("username");
					String fname = resultSet.getString("firstname");
					String surname = resultSet.getString("surname");
					String pwd = resultSet.getString("password");
					String image = resultSet.getString("image");

					if (image != null) {
						DefaultUser deUser = new DefaultUser(pwd, uname, fname, surname, image);
						deUser.setImageString(image);
						Authenticator auth = user.getAuth();
						auth.addUser(deUser);
					} else {
						DefaultUser deUser = new DefaultUser(pwd, uname, fname, surname, "/default.jpg");
						Authenticator auth = user.getAuth();
						auth.addUser(deUser);
					}
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

}

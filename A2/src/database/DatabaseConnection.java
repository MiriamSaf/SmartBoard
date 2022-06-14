package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//adapted from week 9 sqlitedemo materials from canvas
//https://rmit.instructure.com/courses/96668/pages/week-9-pre-class-activities?module_item_id=3962984
public class DatabaseConnection {

	private static final String DB_URL = "jdbc:sqlite:user.db";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}
}

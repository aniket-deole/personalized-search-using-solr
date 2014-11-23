package org.zeppelin.p3.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

// Copied from <br>
// http://www.vogella.com/tutorials/MySQLJava/article.html

public class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public void readDataBase() throws Exception {
		try {

			// statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement
					.executeQuery("select * from ub535p3.user_master");
			writeResultSet(resultSet);

			// // preparedStatements can use variables and are more efficient
			// preparedStatement = connect
			// .prepareStatement("insert into  FEEDBACK.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
			// //
			// "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
			// // parameters start with 1
			// preparedStatement.setString(1, "Test");
			// preparedStatement.setString(2, "TestEmail");
			// preparedStatement.setString(3, "TestWebpage");
			// preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
			// preparedStatement.setString(5, "TestSummary");
			// preparedStatement.setString(6, "TestComment");
			// preparedStatement.executeUpdate();

			preparedStatement = connect
					.prepareStatement("SELECT user_id, name, category_id, tags, doc_id from ub535p3.user_master");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	public void updateUserCategoryPreferences(int userId, String categoryName)
			throws Exception {
		try {
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/ub535p3?"
							+ "user=mysqluser&password=justarandompassword");
			// statements allow to issue SQL queries to the database
			// // preparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("SELECT category_id from ub535p3.category_master where name=?");
			preparedStatement.setString(1, categoryName);
			resultSet = preparedStatement.executeQuery();
			int categoryId = 0;
			while (resultSet.next()) {
				categoryId = resultSet.getInt("category_id");
			}
			preparedStatement = connect
					.prepareStatement("insert into  ub535p3.user_category_map values (?, ?)");
			// preparedStatement = connect.prepareStatement("INSERT");
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, categoryId);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			;
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	public ArrayList<String> fetchPreferredCategories(int userId)
			throws Exception {
		try {
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/ub535p3?"
							+ "user=mysqluser&password=justarandompassword");
			// statements allow to issue SQL queries to the database
			preparedStatement = connect
					.prepareStatement("SELECT cm.name from ub535p3.user_category_map ucm, ub535p3.category_master cm where user_id=? AND ucm.category_id=cm.category_id");
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			ArrayList<String> preferredCartegories = new ArrayList<String>();
			while (resultSet.next()) {
				preferredCartegories.add("category:"
						+ resultSet.getString("name"));
			}
			return preferredCartegories;

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// resultSet is initialised before the first data set
		while (resultSet.next()) {
			// it is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g., resultSet.getSTring(2);
			int user = resultSet.getInt("user_id");
			String website = resultSet.getString("name");
			int summary = resultSet.getInt("category_id");
			String date = resultSet.getString("tags");
			String comment = resultSet.getString("doc_id");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summary: " + summary);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

	// you need to close all three to make sure
	private void close() {
		close(resultSet);
		close(statement);
		close(connect);
	}

	private void close(AutoCloseable c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			// don't throw now as it might leave following closeables in
			// undefined state
		}
	}

}
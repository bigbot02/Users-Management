package com.xadmin.usermanagment.dao;

import com.xadmin.usermanagment.bean.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
private String jdbcURL = "jdbc:mysql://127.0.0.1:3306/userdb?useSSL=false";
private String jdbcUsername = "root";
private String jdbcPassword = "admin";
private String jdbcDriver = "com.mysql.jdbc.Driver";

private static final String INSERT_USERS_SQL = "INSERT INTO users" + "(name, email, country) VALUES" + " (?, ?, ?);";
private static final String SELECT_USER_BY_ID = "select id,name,email,country from users when id =?";
private static final String SELECT_ALL_USERS = "select * from users";
private static final String DELETE_USERS_SQL = "delete from users when id =?";
private static final String UPDATE_USERS_SQL = "update users set name = ?, email = ?, country = ? where id =?;";

public UserDao(){
}

protected Connection getConnection() {
	Connection connection = null;
	try {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	return connection;
}


// Insert User
public void insertUser(User user) throws SQLException {
	System.out.println(INSERT_USERS_SQL);
	try (Connection connection = getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)){
		preparedStatement.setString(1, user.getName());
		preparedStatement.setString(2, user.getEmail());
		preparedStatement.setString(3, user.getCountry());
        System.out.println(preparedStatement);
        preparedStatement.executeUpdate();
	} catch (SQLException e) {
		printSQLException(e);
	}
}


// Select User by ID
public User selectUser(int id) {
	User user = null;
	// 1st Step : Establishing a Connection
	try (Connection connection = getConnection();
			// 2nd Step : Create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
		preparedStatement.setInt(1, id);
		System.out.println(preparedStatement);
		// 3rd Step : Execute the query or update query
		ResultSet rs = preparedStatement.executeQuery();
		
		// 4th Step : Process the resultSet object
		while (rs.next()) {
			String name = rs.getString("name");
			String email = rs.getString("email");
			String country = rs.getString("country");
			user = new User(id, name, email, country);
		}
	} catch (SQLException e) {
		printSQLException(e);
	}
	return user;
}

// Select All Users
public List<User> SelectAllUsers(){
 List<User> users = new ArrayList<>();
 // 1st Step : Establishing Connection
	try (Connection connection = getConnection();
// 2nd Step : Create a Statement using connection object
			
    PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
		System.out.println(preparedStatement);
		// 3rd Step : Execute the query or update query
		ResultSet rs = preparedStatement.executeQuery();
		 // 4th Step : Process the ResultSet Object
		while (rs.next()) {
		   int id = rs.getInt("id");
		    String name = rs.getString("name");
			String email = rs.getString("email");
			String country = rs.getString("country");
			users.add(new User(id, name, email, country));
		   }
		} catch (SQLException e) {
			printSQLException(e);
		}
	return users;
	}

// Update User
public boolean updateUser(User user) throws SQLException {
    boolean rowUpdated = false; // Initialize to false by default
    try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getCountry());
        statement.setInt(4, user.getId());
        
        rowUpdated = statement.executeUpdate() > 0;
    }
    return rowUpdated;
}


// Delete User
public boolean deleteUser(int id) throws SQLException {
	boolean rowDeleted;
	try (Connection connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
		statement.setInt(1, id);
		rowDeleted = statement.executeUpdate() > 0;
	}
	return rowDeleted;
}





private void printSQLException(SQLException ex) {
	for (Throwable e : ex) {
		if (e instanceof SQLException) {
			e.printStackTrace(System.err);
			System.err.println("SQLState:" + ((SQLException) e).getSQLState());
			System.err.println("Error Code" + ((SQLException) e).getErrorCode());
			System.err.println("Message" + e.getMessage());
            Throwable t = ex.getCause();
            while (t != null) {
            	System.out.println("Cause: " + t);
            	t = t.getCause();
            }
		}
	}
}
}

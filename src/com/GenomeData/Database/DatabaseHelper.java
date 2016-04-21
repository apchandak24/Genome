package com.GenomeData.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseHelper {
	
	/**
	 * Get the Database connection object
	 */
	public static Connection getConnection(Properties properties) {
		try {
			
			String dbName = properties.getProperty("database");
			String dbUser = properties.getProperty("dbuser");
			String dbPassword = properties.getProperty("dbpassword");
			String connectionURL = "jdbc:mysql://localhost:3306/"+dbName;
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, dbUser,dbPassword);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


}

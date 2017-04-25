package com.gcit.javabasics.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class jdbcConnection {
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public static String username = "root";
	public static String password = "Future123$";
	
	public static Connection conn = null;
	
	public static Connection getConnection()	{
		try {
			Class.forName(driver);
			System.out.println("Getting connection");
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection: yes");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void closeConnection()	{
		if (conn != null)	{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}

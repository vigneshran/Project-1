package com.gcit.javabasics.core;
import java.sql.*;
import java.util.Scanner;

public class JDBCDemo {
	
	public static String driver = "com.mysql.jdbc.Driver";
	public static String url = "jdbc:mysql://localhost/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public static String username = "root";
	public static String password = "Future123$";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		String query = "";
		
		try {
			Class.forName(driver);
			System.out.println("Getting connection");
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connection: yes");
			stmt = conn.createStatement();
			query = "SELECT title FROM tbl_book;";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getString("title"));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

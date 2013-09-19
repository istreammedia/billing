package org.mifosplatform.scheduledjobs.service;
 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;
 
public class JDBCExample {
 
  public static void main(String[] argv) {
 
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
 
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return;
	}
 
	System.out.println("MySQL JDBC Driver Registered!");
	Connection connection = null;
 
	try {
		connection = DriverManager
		.getConnection("jdbc:mysql://localhost:3306/mifostenant-default","root", "mysql");
		
		java.sql.Statement st = connection.createStatement();
		String sql = ("SELECT * FROM b_orders");
		
		ResultSet rs = st.executeQuery(sql);
		if(rs.next()) { 
		 int id = rs.getInt("id"); 
		 String str1 = rs.getString("transaction_type");
		 ResultSetMetaData rsmd = rs.getMetaData();
		 System.out.println(rsmd);
		}

           
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}
 
	if (connection != null) {
		System.out.println("You made it, take control your database now!");
	} else {
		System.out.println("Failed to make connection!");
	}
  }
}
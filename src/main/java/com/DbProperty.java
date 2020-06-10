package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbProperty {
    public static Connection getConnection() {
//		Properties properties = new Properties();
//		InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("/jdbc.properties");
        Connection conn = null;
        try {
//			properties.load(ins);
//			String driver = properties.getProperty("JDBC_DRIVER");
//			String url = properties.getProperty("JDBC_URL");
//			String username = properties.getProperty("JDBC_USERNAME");
//			String password = properties.getProperty("JDBC_PASSWORD");
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/study?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8";
            String username = "root";
            String password = "";
//            String url = "jdbc:mysql://58.221.142.74:37306/ntjz?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=GMT%2B8";
//            String username = "ntjz";
//            String password = "Ntjz@123";
//			ins.close();
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
//		} catch (IOException e1) {
//			e1.printStackTrace();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}

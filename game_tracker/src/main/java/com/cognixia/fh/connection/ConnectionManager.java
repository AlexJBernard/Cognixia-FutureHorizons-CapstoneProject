package com.cognixia.fh.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

  private static final String FILENAME = "game_tracker/app.config";

  public static Properties prop = new Properties();

  private static String url;

  private static String username;

  private static String password;

  private static Connection connection = null;

  /**
   * Initializes a connection to the games database.
   * 
   * @return A Connection object containing a 
   * @throws ClassNotFoundException If the correct java plugin is not installed, the project will throw an error
   * @throws SQLException Throws an exception if a connection to the SQL database cannot be opened
   */
  public static Connection makeConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {

    try (FileInputStream fis = new FileInputStream(FILENAME)) {
      prop.load(fis);
    } catch (Exception e) {
      throw e;
    }
    
    Class.forName("com.mysql.cj.jdbc.Driver");

    url = prop.getProperty("database_url");
    username = prop.getProperty("username");
    password = prop.getProperty("password");

    connection = DriverManager.getConnection(url, username, password);

    return connection;
  }

  public static Connection getConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {

    if (connection == null) {
      makeConnection();
    }

    return connection;
  }

  public static void main(String[] args) {
    try (Connection connection = ConnectionManager.getConnection()) {
      System.out.println("CONNECTED");
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    
  }
}

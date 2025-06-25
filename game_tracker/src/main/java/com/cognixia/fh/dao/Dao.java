package com.cognixia.fh.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface Dao {

  /**
   * Establishes a connection with the MySQL database
   * 
   * @throws ClassNotFoundException The application does not contain the appropriate maven library.
   * @throws SQLException The application cannot connect to the MySQL database.
   * @throws FileNotFoundException The user has not implemented the app.config file correctly
   * @throws IOException An error occured with the FileInputStream class
   */
  public void establishConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException ;

  /**
   * Closes the connection to the MySQL datbase
   * @throws SQLException
   */
  public void closeConnection() throws SQLException;
}

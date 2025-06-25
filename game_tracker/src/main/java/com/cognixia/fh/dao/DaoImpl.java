package com.cognixia.fh.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.cognixia.fh.connection.ConnectionManager;

public abstract class DaoImpl implements Dao {

  protected Connection connection = null;
  
  
  @Override
  public void establishConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {

    if (connection == null) {
      connection = ConnectionManager.getConnection();
    }
  }

  @Override
  public void closeConnection() throws SQLException {
    if (connection != null) {
      connection.close();
    }
  }
}

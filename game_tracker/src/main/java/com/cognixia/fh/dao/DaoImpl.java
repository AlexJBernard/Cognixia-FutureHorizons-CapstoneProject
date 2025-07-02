package com.cognixia.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cognixia.fh.connection.ConnectionManager;

public abstract class DaoImpl implements Dao {

  private final ConnectionManager CONNECTIONS = ConnectionManager.getInstance();

  private Connection connect;

  @Override 
  public PreparedStatement openStatement(String query) {

    connect = CONNECTIONS.getConnection();
    
    try {
      return connect.prepareStatement(query);
    } catch( SQLException e) {
      System.out.println("COULD NOT CREATE STATEMENT");
      e.printStackTrace(System.out);
      return null;
    }
  }

  @Override 
  public boolean closeStatement() {
    if (connect != null) {
      return CONNECTIONS.releaseConnection(connect);
    } else {
      return false;
    }
  }
}

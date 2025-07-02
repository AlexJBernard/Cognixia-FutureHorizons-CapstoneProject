package com.cognixia.fh.dao;

import java.sql.PreparedStatement;

public interface Dao {

  /** 
   * Executes the chosen query. Releases Database connection once the operation has concluded.
   * @param stmnt The PreparedStatement being executed.
   * @return A prepared statement created from the 
   */
  public PreparedStatement openStatement(String query);

  /**
   * Closes the DAO's current connection
   * @return True if the connection was closed
   */
  public boolean closeStatement();
}

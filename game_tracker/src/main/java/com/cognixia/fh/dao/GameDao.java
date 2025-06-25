package com.cognixia.fh.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.cognixia.fh.dao.model.Game;

public interface GameDao {

  /**
   * Establishes a connection with the MySQL database
   * 
   * @throws ClassNotFoundException The application does not contain the appropriate maven library.
   * @throws SQLException The application cannot connect to the MySQL database.
   */
  public void establishConnection() throws ClassNotFoundException, SQLException;

  /**
   * Closes the connection to the MySQL datbase
   * @throws SQLException
   */
  public void closeConnection() throws SQLException;

  /**
   * Gets the game with a specific ID from the database
   * @return An optional containing the game with the corresponding id, if it exists
   */
  public Optional<Game> getById(int id);

  /**
   * Gets all games within a specific generation
   * @param generation The specific game generation
   * @return A list of all games released within the specified generation
   */
  public List<Game> getByGeneration(int generation);
}

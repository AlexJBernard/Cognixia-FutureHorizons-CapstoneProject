package com.cognixia.fh.dao;

import java.util.List;
import java.util.Optional;

import com.cognixia.fh.dao.model.Game;
import com.cognixia.fh.dao.model.GameEntry;
import com.cognixia.fh.dao.model.User;

public interface GameEntryDao extends Dao {

  /**
   * Gets the GameEntry with the specified ID.
   * @param id The GameEntry's database id.
   * @return The GameEntry if it exists.
   */
  public Optional<GameEntry> getById(int id);

  /**
   * Creates a list of GameEntries with the same id as the given user.
   * 
   * @param user The User whose id is being compared.
   * @return A complete list of game entries owned by the specified user.
   */
  public List<GameEntry> getByOwnerId(User user);

  /**
   * Creates a list of GameEntries with the same gameId as the given game.
   * @param game The Game whose id is being compared.
   * @return A complete list of game entries with the specified game id.
   */
  public List<GameEntry> getByGameId(Game game);
}

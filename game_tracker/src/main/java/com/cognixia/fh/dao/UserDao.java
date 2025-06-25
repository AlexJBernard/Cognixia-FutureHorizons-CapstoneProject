package com.cognixia.fh.dao;

import java.util.Optional;

import com.cognixia.fh.dao.model.User;

public interface UserDao {

  /**
   * Gets the user with a specific ID from the database
   * @return An optional containing the game with the corresponding id, if it exists
   */
  public Optional<User> getById(int id);
}

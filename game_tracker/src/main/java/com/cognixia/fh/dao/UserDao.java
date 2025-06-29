package com.cognixia.fh.dao;

import java.util.Optional;

import com.cognixia.fh.dao.model.User;

public interface UserDao {

  /**
   * Gets the user with a specific ID from the database
   * @return An optional containing the game with the corresponding id, if it exists
   */
  public Optional<User> getById(int id);

  /**
   * Searches for auser with the given username
   * @param username The username being searched for
   * @return A full list of all users with the same username
   */
  public Optional<User> findByUsername(String username);

  /**
   * Attempts to add the given user to the database
   * @param user The user being added to the database
   * @return
   */
  public boolean createUser(User user);
}

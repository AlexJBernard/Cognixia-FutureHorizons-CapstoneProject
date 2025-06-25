package com.cognixia.fh.dao.model;

/**
 * Model class used for users stored in the database
 */
public class User {

  /**
   * User id specified in database
   */
  private int id;

  /**
   * The User's username
   */
  private String username;

  /**
   * The User's password
   */
  private String password;

  /**
   * Constructor for user data fetched from the database
   * @param id The database id
   * @param username The user username
   * @param password The user password
   */
  public User(int id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  /**
   * Constructor for creating a new User to add to database. Omits the id field.
   * @param username The new user's username
   * @param password The new user's password
   */
  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Gets the user's id
   * @return The user id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the user's username
   * @return The user's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the user's username, provided it's not null and has a length greater than 0
   * @param username The user's new username
   */
  public void setUsername(String username) {
    if (username != null && username.length() > 0) {
      this.username = username;
    }
  }

  /**
   * Gets the user's password
   * @return The user's password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's password, provided it's not null and has a length greater than 0
   * @param password The user's new password
   */
  public void setPassword(String password) {
    if (password != null && password.length() > 0) {
      this.password = password;
    }
  }

  

}

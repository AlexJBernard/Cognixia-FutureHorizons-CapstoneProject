package com.cognixia.fh.account;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.cognixia.fh.dao.GameDaoImpl;
import com.cognixia.fh.dao.UserDaoImpl;
import com.cognixia.fh.dao.model.Game;
import com.cognixia.fh.dao.model.GameEntry;
import com.cognixia.fh.dao.model.User;

/**
 * Collects all information associated with the given user
 */
public class AccountManager {
  /** The user that's currently logged in */
  private static User currentUser;

  /** List of games owned by the current user */
  private static List<GameEntry> ownedGames;

  /** An array of max length 10 arrays listing every game not owned by the user */
  private static Game[][] unownedGames;

  private static final UserDaoImpl USER_DAO = new UserDaoImpl();

  private static final GameDaoImpl GAME_DAO = new GameDaoImpl();


  public static boolean validateUsername(String username) {

    boolean userFound = false;

    try {
      USER_DAO.establishConnection();

      Optional<User> response = USER_DAO.findByUsername(username);
      userFound = !response.isEmpty();

      USER_DAO.closeConnection();
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    
    return userFound;
  }

  /**
   * Attempts to login the user with the provided credentials
   * @param username The user's username
   * @param password The user's password
   */
  public static void attemptLogin(String username, String password) {
    User user = null;
    try {
        USER_DAO.establishConnection();

        user = USER_DAO.findByUsername(username).get();

        USER_DAO.closeConnection();
        
    } catch (ClassNotFoundException e) {
      System.out.println("Maven MySQL package not installed!");
    } catch (SQLException e) {
      System.out.println("Error connecting to SQL Database");
    }catch (Exception e) {
      e.printStackTrace(System.out);
    }

    if (user != null && user.getPassword().equals(password)) {
      currentUser = user;
      System.out.println("LOGIN SUCCESS");
    }
  }

  /**
   * Shows if the user is currently logged in
   * @return
   */
  public static boolean isLoggedIn() {
    return currentUser == null;
  }

  /**
   * Returns the current user
   * @return
   */
  public static User getCurrentUser() {
    return currentUser;
  }

  /**
   * Lists all games not owned by the current user
   * @return
   */
  public static Game[][] getUnownedGames() {
    return unownedGames;
  }

  /**
   * 
   * @return
   */
  public static List<GameEntry> ownedGames() {
    return ownedGames;
  }

  public static void logout() {
    if (AccountManager.isLoggedIn()) {
      currentUser = null;
    }
  }


}

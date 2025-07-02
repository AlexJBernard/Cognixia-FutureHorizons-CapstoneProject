package com.cognixia.fh.account;

import java.util.List;
import java.util.Optional;

import com.cognixia.fh.dao.GameDaoImpl;
import com.cognixia.fh.dao.GameEntryDaoImpl;
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

  private static final GameEntryDaoImpl GAME_ENTRY_DAO = new GameEntryDaoImpl();


  public static boolean validateUsername(String username) {

    Optional<User> response = USER_DAO.findByUsername(username);
    
    return !response.isEmpty();
  }

  /**
   * Attempts to login the user with the provided credentials
   * @param username The user's username
   * @param password The user's password
   */
  public static void attemptLogin(String username, String password) {
    User user = USER_DAO.findByUsername(username).get();

    if (user != null && user.getPassword().equals(password)) {
      currentUser = user;
      System.out.println("LOGIN SUCCESS");
      ownedGames = GAME_ENTRY_DAO.getByOwnerId(currentUser);
    }
  }

  /**
   * Shows if the user is currently logged in
   * @return
   */
  public static boolean isLoggedIn() {
    return currentUser != null;
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
  public static List<GameEntry> getOwnedGames() {
    return ownedGames;
  }

  public static void logout() {
    if (AccountManager.isLoggedIn()) {
      currentUser = null;
    }
  }


}

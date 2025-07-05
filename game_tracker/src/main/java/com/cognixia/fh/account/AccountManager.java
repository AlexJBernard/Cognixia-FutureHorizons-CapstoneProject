package com.cognixia.fh.account;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cognixia.fh.dao.GameDaoImpl;
import com.cognixia.fh.dao.GameEntryDaoImpl;
import com.cognixia.fh.dao.UserDaoImpl;
import com.cognixia.fh.dao.model.Game;
import com.cognixia.fh.dao.model.GameEntry;
import com.cognixia.fh.dao.model.User;
import com.cognixia.fh.exception.NoResultsException;

/**
 * Collects all information associated with the current user.
 */
public class AccountManager {
  /** The user that's currently logged in */
  private static User currentUser;

  /** DAO for sending requests to the users database */
  private static final UserDaoImpl USER_DAO = new UserDaoImpl();

  /** DAO for sending requests to the games database */
  private static final GameDaoImpl GAME_DAO = new GameDaoImpl();

  /** DAO for sending requests to the game_entries database */
  private static final GameEntryDaoImpl GAME_ENTRY_DAO = new GameEntryDaoImpl();


  /**
   * See if a user with the given username already exists in the user table
   * @param username The username to search for
   * @return True if a user with the given username exists within the database
   */
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
    }
  }

  /**
   * Shows if the user is currently logged in
   * @return True if the user is logged in
   */
  public static boolean isLoggedIn() {
    return currentUser != null;
  }

  /**
   * Returns the current user
   * @return The user that's currently logged in
   */
  public static User getCurrentUser() {
    return currentUser;
  }

  /**
   * Lists all games not owned by the current user
   * @return
   */
  public static List<Game> getUnownedGames() throws NoResultsException {
    List<Game> unownedGames = GAME_DAO.getAllUnownedGames(currentUser.getId());
    if (unownedGames.isEmpty()) {
      throw new NoResultsException("No games found");
    }

    return unownedGames;
  }

  /**
   * Obtains a list of the user's games as specified in the 
   * @return A list of all GameEntries where the currentUser is the designated owner
   */
  public static List<GameEntry> getOwnedGames() throws NoResultsException {

    List<GameEntry> ownedGames = GAME_ENTRY_DAO.getByOwnerId(currentUser);

    if (ownedGames.isEmpty()) {
      throw new NoResultsException(String.format("The user %s has no games", currentUser.getUsername()));
    }

    return ownedGames;
  }

  /**
   * Returns a list of games that are currently "in progress"
   * @return
   * @throws NoResultsException
   */
  public static List<GameEntry> getGamesInProgress() throws NoResultsException {
    List<GameEntry> gamesInProgress = getOwnedGames()
      .stream().filter(g -> g.getNumCaught() > 0 && g.getNumCaught() < g.getGame().getDex())
      .collect(Collectors.toList());

    if (gamesInProgress.isEmpty()) throw new NoResultsException(String.format("The user %s has no games in-progress", currentUser.getUsername()));

    return gamesInProgress;
  }

  public static List<GameEntry> getGamesUnopened() throws NoResultsException {
    List<GameEntry> gamesUnopened = getOwnedGames()
      .stream().filter(g -> g.getNumCaught() == 0)
      .collect(Collectors.toList());

    if (gamesUnopened.isEmpty()) throw new NoResultsException(String.format("The user %s has no unopened games", currentUser.getUsername()));

    return gamesUnopened;
  }

  public static List<GameEntry> getGamesCompleted() throws NoResultsException {
    List<GameEntry> gamesComplete = getOwnedGames()
      .stream().filter(g -> g.getNumCaught() == g.getGame().getDex())
      .collect(Collectors.toList());
    
      if (gamesComplete.isEmpty()) throw new NoResultsException(String.format("The user %s has not completed any games", currentUser.getUsername()));

      return gamesComplete;
  }

  /**
   * Creates a new GameEntry with the given game
   * @param newGame The game to add to the user's list
   * @return True if a new entry is added to the database
   * @throws NoResultsException Error if a GameEntry cannot be added
   */
  public static boolean registerGame(Game newGame) throws NoResultsException {
    GameEntry newEntry = new GameEntry(0, 0, newGame, currentUser);
    if (GAME_ENTRY_DAO.insertGameEntry(newEntry)) {
      return true;
    } else {
      throw new NoResultsException("COULD NOT ADD GAME");
    }
  }

  /**
   * Logs the user out of the program
   */
  public static void logout() {
    if (AccountManager.isLoggedIn()) {
      currentUser = null;
    }
  }

  /**
   * Updates a GameEntry on the database
   * @param gameEntry The GameEntry to edit
   * @return True if a GameEntry is edited
   */
  public static boolean updateGameEntry(GameEntry gameEntry) {
    return GAME_ENTRY_DAO.updateGameEntry(gameEntry);
  }

  /**
   * Removes the input gameEntry from the game_entry table
   * @param gameEntry The GameEntry to remove
   * @return True if the game entry was removed
   */
  public static boolean removeGameEntry(GameEntry gameEntry) {
    return GAME_ENTRY_DAO.removeGameEntry(gameEntry.getId());
  }

  /**
   * Attempts to create a new user and add it to the database
   * 
   * @param username The new user's username
   * @param password The new user's password
   * @return True if a new user is added
   */
  public static boolean createAccount(String username, String password) {
    return USER_DAO.createUser(new User(username, password));
  }
}

package com.cognixia.fh.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cognixia.fh.dao.model.Game;
import com.cognixia.fh.dao.model.GameEntry;
import com.cognixia.fh.dao.model.User;

public class GameEntryDaoImpl extends DaoImpl implements GameEntryDao {

  private final String BASE_QUERY = "SELECT * FROM game_entries"
    .concat(" LEFT JOIN games ON game_entries.game_id = games.game_id")
    .concat(" LEFT JOIN users ON users.user_id = game_entries.user_id");

    /**
     * Constructs a GameEntry from the current row of the ResultSet
     * @param rs The ResultSet being parsed
     * @return A new GameEntry using the current row of the ResultSet
     */
  private GameEntry extractEntry(ResultSet rs) {
    try {

      int entryId = rs.getInt(1);
      int numCaught = rs.getInt(4);
      int rating = rs.getInt(5);
      User user = extractEntryUser(rs);
      Game game = extractEntryGame(rs);

      return new GameEntry(entryId, numCaught, rating, game, user);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Extracts a Game Entry with an existing game object
   * @param rs The result set of 
   * @param game The Game attatched to the given entry
   * @return The GameEntry made from the given data
   */
  private GameEntry extractEntry(ResultSet rs, Game game)  {
    try {
      int entryId = rs.getInt(1);
      int numCaught = rs.getInt(4);
      int rating = rs.getInt(5);
      User user = extractEntryUser(rs);

      return new GameEntry(entryId, numCaught, rating, game, user);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Constructs a new GameEntry using the given user as a parameter in its constructor
   * @param rs The ResultSet from the last database query
   * @param user The existing user object to use as a parameter
   * @return The GameEntry made from the given data
   */
  private GameEntry extractEntry(ResultSet rs, User user) {
    try {
      int entryId = rs.getInt(1);
      int numCaught = rs.getInt(4);
      int rating = rs.getInt(5);
      Game game = extractEntryGame(rs);

      return new GameEntry(entryId, numCaught, rating, game, user);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Creates a game object from the provided result set
   * @param rs The ResultSet of Game Entries with their associated games (3: id, 8: title, 9: generation, 10: dex)
   * @return The game listed in the associated Entry
   * @throws SQLException If the ResultSet could not be used
   */
  private Game extractEntryGame(ResultSet rs) throws SQLException {
    int gameId = rs.getInt(3);
    String title = rs.getString(7);
    int generation = rs.getInt(8);
    int dex = rs.getInt(9);

    return new Game(gameId, title, generation, dex);
  }

  /**
   * Creates a new User from the above query
   * 
   * @param rs The ResultSet of Game Entries. Contains user fields (10: id, 11: username, 12: password)
   * @return A new User object using the values in the given ResultSet
   * @throws SQLException If the ResultSet could not be used
   */
  private User extractEntryUser(ResultSet rs) throws SQLException {
    int userId = rs.getInt(11);
    String username = rs.getString(12);
    String password = rs.getString(13);
    return new User(userId, username, password);
  }

  @Override
  public Optional<GameEntry> getById(int id) {

    Optional<GameEntry> result = Optional.empty();

    String query = BASE_QUERY + " WHERE game_entries.game_entry_id = ?";

    try (PreparedStatement stmnt = openStatement(query)) {
      stmnt.setInt(1, id);
      ResultSet resultSet = stmnt.executeQuery();
      if (resultSet.next()) {
        result = Optional.of(extractEntry(resultSet));
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      closeStatement();
    }
    
    return result;
  }

  @Override
  public List<GameEntry> getByOwnerId(User user) {
    int userId = user.getId();
    String query = BASE_QUERY + " WHERE game_entries.user_id = ?";
    List<GameEntry> entries = new ArrayList<>();

    try (PreparedStatement  stmnt = openStatement(query)) {
      
      stmnt.setInt(1, userId);
      ResultSet resultSet = stmnt.executeQuery();
      while (resultSet.next()) {
        entries.add(extractEntry(resultSet, user));
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      closeStatement();
    }

    return entries;
  }

  /**
   * Gets a list of completed game by the user_id
   * @param user The user being checked.
   * @return An ArrayList of all games the user has completed
   */
  public List<GameEntry> getCompletedByOwnerId(User user) {
    int userId = user.getId();
    String query = BASE_QUERY + " WHERE game_entries.user_id = ? AND game_entries.game_entry_num_caught = games.game_dex";
    List<GameEntry> entries = new ArrayList<>();

    try (PreparedStatement stmnt = openStatement(query)) {
      stmnt.setInt(1, userId);
      ResultSet resultSet = stmnt.executeQuery();
      while (resultSet.next()) {
        entries.add(extractEntry(resultSet, user));
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      closeStatement();
    }

    return entries;
  }

  @Override
  public List<GameEntry> getByGameId(Game game) {
    int gameId = game.getId();
    String query = BASE_QUERY + " WHERE game_entries.game_id = ?";
    List<GameEntry> entries = new ArrayList<>();

    try (PreparedStatement stmnt = openStatement(query)) {
      stmnt.setInt(1, gameId);
      ResultSet resultSet = stmnt.executeQuery();
      while (resultSet.next()) {
        entries.add(extractEntry(resultSet, game));
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      closeStatement();
    }

    return entries;
  }

  /**
   * Adds the new GameEntry in the database under the game_entries table
   * @param entry The entry to insert into the database
   * @return True if the given entry was added to the database
   */
  public boolean insertGameEntry(GameEntry entry) {
    String query = "INSERT INTO game_entries(user_id, game_id, game_entry_num_caught, game_entry_rating) VALUES (?, ?, 0, 1)";

    try (PreparedStatement stmnt = openStatement(query)) {
      stmnt.setInt(1, entry.getUser().getId());
      stmnt.setInt(2, entry.getGame().getId());
      int updated = stmnt.executeUpdate();
      return updated > 0;
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return false;
    } finally {
      closeStatement();
    }
  }

  /**
   * Updates the a database row with the values of the input entry
   * @param entry The entry being updated
   * @return True if an entry was updated
   */
  public boolean updateGameEntry(GameEntry entry) {
    String query = "UPDATE game_entries SET game_entry_num_caught = ?, game_entry_rating = ? WHERE game_entries.game_entry_id = ?";

    try (PreparedStatement stmnt = openStatement(query)) {
      stmnt.setInt(1, entry.getNumCaught());
      stmnt.setInt(2, entry.getRating());
      stmnt.setInt(3, entry.getId());

      int updated = stmnt.executeUpdate();
      return updated > 0;
    } catch (Exception e) {
      
    } finally {
      closeStatement();
    }
    return false;
  }

  /**
   * Removes the GameEntry with the given id from the database
   * @param entryId The id of the GameEntry being removed
   * @return True if a GameEntry was removed from the database
   */
  public boolean removeGameEntry(int entryId) {
    String query = "DELETE FROM game_entries WHERE game_entries.game_entry_id = ?";

    try (PreparedStatement stmnt = openStatement(query)) {
      stmnt.setInt(1, entryId);
      int updated = stmnt.executeUpdate();
      return updated > 0;
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return false;
    } finally {
      closeStatement();
    }
  }
}

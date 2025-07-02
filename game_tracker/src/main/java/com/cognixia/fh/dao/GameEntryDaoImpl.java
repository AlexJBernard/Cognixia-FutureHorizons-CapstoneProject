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

  private final String BASE_QUERY = "SELECT * FROM pkmn_db.game_entries"
    .concat(" LEFT JOIN pkmn_db.games ON pkmn_db.game_entries.game_id = pkmn_db.games.game_id")
    .concat(" LEFT JOIN pkmn_db.users ON pkmn_db.users.user_id = pkmn_db.game_entries.user_id");

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
   * @return
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
   * @throws SQLException
   */
  private Game extractEntryGame(ResultSet rs) throws SQLException {
    int gameId = rs.getInt(3);
    String title = rs.getString(7);
    int generation = rs.getInt(8);
    int dex = rs.getInt(9);
    System.out.println(title);

    return new Game(gameId, title, generation, dex);
  }

  /**
   * 
   * @param rs The ResultSet of Game Entries. Contains user fields (10: id, 11: username, 12: password)
   * @return
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

    String query = BASE_QUERY + " WHERE pkmn_db.game_entries.game_entry_id = ?";
    
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<GameEntry> getByOwnerId(User user) {
    int userId = user.getId();
    String query = BASE_QUERY + " WHERE pkmn_db.game_entries.user_id = ?";
    List<GameEntry> entries = new ArrayList<>();

    try (PreparedStatement  stmnt = openStatement(query)) {
      
      stmnt.setInt(1, userId);
      ResultSet resultSet = stmnt.executeQuery();
      while (resultSet.next()) {
        entries.add(extractEntry(resultSet, user));
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }

    return entries;
  }

  @Override
  public List<GameEntry> getByGameId(int gameId) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
}

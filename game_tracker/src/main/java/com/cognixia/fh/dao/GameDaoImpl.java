package com.cognixia.fh.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cognixia.fh.dao.model.Game;
import com.cognixia.fh.dao.model.GameEntry;

/**
 * Handles all interactions with the 'games' and 'game_entries' tables
 */
public class GameDaoImpl extends DaoImpl implements GameDao {

  /**
   * 
   * @param rs The row being extracted
   * @return A game object using the given row
   * @throws SQLException The row does not match the required format
   */
  private Game extractResult(ResultSet rs) throws SQLException {
    int id = rs.getInt(1);
    String title = rs.getString(2);
    int generation = rs.getInt(3);
    int dex = rs.getInt(4);

    return new Game(id, title, generation, dex);
  }

  @Override
  public Optional<Game> getById(int id) {
    try (PreparedStatement stmnt = connection.prepareStatement("SELECT * FROM pkmn_db.games WHERE game_id = ?")) {
      Game game = extractResult(stmnt.executeQuery());
      return Optional.of(game);
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return Optional.empty();
    }
  }

  @Override
  public List<Game> getByGeneration(int generation) {
    String preparedStatement = "SELECT * FROM pkmn_db.games WHERE game_generation = ?";

    try (PreparedStatement stmnt = connection.prepareStatement(preparedStatement)) {
      ResultSet rs = stmnt.executeQuery();
      List<Game> games = new ArrayList<>();
      while (rs.next()) {
        games.add(extractResult(rs));
      }

      return games;
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return null;
    }
  }

  @Override
  public List<GameEntry> getByOwnerId(int userId) {
    List<GameEntry> result = new ArrayList<>();
    String query = "SELECT * FROM pkmn_db.game_entries WHERE user_id = ? LEFT JOIN pkmn.db.games ON pkmn_db.game_entries.game_id = pkmn_db.games.game_id";

    try (PreparedStatement stmnt = connection.prepareStatement(query)) {
      ResultSet rs = stmnt.executeQuery();
      Map<Integer, Game> recordedGames = new HashMap<>();
      while (rs.next()) {
        
        int gameId = rs.getInt(3);
        if (recordedGames.containsKey(gameId)) {

        }
        result.add(null);
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }

    return result;
  }
}

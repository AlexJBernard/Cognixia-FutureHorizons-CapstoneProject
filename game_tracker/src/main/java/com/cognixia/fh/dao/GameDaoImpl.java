package com.cognixia.fh.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cognixia.fh.dao.model.Game;

/**
 * Handles all interactions with the 'games' table
 */
public class GameDaoImpl extends DaoImpl implements GameDao {

  private final String BASE_QUERY = "SELECT * FROM pkmn_db.games";

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

    String query = BASE_QUERY + " WHERE game_id = ?";

    try (PreparedStatement stmnt = openStatement(query)) {

      Game game = extractResult(stmnt.executeQuery());
      return Optional.of(game);
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return Optional.empty();
    } finally {
      closeStatement();
    }
  }

  @Override
  public List<Game> getByGeneration(int generation) {
    String query = BASE_QUERY + " WHERE game_generation = ?";
    List<Game> games = new ArrayList<>();

    try (PreparedStatement stmnt = openStatement(query)) {

      stmnt.setInt(1, generation);
      ResultSet rs = stmnt.executeQuery();
      while (rs.next()) {
        games.add(extractResult(rs));
      }
      return games;
    } catch (SQLException e) {
      e.printStackTrace(System.out);
      System.out.println("Error connecting to database!\nResults may not reflect the user's list");
    } finally {
      closeStatement();
    }

    return games;
  }

  public List<Game> getAllUnownedGames(int user_id) {
    String query = BASE_QUERY + " WHERE NOT EXISTS (SELECT * FROM pkmn_db.game_entries WHERE pkmn_db.game_entries.game_id = pkmn_db.games.game_id AND pkmn_db.game_entries.user_id = ?)";
    System.out.println();

    List<Game> games = new ArrayList<>();

    try (PreparedStatement stmnt = openStatement(query)) {

      stmnt.setInt(1, user_id);
      ResultSet rs = stmnt.executeQuery();
      while (rs.next()) {
        games.add(extractResult(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace(System.out);
      System.out.println("Error connecting to database!\nResults may not reflect the user's list");
    } finally {
      closeStatement();
    }

    return games;
  }
}

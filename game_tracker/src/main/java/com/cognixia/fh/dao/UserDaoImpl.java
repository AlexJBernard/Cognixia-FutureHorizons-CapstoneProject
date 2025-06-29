package com.cognixia.fh.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.cognixia.fh.dao.model.User;

public class UserDaoImpl extends DaoImpl implements UserDao {

  private User extractResult(ResultSet rs) throws SQLException {
    int id = rs.getInt(1);
    String username = rs.getString(2);
    String password = rs.getString(3);

    return new User(id, username, password);
  }
  
  @Override
  public Optional<User> getById(int id) {
    String query = "SELECT * FROM pkmn_db.users WHERE user_id = ?";

    try (PreparedStatement stmnt = connection.prepareStatement(query)) {
      ResultSet rs = stmnt.executeQuery();
      if (rs.next()) {
        return Optional.of(extractResult(rs));
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> findByUsername(String username) {
    String query = "SELECT * FROM pkmn_db.users WHERE user_username = ?";
    try (PreparedStatement stmnt = connection.prepareStatement(query)) {
      stmnt.setString(1, username);
      ResultSet rs = stmnt.executeQuery();
      if (rs.next()) {
        return Optional.of(extractResult(rs));
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
      return Optional.empty();
    }
  }

  @Override
  public boolean createUser(User user) {
    return false;
  }
}

package com.cognixia.fh.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.cognixia.fh.dao.model.User;

public class UserDaoImpl extends DaoImpl implements UserDao {

  private final String BASEQUERY = "SELECT * FROM users";

  /**
   * Creates a new User object from the current row of the ResultSet
   * @param rs The ResultSet being evaluated
   * @return A new User object
   * @throws SQLException Thrown if data cannot be extracted from the ResultSet
   */
  private User extractResult(ResultSet rs) throws SQLException {
    int id = rs.getInt(1);
    String username = rs.getString(2);
    String password = rs.getString(3);

    return new User(id, username, password);
  }
  
  @Override
  public Optional<User> getById(int id) {
    String query = BASEQUERY + " WHERE user_id = ?";

    try (PreparedStatement stmnt = openStatement(query)) {

      stmnt.setInt(1, id);
      ResultSet rs = stmnt.executeQuery();
      if (rs.next()) {
        return Optional.of(extractResult(rs));
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      closeStatement();
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> findByUsername(String username) {
    String query = BASEQUERY + " WHERE user_username = ?";

    try (PreparedStatement stmnt = openStatement(query)) {

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
    } finally {
      closeStatement();
    }
  }

  @Override
  public boolean createUser(User user) {
    String insertQuery = "INSERT INTO users(user_username, user_password) VALUES (?, ?)";
    boolean wasCreated = false;

    try (PreparedStatement stmnt = openStatement(insertQuery)) {

      stmnt.setString(1, user.getUsername());
      stmnt.setString(2, user.getPassword());
      wasCreated = stmnt.executeUpdate() > 0;
    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {
      closeStatement();
    }
    return wasCreated;
  }
}

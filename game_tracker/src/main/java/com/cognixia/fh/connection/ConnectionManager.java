package com.cognixia.fh.connection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class ConnectionManager {

  private static final String FILENAME = "game_tracker/app.config";

  //public static Properties prop;

  /** Static definition of database url */
  private static String url;

  /** Static definition of the database username */
  private static String username;

  /** Static definition of the database password */
  private static String password;

  /** Maximum number of simultaneous connections */
  private final int POOL_SIZE = 4;

  /** List of available connections */
  private List<Connection> connectionPool;

  /** List of connections currently in use */
  private List<Connection> usedConnections = new ArrayList<>();

  /** The singleton instance of the connection manager */
  private static ConnectionManager INSTANCE;

  /**
   * Attempts to construct a new file manager
   * @throws FileNotFoundException
   * @throws SQLException
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private ConnectionManager() throws FileNotFoundException, SQLException, IOException, ClassNotFoundException {
    Properties prop = new Properties();

    // Use a try-with resources block to ensure that the file stream is closed
    try (FileInputStream fis = new FileInputStream(FILENAME)) {
      prop.load(fis);

      url = prop.getProperty("database_url");
      username = prop.getProperty("username");
      password = prop.getProperty("password");
      connectionPool = new ArrayList<>();
    } catch (Exception e) {
      throw e;
    }

    // Ensure that the associated Driver class is installed
    Class.forName("com.mysql.cj.jdbc.Driver");

    for (int i = 0; i < POOL_SIZE; i++) {
      connectionPool.add(makeConnection());
    }
  }

  /**
   * Pass the single instance of the Connection Manager
   * @return The connection manager
   */
  public static ConnectionManager getInstance() {
    if (INSTANCE == null) {
      try {
        INSTANCE = new ConnectionManager();
      }  catch (FileNotFoundException e) {

        e.printStackTrace(System.out);
        System.out.println("\nERROR: Cannot access the config file");
      } catch (IOException e) {

        e.printStackTrace(System.out);
        System.out.println("\nERROR: Could not open file stream");
      } catch (SQLException e) {

        e.printStackTrace(System.out);
        System.out.println("\nERROR: Could not connect to database");
      } catch (ClassNotFoundException e) {

        e.printStackTrace(System.out);
        System.out.println("\nERROR: User does not have the jdbc Driver installed");
      }
    }

    return INSTANCE;
  }

  /**
   * Returns a connection contained in the connectionPool
   * @return An unused connection
   */
  public Connection getConnection() {
    Connection connection = connectionPool.remove(connectionPool.size() - 1);
    usedConnections.add(connection);
    return connection;
  }

  /**
   * Removes a connection from the usedConnections list. Marks it as available for use.
   * @param connection The connection being released
   * @return True if the connection has been removed
   */
  public boolean releaseConnection(Connection connection) {
    connectionPool.add(connection);
    return usedConnections.remove(connection);
  }

  /**
   * Initializes a connection to the pkmn_games database.
   * 
   * @return A Connection object containing a 
   * @throws ClassNotFoundException If the correct java plugin is not installed, the project will throw an error
   * @throws SQLException Throws an exception if a connection to the SQL database cannot be opened
   */
  private static Connection makeConnection() throws ClassNotFoundException, SQLException {
    
    Class.forName("com.mysql.cj.jdbc.Driver");

    return DriverManager.getConnection(url, username, password);
  }

  public void closeConnections() {
    while (!usedConnections.isEmpty()) {
      this.releaseConnection(usedConnections.get(0));
    }

    for (Connection c : connectionPool) {
      try {
        c.close();
      } catch (SQLException e) {
        e.printStackTrace(System.out);
      }
    }

    connectionPool.clear();
  }

  public static void main(String[] args) {
    
  }
}

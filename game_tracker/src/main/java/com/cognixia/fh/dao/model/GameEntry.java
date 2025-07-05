package com.cognixia.fh.dao.model;

public class GameEntry {
  
  /**
   * Game id's entry
   */
  private int id;

  /** Game's Owner */
  private User user;

  /** The listed game */
  private Game game;
  
  /** Number of pokemon caught in the listed game */
  private int numCaught;

  /** User rating from 1 to 5 */
  private int rating;

  public GameEntry(int id, int numCaught, int rating, Game game, User user) {
    this.id = id;
    this.numCaught = numCaught;
    this.rating = rating;
    this.game = game;
    this.user = user;
  }

  public GameEntry(int numCaught, int rating, Game game, User user) {
    this.numCaught = numCaught;
    this.rating = rating;
    this.game = game;
    this.user = user;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  /**
   * Returns the number of Pokemon caught in the given game.
   * @return The total number of pokemon caught
   */
  public int getNumCaught() {
    return numCaught;
  }

  /**
   * Sets the number of caught Pokemon in the current game to the given integer. 
   * @param numCaught Number of pokemon caught in the current game. Must be a positive number less than or equal to the total number of pokemon in the set game.
   */
  public void setNumCaught(int numCaught) {
    if ( numCaught <= this.game.getDex() && numCaught >= 0 ) {
      this.numCaught = numCaught;
    }
  }

  public int getRating() {
    return rating;
  }


  public void setRating(int rating) {
    if (rating > 0 && rating <= 5) {
      this.rating = rating;
    }
  }

  /**
   * Returns the percentage of pokemon caught in the current game.
   * @return The percentage of pokemon caught.
   */
  public double getCompletionStatus() {
    return ((double) this.numCaught) / this.getGame().getDex();
  }

  /**
   * 
   */
  @Override
  public String toString() {
    String output = this.game.getTitle() + "\n";
    output += "Pokemon Caught:\n\t" + this.numCaught + " / " + this.game.getDex() + "\n";
    output += "User Rating:\n\t" + this.rating + " / 5";
    return output;
  }
}

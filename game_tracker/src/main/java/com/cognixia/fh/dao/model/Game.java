package com.cognixia.fh.dao.model;

public class Game {
  
  /**
   * Game id from database.
   */
  private int id;

  /**
   * The name of the listed game.
   */
  private String title;

  /**
   * The generation in which the game was released under.
   */
  private int generation;

  /**
   * Total number of pokemon needed to complete the game
   */
  private int dex;

  /**
   * Constructor used with information collected from the database
   * @param id The game's id
   * @param title The game's title
   * @param generation The game's generation
   * @param dex Total number of pokemon in pokedex
   */
  public Game(int id, String title, int generation, int dex) {
    this.id = id;
    this.title = title;
    this.generation = generation;
    this.dex = dex;
  }

  /**
   * Shortened constructor used to create games to add to the database.
   * @param title The game's title
   * @param generation The game's generation
   * @param dex Total number of pokemon in pokedex
   */
  public Game(String title, int generation, int dex) {
    this.title = title;
    this.generation = generation;
    this.dex = dex;
  }

  /**
   * Gets the game's id
   * @return The game's id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets the game's title
   * @return The game title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the game's title
   * @param title The game's new title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the game's generation
   * @return The game generation
   */
  public int getGeneration() {
    return generation;
  }

  public void setGeneration(int generation) {
    this.generation = generation;
  }

  /**
   * Returns the dex count for the current game
   * @return 
   */
  public int getDex() {
    return dex;
  }

  /**
   * Sets the total dex count
   * @param dex The new total dex count
   */
  public void setDex(int dex) {
    this.dex = dex;
  }

  @Override
  public String toString() {
    String output = this.title
      .concat("\nGeneration: " + this.generation)
      .concat("\nDex Count: " + this.dex);
    return output;
  }  
}

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

  }
}

drop database if exists pkmn_db;
create database pkmn_db;
use pkmn_db;

-- A pokemon game
CREATE TABLE pkmn_games (
    game_id         INT PRIMARY KEY 
				        AUTO_INCREMENT,
    game_title      VARCHAR(255) NOT NULL,
    game_generation SMALLINT NOT NULL,
    game_dex        INT NOT NULL
);

-- A user account
CREATE TABLE users (
    user_id         INT PRIMARY KEY 
				        AUTO_INCREMENT,
    user_username   VARCHAR(255) NOT NULL,
    user_password   SMALLINT NOT NULL
);

-- Game owned by a given user
CREATE TABLE owned_games (
    owned_game_id       INT PRIMARY KEY,
    user_id             INT,
    game_id             INT,
    owned_game_num_pkmn INT,
    owned_game_rating   INT,
    CONSTRAINT users 
        FOREIGN KEY(user_id)
        REFERENCES users( user_id )
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT pkmn_games 
        FOREIGN KEY( game_id )
        REFERENCES pkmn_games( game_id )
        ON DELETE CASCADE ON UPDATE CASCADE
);
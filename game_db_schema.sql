drop database if exists pkmn_db;
create database pkmn_db;
use pkmn_db;

-- A pokemon game
CREATE TABLE games (
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
    user_password   VARCHAR(255) NOT NULL,
    CONSTRAINT uq_user UNIQUE (user_username, user_password)
);

-- Game owned by a given user
CREATE TABLE game_entries (
    game_entry_id           INT PRIMARY KEY,
    user_id                 INT NOT NULL,
    game_id                 INT NOT NULL,
    game_entry_num_caught   INT,
    game_entry_rating       INT,
    -- An entry must be deleted if the user or game is deleted
    CONSTRAINT users 
        FOREIGN KEY(user_id)
        REFERENCES users( user_id )
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT games 
        FOREIGN KEY( game_id )
        REFERENCES games( game_id )
        ON DELETE CASCADE ON UPDATE CASCADE
);
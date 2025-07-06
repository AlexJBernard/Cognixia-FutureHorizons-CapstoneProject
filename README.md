# COGNIXIA FUTURE HORIZONS CAPSTONE PROJECT
## POKEMON-GAME PROGRESS TRACKER

The following program tracks a user's completion status of various pokemon game based on the number of pokemon they have registered in each game.

## Table of Contents
- [Purpose](##Purpose)
- [Requirements](##Requirements)
- [Functions](##Functions)

## Purpose
This project was created as a part of Cognixia's Future Horizons upskilling course to demonstrate an understanding of Java and MySQL on an intermediate level. 

## Requirements
This project requires the following in order to run properly.
- app.config file
- A MySQL database
- Java Database Connectivity API

### Prequisites
A running MySQL database is required for this project to run correctly. The repository also contains a schema file in the root directory (`/game_db_schema.sql`) to create the necessary table structure for the project. An additional file `/game_db_data.sql` is also provided to add a default list of games, users, and game entries for the project's use.

### Setup
Before running this project, the user must create an app.config file, containing the credentials for the database.

The file `/game-tracker/app.config.template` acts as a guideline for how this file should be formatted. The .config file must contain the url, username, and password for the associated database in the following format (exclude brackets).
```
database_url=jdbc:mysql://[url]
username=[username]
password=[password]
```

## Functions
When the program is run, a user is able to do the following.
- Create an account and log in with a username and password
- Register and remove a game on their list
- View and edit a list of registered games, with 3 filtering options for completion status.

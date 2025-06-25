use pkmn_db;

-- FILL POKEMON GAMES
insert into pkmn_games(game_title, game_generation, game_dex) 
    values 

    -- GENERATION 1
	('Pokemon Red', 1, 151),
	('Pokemon Blue', 1, 151),
    ('Pokemon Yellow', 1, 151),

    -- GENERATION 2
    ('Pokemon Gold', 2, 251),
    ('Pokemon Silver', 2, 251),
    ('Pokemon Crystal', 2, 251),

    -- GENERATION 3
    ('Pokemon Ruby', 3, 202),
    ('Pokemon Sapphire', 3, 202),
    ('Pokemon Emerald', 3, 202),
    ('Pokemon FireRed', 3, 151),
    ('Pokemon LeafGreen', 3, 151),

    -- GENERATION 4
    ('Pokemon Diamond', 4, 151),
    ('Pokemon Pearl', 4, 151),
    ('Pokemon Platinum', 4, 210),
    ('Pokemon HeartGold', 4, 256),
    ('Pokemon SoulSilver', 4, 256),

    -- GENERATION 5
    ('Pokemon White', 5, 155),
    ('Pokemon Black', 5, 155),
    ('Pokemon Black 2', 5, 300),
    ('Pokemon White 2', 5, 300),

    -- GENERATION 6
    ('Pokemon X', 6, 457),
    ('Pokemon Y', 6, 457),

    -- GENERATION 7
    ('Pokemon Sun', 7, 302),
    ('Pokemon Moon', 7, 302),
    ('Pokemon Ultra Sun', 7, 403),
    ('Pokemon Ultra Moon', 7, 403),

    -- GENERATION 8
    ('Pokemon Sword', 8, 400),
    ('Pokemon Shield', 8, 400),
    
    -- GENERATION 9
    ('Pokemon Scarlet', 9, 400),
    ('Pokemon Violet', 9, 400);

-- FILL USERS
INSERT INTO users(user_username, user_password)
    VALUES 

    -- ADMIN
    ('admin', 'admin'),
    
    -- TEST USER
    ('test', 'test');
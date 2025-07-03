package com.cognixia.fh;

import java.util.List;
import java.util.Scanner;

import com.cognixia.fh.account.AccountManager;
import com.cognixia.fh.connection.ConnectionManager;
import com.cognixia.fh.dao.model.Game;
import com.cognixia.fh.dao.model.GameEntry;
import com.cognixia.fh.exception.NoResultsException;

/**
 * The App class is the main file used to collect user inputs
 * 
 * @author Alex Bernard
 * @date 06/23/2025
 */
public class App 
{

    /**
     * Inner class used when displaying a list of games as an option for the user to click. Used to accomodate the limited space of the terminal window.
     */
    public static class GameDisplay {
        
        private final int PAGE_SIZE = 5;

        /** List of games to be displayed. */
        private Game[][] gameBook = null;

        /** */
        private int currentPage = 0;

        /**
         * Creates a 2D array for the app to display games
         * @param games 
         */
        public void setGameBook(List<Game> games) {
            // Reset Values
            currentPage = 0;

            // Determine the number of pages in the 'book'
            int numPages = games.size() / 5;
            if (games.size() % 5 > 0) numPages += 1;

            gameBook = new Game[numPages][PAGE_SIZE];
            for (int i = 0; i < gameBook.length; i++) {
                int checkSize = i * 5;
                
                for (int j = 0; j < gameBook[0].length && checkSize + j < games.size(); j++) {
                    gameBook[i][j] = games.get(checkSize + j);
                }
            }
        }

        /**
         * Returns the next 'page' of games
         * @return
         */
        public Game[] getPage() {
            if (gameBook != null) {
                return gameBook[currentPage];
            } else {
                return null;
            }
        }

        public void nextPage() {
            if (gameBook != null) {
                currentPage += 1;
                if (currentPage >= gameBook.length) {
                    currentPage = 0;
                }
            }
        }
    }

    private static final GameDisplay gameDisplay = new GameDisplay();

    /** Manages all connections to the database */
    private static ConnectionManager connectionManager;

    private static final String EXIT_STRING = "Q";

    /**
     * States for each menu
     */
    private enum MenuState {
        /** Login Menu state */
        LOGIN,

        /** Start Menu state */
        START,
        
        /** Add Game Menu state */
        ADD,
        
        /** Edit Game Menu state */
        EDIT;
    }



    public static void main( String[] args )
    {

        connectionManager = ConnectionManager.getInstance();

        try (Scanner scan = new Scanner(System.in)) {
            boolean isOpen = true;

            MenuState menu = MenuState.LOGIN;
            while (isOpen) {
                System.out.println("\nTHE POKEMON GAME TRACKER");
                System.out.println("Input 'Q' at any time to exit\n");

                switch (menu) {
                    case LOGIN:
                        System.out.println("1 - Login");
                        System.out.println("2 - Register new User");
                        System.out.println("Q - Quit");
                        break;
                    case START:
                        System.out.printf("You are logged in as:\n%s\n", AccountManager.getCurrentUser().getUsername());
                        System.out.println("1 - View Owned Games");
                        System.out.println("2 - Register Game");
                        System.out.println("L - Log Out");
                        System.out.println("Q - Quit");
                        break;
                    case ADD: 
                        System.out.println("Select a game to add");
                        Game[] display = gameDisplay.getPage();
                        int i = 0;
                        for (Game g : display) {
                            if (g != null) {
                                System.out.printf("%d - %s\n", i, g.getTitle());
                                i++;
                            } else {
                                break;
                            }
                        }
                        System.out.println("\nN - Next Page");
                        System.out.println("C - Cancel Action");
                        System.out.println("Q - QUIT APP");
                    default:
                        break;
                }
                System.out.println("\n");

                System.out.print("Input: ");
                String input = scan.nextLine();

                // ALWAYS CHECK FOR EXIT QUERY
                isOpen = !input.equals(EXIT_STRING);

                // DO NOT PROCESS AN EXIT QUERY AT ANY MOMENT
                if (isOpen) {
                    switch (menu) {
                        case LOGIN:

                            boolean loggedin = loginProcess(scan);
                            if (loggedin) {
                                menu = MenuState.START;
                            }
                            break;
                        case START:
                            menu = inputStartMenu(input);
                            break;
                        case ADD:
                            menu = inputAddMenu(input);
                            break;
                        default: // Error
                            break;
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            connectionManager.closeConnections();
        }
    }


    /**
     * Runs the login query for the user. Attempts to login.
     * @param scan Scanner for user input
     * @return True if the user has successfully logged in
     */
    private static boolean loginProcess( Scanner scan ) 
    {
        
        System.out.print("INSERT USERNAME: ");
        String username = scan.nextLine();

        boolean foundUser = AccountManager.validateUsername(username);

        if (foundUser) {
            System.out.print("INSERT PASSWORD: ");
            String password = scan.nextLine();
            AccountManager.attemptLogin(username, password);
        } else {
            System.out.println("NO USER FOUND");
        }

        return AccountManager.isLoggedIn();
    }


    /**
     * Evaluates input given on the user start menu
     * @param input
     * @param scan
     * @return
     */
    private static MenuState inputStartMenu( String input ) {
        MenuState result = MenuState.START;
        switch(input) {
            case "1": // View Owned Games
                System.out.println("PRINTING OWNED GAMES");
                try {
                    List<GameEntry> games = AccountManager.getOwnedGames();
                    for (GameEntry e : games) {
                        System.out.println(e.getGame().toString());
                    }
                } catch (NoResultsException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2": // Register a new game
                /**
                 * 1: Collect all unowned games
                 * 2: Display up to 5 games at a time for the user to add
                 * 3: Add game to user's list as unfinished
                 */
                try {
                    List<Game> games = AccountManager.getUnownedGames();
                    if (!games.isEmpty()) {
                        gameDisplay.setGameBook(games);
                        result = MenuState.ADD;
                    }
                } catch (NoResultsException e) {
                    System.out.println(e.getMessage());
                }
                
                // SUBMENU: ADD
                break;
            case "L": // LOGOUT
                AccountManager.logout();
                result = MenuState.LOGIN;
                break;
            default: // Error
                System.out.println("ERROR: Invalid response");
                break;
        }

        return result;
    }

    private static MenuState inputAddMenu( String input) {
        MenuState result = MenuState.ADD;
        switch (input) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                int index = Integer.parseInt(input);
                System.out.println(gameDisplay.getPage()[index].toString());
                result = MenuState.START;
                break;
            case "N":
                gameDisplay.nextPage();
                break;
            case "C":
                result = MenuState.START;
            default: 
                break;
        }
        return result;
    }
}

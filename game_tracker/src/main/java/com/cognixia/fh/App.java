package com.cognixia.fh;

import java.util.List;
import java.util.Scanner;

import com.cognixia.fh.account.AccountManager;
import com.cognixia.fh.connection.ConnectionManager;
import com.cognixia.fh.dao.model.Game;
import com.cognixia.fh.dao.model.GameEntry;
import com.cognixia.fh.exception.NoResultsException;
import com.cognixia.fh.exception.OutOfBoundsException;

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
         * @return A Game array of length 5, containing the current list of games
         */
        public Game[] getPage() {
            if (gameBook != null) {
                return gameBook[currentPage];
            } else {
                return null;
            }
        }

        /**
         * Toggles to the next page in the gameBook
         */
        public void nextPage() {
            if (gameBook != null) {
                currentPage += 1;
                if (currentPage >= gameBook.length) {
                    currentPage = 0;
                }
            }
        }

        public boolean hasPages() {
            return gameBook.length > 1;
        }
    }

    public static class EntryDisplay {
        /**  */
        private final int PAGE_SIZE = 5;

        /** 2D array of games to display */
        private GameEntry[][] entryBook;


        private GameEntry currentEntry;

        private enum filter {
            NONE,
            INCOMPLETE,
            COMPLETE,
            NOTSTARTED
        };

        private int currentPage = 0;

        public void setBook(List<GameEntry> entries) {
            // Reset Values
            currentPage = 0;

            // Determine the number of pages in the 'book'
            int numPages = entries.size() / 5;
            if (entries.size() % 5 > 0) numPages += 1;

            entryBook = new GameEntry[numPages][PAGE_SIZE];
            for (int i = 0; i < entryBook.length; i++) {
                int checkSize = i * 5;
                
                for (int j = 0; j < entryBook[0].length && checkSize + j < entries.size(); j++) {
                    entryBook[i][j] = entries.get(checkSize + j);
                }
            }
        }

        /**
         * Returns the next 'page' of GameEntries
         * @return A GameEntry array of length 5, containing the current list of games
         */
        public GameEntry[] getPage() {
            if (entryBook != null) {
                return entryBook[currentPage];
            } else {
                return null;
            }
        }

        /**
         * Displays all 
         * @return
         */
        public String currentPageString() {
            StringBuilder output = new StringBuilder();
            GameEntry[] entryPage = entryBook[currentPage];
            for (int i = 0; i < entryPage.length; i++) {
                if (entryPage[i] == null) {
                    break;
                }
                output.append(String.format("%s - %s\n", i, entryPage[i].getGame().getTitle()));
            }
            return output.toString();
        }

        /**
         * Toggles to the next page in the entryBook
         */
        public void nextPage() {
            if (entryBook != null) {
                currentPage += 1;
                if (currentPage >= entryBook.length) {
                    currentPage = 0;
                }
            }
        }

        public boolean hasPages() {
            return entryBook.length > 1;
        }

        /**
         * Returns the last selected entry from the current
         * @param index The index of the selected entry on the current page
         * @return True if a GameEntry exists on the selected page
         */
        public boolean setCurrentEntry(int index) {
            currentEntry = null;
            try {
                currentEntry = entryBook[currentPage][index];
            } catch (Exception e) {
                System.out.println("OUT OF BOUNDS");
            }
            return currentEntry != null;
        }

        public GameEntry getCurrentEntry() {
            return currentEntry;
        }
        

        public void reset() {
            entryBook = null;
            currentEntry = null;
            currentPage = 0;
        }
    }

    /** Class used to display games on terminal window */
    private static final GameDisplay gameDisplay = new GameDisplay();

    /** Class used to collect entry information for terminal display */
    private static final EntryDisplay entryDisplay = new EntryDisplay();
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
        
        /** Select Game Menu state */
        SELECT,
        
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
                        break;
                    case START:
                        System.out.printf("You are logged in as:\n%s\n", AccountManager.getCurrentUser().getUsername());
                        System.out.println("1 - View Owned Games");
                        System.out.println("2 - Register Game");
                        System.out.println("3 - View In-Progress Games");
                        System.out.println("4 - View Unopened Games");
                        System.out.println("5 - View Completed games\n");
                        System.out.println("L - Log Out");
                        break;
                    case ADD: 
                        System.out.println("Select a game to add");
                        int i = 0;
                        for (Game g : gameDisplay.getPage()) {
                            if (g != null) {
                                System.out.printf("%d - %s\n", i, g.getTitle());
                                i++;
                            } else {
                                break;
                            }
                        }
                        System.out.println("\nN - Next Page");
                        System.out.println("C - Cancel Action");
                        break;
                    case SELECT:
                        System.out.println("Select a game to edit");
                        System.out.print(entryDisplay.currentPageString());
                        System.out.println("");
                        if (entryDisplay.hasPages()) {
                            System.out.println("N - Next Page");
                        }
                        System.out.println("C - Cancel Action");
                        break;
                    case EDIT:
                        System.out.println(entryDisplay.getCurrentEntry().toString());
                        System.out.println("Select a field to edit");
                        System.out.println("P - Pokemon Caught");
                        System.out.println("R - Rating");
                        System.out.println("D - Delete\n");
                        System.out.println("C - Cancel");
                        break;
                    default:
                        break;
                }
                System.out.println("Q - CLOSE APP\n");

                System.out.print("Input: ");
                String input = scan.nextLine();

                // ALWAYS CHECK FOR EXIT QUERY
                isOpen = !input.equals(EXIT_STRING);

                // DO NOT PROCESS AN EXIT QUERY AT ANY MOMENT
                if (isOpen) {
                    switch (menu) {
                        case LOGIN:
                            menu = inputLogin(input, scan);
                            break;
                        case START:
                            menu = inputStartMenu(input);
                            break;
                        case ADD:
                            menu = inputAddMenu(input);
                            break;
                        case SELECT: 
                            menu = inputSelectMenu(input);
                            break;
                        case EDIT:
                            menu = inputEditMenu(input, scan);
                            break;
                        default: // Error
                            menu = MenuState.START;
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
     * Process the user's input on the Login Menu.
     * Contains the functionality for the login and account registration processes
     * @param input The user's last input
     * @param scan Scanner for system input
     * @return The next MenuState
     */
    private static MenuState inputLogin( String input, Scanner scan) {
        MenuState result = MenuState.LOGIN;

        String username;
        String password;

        switch(input) {
            case("1"): // Login process
                System.out.println("INSERT USERNAME: ");
                username = scan.nextLine();

                if (AccountManager.validateUsername(username)) {
                    System.out.print("INSERT PASSWORD: ");
                    password = scan.nextLine();

                    AccountManager.attemptLogin(username, password);

                    result = AccountManager.isLoggedIn() ? MenuState.START : MenuState.LOGIN;
                } else {
                    System.out.println("Account does not exist");
                }
                break;
            case("2"):
                System.out.println("INSERT USERNAME: ");
                username = scan.nextLine();

                if (!AccountManager.validateUsername(username)) {
                    System.out.print("INSERT PASSWORD: ");
                    password = scan.nextLine();

                    if (AccountManager.createAccount(username, password)) {
                        System.out.println("Account created!");
                    } else {
                        System.out.println("Could not create account");
                    }
                } else {
                    System.out.println("Account already exists");
                }
                break;
            default:
                break;
        }

        return result;
    }

    /**
     * Evaluates input given on the user start menu
     * @param input The user's input
     * @return The new Menu State after the operation is complete
     */
    private static MenuState inputStartMenu( String input ) {
        MenuState result = MenuState.START;
        switch(input) {
            case "1": // View Owned Games
                try {
                    List<GameEntry> games = AccountManager.getOwnedGames();
                    entryDisplay.setBook(games);
                    result = MenuState.SELECT;
                } catch (NoResultsException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2": // Register a new game
                try {
                    List<Game> games = AccountManager.getUnownedGames();
                    if (!games.isEmpty()) {
                        gameDisplay.setGameBook(games);
                        result = MenuState.ADD;
                    }
                } catch (NoResultsException e) {
                    System.out.println(e.getMessage());
                }
                
                break;
                
            case "3": // List In-Progress Games
                try {
                    List<GameEntry> games = AccountManager.getGamesInProgress();
                    entryDisplay.setBook(games);
                    result = MenuState.SELECT;
                } catch (NoResultsException e) {
                    System.out.println(e.getMessage());
                }
                break;
                
            case "4": // List Unopened Games
                try {
                    List<GameEntry> games = AccountManager.getGamesUnopened();
                    entryDisplay.setBook(games);
                    result = MenuState.SELECT;
                } catch (NoResultsException e) {
                    System.out.println(e.getMessage());
                }
                break;

            case "5": // List Complete Games
                try {
                    List<GameEntry> games = AccountManager.getGamesCompleted2();
                    entryDisplay.setBook(games);
                    result = MenuState.SELECT;
                } catch (NoResultsException e) {
                    System.out.println(e.getMessage());
                }
                break;

            // OTHER
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

    /**
     * Processes input for the Add Menu
     * @param input User input on the Add Menu screen
     * @return The new MenuState after the interaction completes
     */
    private static MenuState inputAddMenu( String input ) {
        MenuState result = MenuState.ADD;
        switch (input) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                int index = Integer.parseInt(input);
                Game newGame = gameDisplay.getPage()[index];
                if (newGame != null) {
                    try {
                        AccountManager.registerGame(newGame);
                        System.out.println("GAME ADDED");
                    } catch (NoResultsException e) {
                        System.out.println(e.getMessage());
                    }
                }
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

    /**
     * Process input for the Select Menu
     * @param input The last input given by the user
     * @return The new MenuState after the interaction completes
     */
    private static MenuState inputSelectMenu( String input ) {
        MenuState result = MenuState.START;

        switch (input) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
                boolean find = entryDisplay.setCurrentEntry(Integer.parseInt(input));

                if (find) {
                    result = MenuState.EDIT;
                } else {
                    System.out.println("Entry not found");
                    result = MenuState.START;
                }
                break;
            case "N":
                entryDisplay.nextPage();
                result = MenuState.SELECT;
                break;
            case "C":
                result = MenuState.START;
            default:
                break;
        }
        return result;
    }

    /**
     * Handles all input for the Edit Menu (PRDC)
     * Edit progress
     * Edit rating
     * Delete Entry
     * Cancel action
     * 
     * @param input The user's last input
     * @param scan Used for options 
     * @return The new MenuState after the interaction completes
     */
    private static MenuState inputEditMenu( String input, Scanner scan ) {
        MenuState result = MenuState.START;
        GameEntry mayEdit = entryDisplay.getCurrentEntry();
        switch( input ) {
            case "P": // Input the number of pokemon caught
                System.out.printf("Enter new Number of Pokemon (out of %s)\n", mayEdit.getGame().getDex());
                String inputCaught = scan.nextLine();
                try {
                    int newCaught = Integer.parseInt(inputCaught);

                    if (newCaught < 0 || newCaught > mayEdit.getGame().getDex()) throw new OutOfBoundsException("Value must be a positive integer less than or equal to the number of pokemon in the given game");

                    mayEdit.setNumCaught(newCaught);
                    AccountManager.updateGameEntry(mayEdit);
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Must Input an integer");
                } catch (OutOfBoundsException e) {
                    System.out.print(e.getMessage());
                }
                break;
            case "R":
                System.out.println("Enter the new rating 1- 5");
                String inputRating = scan.nextLine();
                try {
                    int newRating = Integer.parseInt(inputRating);

                    if (newRating < 0 || newRating > 5) throw new OutOfBoundsException("Value must be a positive integer less 6 and greater than 0");

                    mayEdit.setRating(newRating);
                    AccountManager.updateGameEntry(mayEdit);
                } catch (NumberFormatException e) {
                    System.out.println("ERROR: Must Input an integer");
                } catch (OutOfBoundsException e) {
                    System.out.print(e.getMessage());
                }
                break;
            case "D":
                System.out.println("Attempting to remove " + mayEdit.getGame().getTitle());
                if (AccountManager.removeGameEntry(mayEdit)) {
                    System.out.println("Game Removed!");
                }
                break;
            case "C":
                result = MenuState.START;
                break;
            default:
                break;
        }

        return result;
    }
}

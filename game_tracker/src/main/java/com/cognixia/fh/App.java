package com.cognixia.fh;

import java.util.Scanner;

import com.cognixia.fh.account.AccountManager;

/**
 * The App class is the main file used to collect user inputs
 * 
 * @author Alex Bernard
 * @date 06/23/2025
 */
public class App 
{

    private static final String EXIT_STRING = "Q";

    /**
     * States for each menu
     */
    private enum MenuState {
        /** Login Menu state */
        LOGIN,

        /** Start Menu state */
        START;
    }



    public static void main( String[] args )
    {
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
                            menu = inputStartMenu(input, scan);
                            break;
                        default: // Error
                            break;
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
    private static MenuState inputStartMenu( String input, Scanner scan) {
        MenuState result = MenuState.START;
        switch(input) {
            case "1": // View Owned Games
                break;
            case "2": // Register a new game

                break;
            default: // Error
                System.out.println("ERROR: Invalid response");
                break;
        }

        return result;
    }
}

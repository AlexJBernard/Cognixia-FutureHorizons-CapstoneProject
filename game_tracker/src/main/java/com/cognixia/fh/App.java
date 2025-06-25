package com.cognixia.fh;

import java.util.Scanner;

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

            MenuState menu = MenuState.START;
            while (isOpen) {
                System.out.println("\nWELCOME TO THE POKEMON GAME TRACKER");
                System.out.println("Input 'Q' at any time to exit");

                switch (menu) {
                    case LOGIN:
                        System.out.println("\nWELCOME TO THE POKEMON GAME TRACKER:");
                        break;
                    case START:
                        System.out.println("WELCOME TO THE POKEMON GAME TRACKER:");
                        System.out.println("1 - Menu 1");
                        System.out.println("Q - Quit");
                        break;
                    default:
                        break;
                }

                System.out.print("Input: ");
                String input = scan.nextLine();

                // ALWAYS CHECK FOR EXIT QUERY
                isOpen = !input.equals(EXIT_STRING);

                // DO NOT PROCESS AN EXIT QUERY AT ANY MOMENT
                if (isOpen) {
                    switch (menu) {
                        case LOGIN:
                            break;
                        case START:
                            break;
                        default:
                            break;
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void inputLogin( String input ) 
    {

    }
}

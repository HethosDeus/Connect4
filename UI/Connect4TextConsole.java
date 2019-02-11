/**UI contains the Connect4TextConsole.java file.  This is the user interface for 
 * I/O in the text console for the game.
 */
package UI;

import Core.Connect4;
import Core.Connect4ComputerPlayer;
import java.util.InputMismatchException;
import java.util.Scanner;
 
 /** This class is the user UI and input for the connect4 game.  Handles all 
  * user input, and calls Connect4 methods for logic in the program.
  * <p>
 *  Required for Functionality: 
 *  <ul>
 *  <li>Connect4TextConsole.java
 *  <li>Connect4ComputerPlayer.java
 *  </ul>
  * <p>
  * @author Adam Clifton, SER 216
  * @version 1.1
  */
class Connect4TextConsole {
    
    /**Connect4 object for game_board to initialize and play the game. */
    private Connect4 game_board = new Connect4();
    /**static Scanner to handle all inputs outside of the startGame() method. */
    static Scanner scan = new Scanner(System.in);
    /**Connect4ComputerPlayer object to initialize and play against the 
     * computer.
     */
    private Connect4ComputerPlayer compAI = new Connect4ComputerPlayer();
	
    
    /**
     * Constructor to make a text console game object.
     * @param game Connect4 object to use
     */
    public Connect4TextConsole(Connect4 game)
    {
        this.game_board = game;
    }
        
    
    /**startGame()    Initializing the game, gets user input. */
    public void startGame()
    {
        System.out.println("Welcome to Connect Four!");
        System.out.println("Press \"P\" if you want to play against another player.");
        System.out.println("Press \"C\" to play against the computer.");
        System.out.println("Press \"Q\" to exit game.");
        Scanner startGame = new Scanner(System.in);
        String gameStatus = startGame.next();
        
        while (gameStatus != null)
        {
            if(gameStatus.equalsIgnoreCase("P")) 
            {
                rulesHuman();
                playGameHuman();
                startGame.close();
            }
            if(gameStatus.equalsIgnoreCase("C"))
            {
                rulesComputer();    
                playGameComputer();
                startGame.close();
            }
            if(gameStatus.equalsIgnoreCase("Q")) 
            {
                System.out.println("Exiting game...");
                startGame.close();
                System.exit(0);
            } else {
                System.out.println("Please select E to play game or Q to quit.");
                gameStatus = startGame.next();
            }
        }
    }
	
	
    /** playGameHuman()	method where the game is played; gets move, validates move, 
     *  adds token to the game board, and checks for winner.
     * <p>
     * @throws InputMismatchException if something other than an int is scanned.
     */
    public void playGameHuman() throws InputMismatchException
    {   
        String token = game_board.getPlayerToken();
        boolean validCol;
        int tempMove; //holds col selected by user
        int move; //holds adjusted column selection to fit perceived board
        
        game_board.drawBoard();
        
        while (game_board.getWinner() == false && game_board.getTurnCount() <= 42)
        {
            // try/catch to handle input mismatch  
            try
            {    
                System.out.println("Player " + token + ", Please choose a "
                        + "column between 1-7."); 
                tempMove = scan.nextInt();
                move = tempMove - 1;
                validCol = game_board.validateMove(move);
            
                while (validCol == false)
                {
                    System.out.println("Invalid option. Please choose a"
                            + " column between 1-7.");
                    tempMove = scan.nextInt();
                    move = tempMove - 1;
                    validCol = game_board.validateMove(move);
                }
                //add token to the game board
                game_board.dropToken(token, move);
                //check for winner 
                game_board.checkWinner(token);
                game_board.playerTurn(token);	
                token = game_board.getPlayerToken();
                game_board.drawBoard();
                
            }catch (InputMismatchException e){
                System.out.println("That is not a number. Please select a valid"
                        + " column.");
                //catch weird input and do nothing
                String temp = scan.nextLine();            
            }
        }

	//output winner
	if(game_board.getWinner() == true)
        {
            game_board.playerTurn(token); 
            token = game_board.getPlayerToken();
            if(token.equals("X"))
            {
                System.out.println("CONGRATULATIONS!");
                System.out.println("Player X has won the game!  Game over." );
                System.exit(0);
            } else {
                System.out.println("CONGRATULATIONS!");
                System.out.println("Player O has won the game!  Game over.");
                System.exit(0);
            }              
        } else {
            System.out.println("The game is a draw!  Game over");
            System.exit(0);
        }
    }
    
    
    /** playGameComputer()  method where the game is played against the AI;
     *  gets move, validates move, adds token to the game board, and checks
     *  for winner.
     * <p>
     * @throws InputMismatchException if something other than an int is scanned.
     */
    public void playGameComputer()
    {
        String token = compAI.getPlayerToken();
        boolean validCol;
        int tempMove;   // hold column selected by user
        int move;       // holds adjusted column selection to fit perceived board
        compAI.drawBoardAI();
        while (compAI.getWinner() == false && compAI.getTurnCount() <= 42)
        {
            // try/catch to handle input mismatch  
            try
            {
                if (compAI.getPlayerToken().equals("X"))
                {
                    System.out.println("Player " + token + ", Please choose a "
                            + "column between 1-7."); 
                    tempMove = scan.nextInt();
                    move = tempMove - 1;
            
                    validCol = compAI.validateMove(move);
                
                    while (validCol == false)
                    {
                        System.out.println("Invalid option. Please choose a column "
                            + "between 1-7.");
                        tempMove = scan.nextInt();
                        move = tempMove - 1;
                        validCol = compAI.validateMove(move);
                    }
                } else {
                    compAI.computerPlayer();
                    move = compAI.getComputerCol();
                    validCol = compAI.validateMove(move);
                    
                    while (validCol == false)
                    {
                        compAI.computerPlayer();
                        move = compAI.getComputerCol();
                        validCol = compAI.validateMove(move);
                    }
                }
                //add token to the game board
                compAI.dropToken(token, move);
                //check for winner 
                compAI.checkWinner(token);
                compAI.playerTurn(token);	
                token = compAI.getPlayerToken();
                compAI.drawBoardAI();    
                
            }catch (InputMismatchException e){
                System.out.println("That is not a number. Please select a valid"
                        + " column.");
                //catch weird input and do nothing
                String temp = scan.nextLine();            
            }
        }
        
        //output winner
	if(compAI.getWinner() == true)
        {
            compAI.playerTurn(token); 
            token = compAI.getPlayerToken();
            if(token.equals("X"))
            {
                System.out.println("CONGRATULATIONS!");
                System.out.println("Player X has won the game!  Game over." );
                System.exit(0);
            } else {
                System.out.println("CONGRATULATIONS!");
                System.out.println("The computer has won the game!  Game over.");
                System.exit(0);
            }              
        } else {
            System.out.println("The game is a draw!  Game over.");
            System.exit(0);
        }
    }

	
    
    /**rulesHuman()  displays the rules of the game when playing another player. */
    public void rulesHuman()
    {
       System.out.println("::::Here's how to play Connect Four::::");
       System.out.println("Each player will get 21 'X' or 'O' tokens. The game "
        + "will ask you to \nselect a column to drop your token on to the 6x7 game "
        + "board. The token \nwill be placed in the bottom-most spot available "
        + "in the selected column. \nThe first player to get four tokens in a "
        + "row vertically, horizontally, or \ndiagonally wins. If no one gets "
        + "four in a row it's a draw. Have fun!");
        System.out.println("");
    }
    
    /**rulesComputer()  displays the rules of the game when playing the computer. */
    public void rulesComputer()
    {
       System.out.println("::::Here's how to play Connect Four::::");
       System.out.println("You will get 21 'X' tokens and the computer will get 21 'O' "
        + "tokens. The \ngame will ask you to select a column to drop your token on"
        + " to the 6x7 \ngame board. The token will be placed in the bottom-most"
        + " spot available \nin your selected column. The first to get four"
        + " tokens in a row vertically, \nhorizontally, or diagonally wins. If no"
        + " one gets four in a row it's a \ndraw. Have fun!");
        System.out.println("");
    }
}


/**Core contains Connect4.java, and Connect4ComputerPlayer.java.
 * This contains the logic for the game.
 */
package Core;

import java.util.Arrays;

/** This is an implementation of Connect Four.  This class handles all of 
 *  the logic aspects of the program.  
 *  <p>
 *  Required for Functionality: 
 *  <ul>
 *  <li>Connect4TextConsole.java
 *  <li>Connect4ComputerPlayer.java
 *  </ul>
 *  <p>
 *  @author Adam Clifton, SER 216
 *  @version 1.1
*/
public class Connect4 {

    /** ROW to hold number of rows on game board.*/
    private final static int ROW = 6;
    /** COL to hold number of columns on game board.*/
    private final static int COL = 7;
    /** Game board where the game is played.*/
    private String[][] gameBoard;
    /** Player Token to designate game piece X or O. */
    private String playerToken = "X";
    /** Game piece counters for player X.*/
    private int gamePiecesX = 21;
    /** Game piece counters for player O.*/
    private int gamePiecesO = 21;
    /** Game logic variable:   boolean flag to find winner. */
    private boolean winner = false;  
    /** Game logic variable:  turn counter to track total number of moves made. */
    private int turnCount = 1;

    
    /** Connect4() 	Constructor initializes board and sets all inputs to 
     * empty space.
     */
    public Connect4()
    {
    	gameBoard = new String[ROW][COL];
	reset();
    }
	
 
    /**reset() 	Sets all rows and columns to empty space. */
    private void reset()
    {
	for(String[]row : gameBoard)
	{
            Arrays.fill(row, " ");
	}
    }
    
    /** getPlayerToken()    Accessor returns the playerToken.
     * @return              String playerToken.   
     */
    public String getPlayerToken()
    {
	return playerToken;
    }
    /**getTurnCount()       Accessor returns the total turnCount.
     * @return              int turnCount.
     */
    public int getTurnCount()
    {
        return turnCount;
    } 
    /**getWinner()          Accessor returns the boolean for winner.
    *@return                <code> true </code> if there is a winner;
    *                       <code> false </code> otherwise
    */
    public boolean getWinner()
    {
        return winner;
    }
	
		
    /**drawBoard()  draws the game board in its current state, and lets user
     * know how many pieces they have left to use.
     */
    public void drawBoard()
    {
        System.out.println("----------------------");
        System.out.println("  1  2  3  4  5  6  7 ");
        System.out.println("----------------------");
        
        for (int i = 0; i < ROW ; i++)
        {
           // System.out.print("|");
            for (int j = 0; j < COL ; j++)
            {
                System.out.print("| " + gameBoard[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
        
        if (playerToken.equals("X"))
        {
            System.out.println("Player X has " + gamePiecesX + " tokens left.");
            gamePiecesX--;
        }else{
            System.out.println("Player O has " + gamePiecesO + " tokens left.");
            gamePiecesO--;
        }
    }
	
    
        
    /**drawBoardAI() draws the game board in its current state, and
     * lets user know if it is their turn or the computer's turn.  To be used
     * when playing against the computer. 
     * <p>
     * This is a slight variation of the drawBoard() method.
     */
    public void drawBoardAI()
    {
        System.out.println("----------------------");
        System.out.println("  1  2  3  4  5  6  7 ");
        System.out.println("----------------------");
        
        for (int i = 0; i < ROW ; i++)
        {
           // System.out.print("|");
            for (int j = 0; j < COL ; j++)
            {
                System.out.print("| " + gameBoard[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
        
        if (playerToken.equals("X"))
        {
            System.out.println("Player, it is your turn.");
        }else{
            System.out.println("It is the computer's turn. Please select a colum"
                    + "between 1-7.");
        }
    }

    
    /**validateMove()	Helper method to make sure the column selected is a 
     * valid move on the game board.
     * <p>
     * @param pMove         Takes move as input to check for validity.
     * @return              <code> true </code> if move is valid;
     *                      <code> false</code> otherwise.
     */
    public boolean validateMove(int pMove)
    {   
        if (winner == false && turnCount <= 42)
        {
            if(pMove > gameBoard[0].length - 1 || pMove < 0)
            {
                System.out.println("That column is not on the board. Select again.");
		return false; 
            }
            if(!gameBoard[0][pMove].equals(" "))
            {
		System.out.println("That column is full. Select again.");
                return false;
            } 
	}				  
	return true;
    }
        

    /**dropToken()  Adds the token to the game board. 
     * <p>
     * @param pPlayerToken  Takes playerToken to add to gameBoard.
     * @param pMove         Takes move to add token to correct column.
     */
    public void dropToken(String pPlayerToken, int pMove)
    {
        //add token to gameBoard in bottom-most slot available
        for(int row = gameBoard.length - 1; row >= 0; row--)
        {
            if(gameBoard[row][pMove].equals(" "))
            {
                gameBoard[row][pMove] = playerToken;
                break;
            }
        }
    }
	
        
    /**playerTurn()   Helper method to alternate turns between player X 
     * and player O.  Also increments turnCount.
     * <p>
     * @param pPlayerToken	Takes playerToken as parameter to check turn.
     */
    public void playerTurn(String pPlayerToken)
    {
        if (pPlayerToken.equals("X"))
        {
            playerToken = "O";
        } else {
            playerToken = "X";
        }
        turnCount++;
    }


    /**checkWinner()  Helper method to check is there is a winner in the game by
     * checking if a player got 4 in a row vertically, horizontally, or 
     * diagonally left and diagonally right. 
     * Method will prompt when turnCount greater than or equal to 7.
     * <p>
     * @param pPlayerToken  Takes playerToken to check for four in a row
     *                      against the gameBoard.
     * 
     * @return              <code> True </code> if four tokens in a row are
     *                      found in any direction;
     *                      <code> False </code> otherwise.              
     */
    public boolean checkWinner(String pPlayerToken)
    {
        if (turnCount >= 7)
        {
            //horizontal check
            for (int row = 0 ; row < gameBoard.length; row++ )
            {
                for(int col = 0; col < gameBoard[0].length - 3; col++)
                {
                    if (gameBoard[row][col].equals(pPlayerToken) &&
                        gameBoard[row][col + 1].equals(pPlayerToken)&&
                        gameBoard[row][col + 2].equals(pPlayerToken) &&
                        gameBoard[row][col + 3].equals(pPlayerToken))
                    {
                        winner = true;
                        return true;
                    }
                }
            }
            //vertical check
            for (int row = 0 ; row < gameBoard.length - 3; row++ )
            {
                for(int col = 0; col < gameBoard[0].length; col++)
                {
                    if (gameBoard[row][col].equals(pPlayerToken) &&
                        gameBoard[row + 1][col].equals(pPlayerToken) &&
                        gameBoard[row + 2][col].equals(pPlayerToken) &&
                        gameBoard[row + 3][col].equals(pPlayerToken))
                    {
                        winner = true;
                        return true;
                    }
                }
            }
            //diagonal-up right check
            for (int row = 3 ; row < gameBoard.length; row++ )
            {
                for(int col = 0; col < gameBoard[0].length - 3; col++)
                {
                    if (gameBoard[row][col].equals(pPlayerToken) &&
                        gameBoard[row - 1][col + 1].equals(pPlayerToken) &&
                        gameBoard[row - 2][col + 2].equals(pPlayerToken) &&
                        gameBoard[row - 3][col + 3].equals(pPlayerToken))
                    {
                        winner = true;
                        return true;
                    }
                }
            }
            //diagonal-down right check
            for (int row = 0 ; row < gameBoard.length - 3; row++ )
            {
                for(int col = 0; col < gameBoard[0].length - 3; col++)
                {
                    if (gameBoard[row][col].equals(pPlayerToken) &&
                        gameBoard[row + 1][col + 1].equals(pPlayerToken) &&
                        gameBoard[row + 2][col + 2].equals(pPlayerToken) &&
                        gameBoard[row + 3][col + 3].equals(pPlayerToken))
                    {
                        winner = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

/**Core contains Connect4.java, and Connect4ComputerPlayer.java.
 * This contains the logic for the game.
 */
package Core;

import java.util.Random;

/** This is the AI implementation of Connect Four.  This class handles the AI
 *  components of the program when playing against the computer.
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
public class Connect4ComputerPlayer extends Connect4 {
    
    /**Computer move variable to hold the col selected by the computer. */
    private int compMove; 
    
   /**computerPlayer()  method to have the computer make its move
     * on the game board.
     */
    public void computerPlayer()
    {
        //System.out.println("Computer, please choose a column between 1-7.");
        
        Random rand = new Random();
        compMove = rand.nextInt(7);  
        System.out.println("Computer selected column " + (compMove + 1) + ".");       
    }
    
    
    /**getComputerCol()     Accessor to return chosen computer column. 
     * @return              int compMove.
     */
    public int getComputerCol()
    {
        return compMove;
    }
}

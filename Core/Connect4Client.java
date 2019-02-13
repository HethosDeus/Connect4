package core;


import core.Connect4Constants;
import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

/**
 * This is the client application for Connect4 to play online games.
 * <p>
 * Required for Functionality:
 * <ul>
 * <li>Connect4TextConsole.java
 * <li>Connect4ComputerPlayer.java
 * <li>Connect4.java
 * <li>Connect4GUI.java
 * <li>Connect4Server.java
 * <li>Connect4Constants.java
 * </ul>
 * <p>
 * @author Adam Clifton
 * @version 1.0
 */

public class Connect4Client extends Application implements Connect4Constants {
  
    /**Indicate whether the player has the turn. */
    private boolean myTurn = false;
    /**Indicate the token for the player.*/
    private String myToken = " ";
    /**Indicate the token for the other player. */
    private String otherToken = " ";
    /**Create and initialize cells. */
    private Cell[][] cell = new Cell[ROW][COL];
    /**Create and initialize a title label. */
    private Label lblTitle = new Label();
    /**Create and initialize a status label. */
    private Label lblStatus = new Label();
    /**Indicate selected row by the current move. */
    //private int rowSelected;
    /**Indicate selected column by the current move. */
    private int columnSelected;
    /**Input stream from/to server. */
    private DataInputStream fromServer;
    /**Output streams from/to server. */
    private DataOutputStream toServer;
    /** Flag to continue to play. */
    private boolean continueToPlay = true;
    /**Wait for the player to mark a cell. */
    private boolean waiting = true;
    /**Host name or ip. */
    private String host = "localhost";

    /**
     * Start method to launch client gui.
     * @param primaryStage builds game inferface window.
     */
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Pane to hold cell
        GridPane pane = new GridPane();
        pane.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, 
                    CornerRadii.EMPTY, Insets.EMPTY)));
        for (int i = 0; i < ROW; i++) 
        {
            for (int j = 0; j < COL; j++) 
            {
                pane.add(cell[i][j] = new Cell(i, j), j, i);
            }
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(lblTitle);
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);

        // Create a scene and place it in the stage
        Scene scene = new Scene(borderPane, 600, 550);
        primaryStage.setTitle("Connect Four: Online"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage   

        // Connect to the server
        connectToServer();
    }

    /**
     * Method to connect to server.
     */
    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket(host, 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Control the game on a separate thread
        new Thread(() -> {
            try {
                // Get notification from the server
                int player = fromServer.readInt();

                // Am I player 1 or 2?
                if (player == PLAYER1) {
                    myToken = RED;
                    otherToken = YELLOW;
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 1 with token RED");
                        lblStatus.setText("Waiting for player 2 to join");
                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored

                    // The other player has joined
                    Platform.runLater(()
                            -> lblStatus.setText("Player 2 has joined. I start first"));

                    // It is my turn
                    myTurn = true;
                } else if (player == PLAYER2) {
                    myToken = YELLOW;
                    otherToken = RED;
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 2 with token YELLOW");
                        lblStatus.setText("Waiting for player 1 to move");
                    });
                }

                // Continue to play
                while (continueToPlay) {
                    if (player == PLAYER1) {
                        waitForPlayerAction(); // Wait for player 1 to move
                        sendMove(); // Send the move to the server
                        receiveInfoFromServer(); // Receive info from the server
                    } else if (player == PLAYER2) {
                        receiveInfoFromServer(); // Receive info from the server
                        waitForPlayerAction(); // Wait for player 2 to move
                        sendMove(); // Send player 2's move to the server
                        
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Method to wait for the player to mark a cell.
     * @throws InterruptedException
     */
    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }

    /**
     * Method to send this player's move to the server.
     * @throws IOException
     */
    private void sendMove() throws IOException {
   //     toServer.writeInt(rowSelected); // Send the selected row
        toServer.writeInt(columnSelected); // Send the selected column
    }

        
    /**
     * Method to receive info from the server.
     * @throws IOException
     */
    private void receiveInfoFromServer() throws IOException {
        // Receive game status
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            // Player 1 won, stop playing
            continueToPlay = false;
            if (myToken == RED) {
                Platform.runLater(() -> lblStatus.setText("I won! (RED)"));
            } else if (myToken == YELLOW) {
                Platform.runLater(()
                        -> lblStatus.setText("Player 1 (RED) has won!"));
                receiveMove();
            }
        } else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            if (myToken == YELLOW) {
                Platform.runLater(() -> lblStatus.setText("I won! (YELLOW)"));
            } else if (myToken == RED) {
                Platform.runLater(()
                        -> lblStatus.setText("Player 2 (YELLOW) has won!"));
                receiveMove();
            }
        } else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            Platform.runLater(()
                    -> lblStatus.setText("Game is over, no winner!"));

            if (myToken == YELLOW) {
                receiveMove();
            }
        } else {
            receiveMove();
            Platform.runLater(() -> lblStatus.setText("My turn"));
            myTurn = true; // It is my turn
        }
    }

    /**
     * Method to receive opponent's move from the server.
     * @throws IOException
     */
    private void receiveMove() throws IOException {
        // Get the other player's move
        int row = fromServer.readInt();
        int column = fromServer.readInt();
        Platform.runLater(() -> cell[row][column].setToken(otherToken));
    }

    /**Inner class to build each cell of the game. */
    public class Cell extends Pane {
       
        /** Row of the cell in the board.*/
        private int row;
        /** Column of the cell in the board.*/
        private int column;
        /**Token used for this cell. */
        private String token = " ";
        
        /**
         * Constructor to build each cell in game.
         * @param row 
         * @param column
         */
        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
            this.setPrefSize(2000, 2000); // What happens without this?
            setStyle("-fx-border-color: black"); // Set cell's border
            this.setOnMouseClicked(e -> handleMouseClick());
        }

        /**
         * Get current token.
         * @return token
         */
        public String getToken() {
            return token;
        }

        /**
         * Set a new token.
         * @param s String to be taken and set to token.
         */
        public void setToken(String s) {
            token = s;
            repaint();
        }
        

        /**
         * Method to add red and yellow tokens to the game board.
         */
        protected void repaint() {
            if (token == RED) {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.RED);
               
                getChildren().add(ellipse); // Add the ellipse to the pane
                
            } else if (token == YELLOW) {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.YELLOW);

                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }

        /**
         * Handles a mouse click event.
         */
        private void handleMouseClick() {
            // If cell is not occupied and the player has the turn
            if (token == " " && myTurn) {
            //    Connect4Server move = new Connect4Server();
            
            /*This setToken() adds shape to wrong cell.  Tried troubleshooting, 
            but have not been able to fix without breaking some other part of
            the game, so I am leaving it here for now.*/
                setToken(myToken);  // Set the player's token in the cell
                myTurn = false;
             //   rowSelected = row;
                columnSelected = column;
                lblStatus.setText("Waiting for the other player to move");
                waiting = false; // Just completed a successful move
            }
        }
    }
}

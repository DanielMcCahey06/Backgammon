import java.util.Scanner;

public class Match {
    public static int pointsToWin; // the amount of points needed to win
    public static boolean playAgain = true;
    private Scanner scanner; // Declare the scanner field

    public Match(Scanner scanner){
        this.scanner = scanner;
    }

    protected Game createGame() {
        return new Game();
    }

    public void playMatch() {
        while (playAgain) {
            System.out.println("Welcome to Backgammon");

            getMatchPoints();

            // Get player names
            Player player1 = getPlayerDetails(1, Checker.Colour.WHITE);
            Player player2 = getPlayerDetails(2, Checker.Colour.BLACK);

            while (player1.getScore() < pointsToWin && player2.getScore() < pointsToWin) {
                Game game = createGame();
                // Break the loop if the game was quit
                boolean gameQuit = game.start(player1, player2);
                if (gameQuit) {
                    System.out.println("Game was quit. Exiting match...");
                    playAgain = false;
                    return; // Exit match loop
                }
            }

            displayMatchWinner(player1, player2);
            askToPlayAgain();
        }
    }

    /**
     * Prompts the user to enter the number of points needed to win the match.
     */
    public void getMatchPoints() {
        System.out.println("How many points would you like to play to?");
        boolean validPointsInput = false;

        while (!validPointsInput) {
            if (scanner.hasNextInt()) {
                pointsToWin = scanner.nextInt();
                scanner.nextLine(); // Clear the newline left in the buffer

                if (pointsToWin > 0) {
                    validPointsInput = true;
                } else {
                    System.out.println("Please enter a positive integer for points to win:");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer:");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    /**
     * Prompts the user to enter player details.
     *
     * @param playerNumber The player number (1 or 2).
     * @param colour       The checker color for the player.
     * @return A Player object representing the player.
     */
    public Player getPlayerDetails(int playerNumber, Checker.Colour colour) {
        System.out.println("Enter name " + playerNumber + " (" + colour + " Checker):");
        String playerName = scanner.nextLine();
        return new Player(playerName, colour);
    }

    /**
     * Displays the match winner based on the scores.
     *
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public void displayMatchWinner(Player player1, Player player2) {
        if (player1.getScore() >= pointsToWin) {
            Game.outputMessage(player1.getName() + " has won the match, congratulations! ");
        }
        if (player2.getScore() >= pointsToWin) {
            Game.outputMessage(player2.getName() + " has won the match, congratulations! ");
        }
    }

    /**
     * Asks the user if they want to play another match.
     */
    public void askToPlayAgain() {
        String userResponse;
        boolean validResponse = false;

        System.out.println("Would you like to play again? (Y/N):");
        while (!validResponse) {
            userResponse = scanner.nextLine();
            if (userResponse.equalsIgnoreCase("Y")) {
                validResponse = true;
                playAgain = true;
            } else if (userResponse.equalsIgnoreCase("N")) {
                validResponse = true;
                playAgain = false;
            } else {
                System.out.println("Invalid response. Please enter 'Y' or 'N':");
            }
        }
    }
}

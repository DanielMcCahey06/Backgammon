
import java.util.Scanner;
import java.util.*;
public class Match {
    public static int pointsToWin = 0; //the amount of points needed to win
    public static boolean playAgain = true;
    static boolean validPoints = false;

    public static void playMatch() {
        while(playAgain) {
            Game.outputMessage("Welcome to Backgammon");
            System.out.println();
            Scanner scanner = new Scanner(System.in);// allows reading from the console
            //Find out how much points to win the match
            while (!validPoints) {
                Game.outputMessage("How much points would you like to play to?");
                if (scanner.hasNextInt()) {
                    pointsToWin = scanner.nextInt();
                    validPoints = true;
                } else {
                    Game.errorMessage("Invalid Input. Must be an Integer");
                    scanner.nextLine();
                }
            }
            scanner.nextLine(); //clear the new line left in the buffer

            //Get player names
            Game.outputMessage("Enter name 1 (White Checker):");
            String player1Name = scanner.nextLine();
            Player player1 = new Player(player1Name, Checker.Colour.WHITE);

            Game.outputMessage("Enter name 2 (Black Checker):");
            String player2Name = scanner.nextLine();
            Player player2 = new Player(player2Name, Checker.Colour.BLACK);

            while (player1.getScore() < pointsToWin && player2.getScore() < pointsToWin) {
                Game game = new Game();
                game.start(player1, player2);
            }

            if (player1.getScore() == pointsToWin || player1.getScore() > pointsToWin) {
                Game.outputMessage(player1.getName() + " has won the match, congratulations! ");
            }
            if (player2.getScore() == pointsToWin || player2.getScore() > pointsToWin) {
                Game.outputMessage(player2.getName() + " has won the match, congratulations! ");
            }

            String userResponse;
            boolean validResponse = false;
            System.out.println("Would you like to play again? (Y/N):");
            userResponse = scanner.nextLine();

            while(!validResponse) {
                if (userResponse.equalsIgnoreCase("Y")) {
                    validResponse = true;
                    playAgain = true;
                }
                else {
                    validResponse = true;
                    playAgain = false;
                }
            }
        }
    }
}

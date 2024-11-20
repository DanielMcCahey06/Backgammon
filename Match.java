import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class Match {
    private int pointsToWin; //the amount of points needed to win


    public void playMatch() {
        System.out.println("Welcome to Backgammon");
        System.out.println();
        //Find out how much points to win the match
        System.out.println("How much points would you like to play to?");
        Scanner scanner = new Scanner(System.in); // allows reading from the console
        int pointsToWin = scanner.nextInt();

        //Get player names
        System.out.println("Enter name 1 (White Checker):");
        String player1Name = scanner.nextLine();
        Player player1 = new Player(player1Name, Checker.Colour.WHITE);

        System.out.println("Enter name 2 (Black Checker):");
        String player2Name = scanner.nextLine();
        Player player2 = new Player(player2Name, Checker.Colour.BLACK);

        while (player1.getScore()<pointsToWin && player2.getScore()<pointsToWin){
            Game game = new Game();
            game.start(player1, player2);

        }
        if (player1.getScore() = pointsToWin){
            outputmessage(player1.getName() + " has won the match, congratulations! ")
        }
        if (player2.getScore() = pointsToWin){
            outputmessage(player2.getName() + " has won the match, congratulations! ")
        }





    }
}

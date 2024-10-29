import java.util.Scanner;

public class Game {
    private boolean running = true;

    private void initialiseGame() {

        System.out.println("Welcome to Backgammon");
        System.out.println();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in); // allows reading from the console
        while (running) {
            Board.display();

            System.out.println("Enter move:");
            String action = scanner.nextLine(); // stores user input into 'action'

        }
        scanner.close();
    }
}

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object for reading user input
        Match match = new Match(scanner); // Initialize a new Match object with the scanner
        match.playMatch(); // Start the match by calling the playMatch method
    }
}







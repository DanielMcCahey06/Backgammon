import java.util.Scanner;

class TestMatch extends Match {
    // Constructor initializes TestMatch with a scanner object
    public TestMatch(Scanner scanner) {
        super(scanner); // Call the parent constructor to initialize the match with the scanner
    }

    // Simulates the playMatch method and logs a message
    @Override
    public void playMatch() {
        System.out.println("Simulated playMatch called."); // Simulated output for testing
    }
}

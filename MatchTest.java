import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Set up the output stream to capture console output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testGetMatchPoints_ValidInput() {
        String input = "5\n"; // Simulate user input for points to win
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes())); // Create scanner with input
        Match match = new Match(scanner); // Create Match instance with scanner

        match.getMatchPoints(); // Call the method to get match points

        assertEquals(5, Match.pointsToWin); // Verify that points to win is set to 5
        assertTrue(outputStream.toString().contains("How many points would you like to play to?")); // Ensure prompt was shown
    }

    @Test
    void testGetMatchPoints_InvalidThenValidInput() {
        String input = "abc\n10\n"; // Simulate invalid input followed by valid input
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes())); // Create scanner with input
        Match match = new Match(scanner); // Create Match instance with scanner

        match.getMatchPoints(); // Call the method to get match points

        assertEquals(10, Match.pointsToWin); // Verify that points to win is set to 10 after valid input
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input. Please enter a valid integer:")); // Ensure error message was shown
    }

    @Test
    void testCreateGame() {
        Match testMatch = new Match(new Scanner(System.in)) {
            @Override
            public Game createGame() {
                return super.createGame(); // Call the parent createGame method
            }
        };

        Game game = testMatch.createGame(); // Create game instance using the method

        assertNotNull(game, "The createGame method should return a non-null Game object"); // Ensure a game is created
        assertInstanceOf(Game.class, game, "The createGame method should return an instance of Game"); // Ensure the object is of type Game
    }

    @Test
    void testGetPlayerDetails() {
        String input = "Alice\n"; // Simulate user input for player name
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes())); // Create scanner with input
        Match match = new Match(scanner); // Create Match instance with scanner

        Player player = match.getPlayerDetails(1, Checker.Colour.WHITE); // Get player details for player 1

        assertNotNull(player); // Ensure the player object is not null
        assertEquals("Alice", player.getName()); // Verify player name is set correctly
        assertEquals(Checker.Colour.WHITE, player.getChecker()); // Verify player color is set correctly
        assertTrue(outputStream.toString().contains("Enter name 1 (WHITE Checker):")); // Ensure prompt was shown
    }

    @Test
    void testDisplayMatchWinner_Player1Wins() {
        Player player1 = new Player("Alice", Checker.Colour.WHITE);
        player1.setScore(5); // Set player 1's score
        Player player2 = new Player("Bob", Checker.Colour.BLACK);
        player2.setScore(3); // Set player 2's score

        Match.pointsToWin = 5; // Set points to win
        Match match = new Match(new Scanner(System.in)); // Create Match instance

        match.displayMatchWinner(player1, player2); // Display match winner

        String output = outputStream.toString();
        assertTrue(output.contains("Alice has won the match, congratulations!")); // Ensure player 1's victory is shown
    }

    @Test
    void testDisplayMatchWinner_Player2Wins() {
        Player player1 = new Player("Alice", Checker.Colour.WHITE);
        player1.setScore(3); // Set player 1's score
        Player player2 = new Player("Bob", Checker.Colour.BLACK);
        player2.setScore(5); // Set player 2's score

        Match.pointsToWin = 5; // Set points to win
        Match match = new Match(new Scanner(System.in)); // Create Match instance

        match.displayMatchWinner(player1, player2); // Display match winner

        String output = outputStream.toString();
        assertTrue(output.contains("Bob has won the match, congratulations!")); // Ensure player 2's victory is shown
    }

    @Test
    void testAskToPlayAgain_YesResponse() {
        String input = "Y\n"; // Simulate user input for "Yes"
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes())); // Create scanner with input
        Match match = new Match(scanner); // Create Match instance

        match.askToPlayAgain(); // Call method to ask if the user wants to play again

        assertTrue(Match.playAgain); // Verify that playAgain is true
        assertTrue(outputStream.toString().contains("Would you like to play again? (Y/N):")); // Ensure prompt was shown
    }

    @Test
    void testAskToPlayAgain_NoResponse() {
        String input = "N\n"; // Simulate user input for "No"
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes())); // Create scanner with input
        Match match = new Match(scanner); // Create Match instance

        match.askToPlayAgain(); // Call method to ask if the user wants to play again

        assertFalse(Match.playAgain); // Verify that playAgain is false
        assertTrue(outputStream.toString().contains("Would you like to play again? (Y/N):")); // Ensure prompt was shown
    }

    @Test
    void testAskToPlayAgain_InvalidThenValidResponse() {
        String input = "X\nY\n"; // Simulate invalid input followed by valid input
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes())); // Create scanner with input
        Match match = new Match(scanner); // Create Match instance

        match.askToPlayAgain(); // Call method to ask if the user wants to play again

        assertTrue(Match.playAgain); // Verify that playAgain is true after valid input
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid response. Please enter 'Y' or 'N':")); // Ensure error message was shown for invalid input
    }

    @Test
    void testPlayMatch_Player1Wins() {
        String input = "3\nAlice\nBob\nN\n"; // Simulate input for match points, player names, and play again response
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes())); // Create scanner with input

        Game mockGame = new Game() {
            private int playCount = 0;

            @Override
            public boolean start(Player player1, Player player2) {
                playCount++;
                if (playCount <= 2) {
                    player1.setScore(player1.getScore() + 1); // Increment player 1's score
                }
                return false;
            }
        };

        Match testMatch = new Match(scanner) {
            @Override
            protected Game createGame() {
                return mockGame; // Use the mockGame for testing
            }
        };

        Match.playAgain = true; // Set playAgain to true
        testMatch.playMatch(); // Start the match

        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Backgammon")); // Ensure game welcome message is shown
        assertTrue(output.contains("Alice has won the match, congratulations!")); // Ensure player 1's victory message is shown
        assertTrue(output.contains("Would you like to play again? (Y/N):")); // Ensure prompt for playing again is shown
    }

}



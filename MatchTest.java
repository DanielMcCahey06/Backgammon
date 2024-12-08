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
        String input = "5\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Match match = new Match(scanner);

        match.getMatchPoints();

        assertEquals(5, Match.pointsToWin);
        assertTrue(outputStream.toString().contains("How many points would you like to play to?"));
    }

    @Test
    void testGetMatchPoints_InvalidThenValidInput() {
        String input = "abc\n10\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Match match = new Match(scanner);

        match.getMatchPoints();

        assertEquals(10, Match.pointsToWin);
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid input. Please enter a valid integer:"));
    }

    @Test
    void testCreateGame() {
        Match testMatch = new Match(new Scanner(System.in)) {
            @Override
            public Game createGame() {
                return super.createGame();
            }
        };

        Game game = testMatch.createGame();

        assertNotNull(game, "The createGame method should return a non-null Game object");
        assertInstanceOf(Game.class, game, "The createGame method should return an instance of Game");
    }

    @Test
    void testGetPlayerDetails() {
        String input = "Alice\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Match match = new Match(scanner);

        Player player = match.getPlayerDetails(1, Checker.Colour.WHITE);

        assertNotNull(player);
        assertEquals("Alice", player.getName());
        assertEquals(Checker.Colour.WHITE, player.getChecker());
        assertTrue(outputStream.toString().contains("Enter name 1 (WHITE Checker):"));
    }

    @Test
    void testDisplayMatchWinner_Player1Wins() {
        Player player1 = new Player("Alice", Checker.Colour.WHITE);
        player1.setScore(5);
        Player player2 = new Player("Bob", Checker.Colour.BLACK);
        player2.setScore(3);

        Match.pointsToWin = 5;
        Match match = new Match(new Scanner(System.in));

        match.displayMatchWinner(player1, player2);

        String output = outputStream.toString();
        assertTrue(output.contains("Alice has won the match, congratulations!"));
    }

    @Test
    void testDisplayMatchWinner_Player2Wins() {
        Player player1 = new Player("Alice", Checker.Colour.WHITE);
        player1.setScore(3);
        Player player2 = new Player("Bob", Checker.Colour.BLACK);
        player2.setScore(5);

        Match.pointsToWin = 5;
        Match match = new Match(new Scanner(System.in));

        match.displayMatchWinner(player1, player2);

        String output = outputStream.toString();
        assertTrue(output.contains("Bob has won the match, congratulations!"));
    }

    @Test
    void testAskToPlayAgain_YesResponse() {
        String input = "Y\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Match match = new Match(scanner);

        match.askToPlayAgain();

        assertTrue(Match.playAgain);
        assertTrue(outputStream.toString().contains("Would you like to play again? (Y/N):"));
    }

    @Test
    void testAskToPlayAgain_NoResponse() {
        String input = "N\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Match match = new Match(scanner);

        match.askToPlayAgain();

        assertFalse(Match.playAgain);
        assertTrue(outputStream.toString().contains("Would you like to play again? (Y/N):"));
    }

    @Test
    void testAskToPlayAgain_InvalidThenValidResponse() {
        String input = "X\nY\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Match match = new Match(scanner);

        match.askToPlayAgain();

        assertTrue(Match.playAgain);
        String output = outputStream.toString();
        assertTrue(output.contains("Invalid response. Please enter 'Y' or 'N':"));
    }

    @Test
    void testPlayMatch_Player1Wins() {
        String input = "3\nAlice\nBob\nN\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        Game mockGame = new Game() {
            private int playCount = 0;

            @Override
            public boolean start(Player player1, Player player2) {
                playCount++;
                if (playCount <= 2) {
                    player1.setScore(player1.getScore() + 1);
                }
                return false;
            }
        };

        Match testMatch = new Match(scanner) {
            @Override
            protected Game createGame() {
                return mockGame;
            }
        };

        Match.playAgain = true;
        testMatch.playMatch();

        String output = outputStream.toString();
        assertTrue(output.contains("Welcome to Backgammon"));
        assertTrue(output.contains("Alice has won the match, congratulations!"));
        assertTrue(output.contains("Would you like to play again? (Y/N):"));
    }
}



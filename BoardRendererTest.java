import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardRendererTest {

    private ByteArrayOutputStream outputStream;
    private StubBoard board;
    private BoardRenderer boardRenderer;

    @BeforeEach
    void setUp() {
        // Capture System.out output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Initialize stub board and renderer
        board = new StubBoard();
        boardRenderer = new BoardRenderer(board);
    }

    @Test
    void testDisplay_EmptyBoard() {
        // Ensure all piles are empty (already default in StubBoard)

        // Display the board
        boardRenderer.display();

        // Capture the output
        String output = outputStream.toString();

        // Validate the output contains empty board indicators
        assertTrue(output.contains("[⎺⎺⎺⎺⎺⎺⎺⎺⎺]"), "Should display empty board markers");
    }

    @Test
    void testDisplay_FilledBoard() {
        // Add checkers to specific piles
        board.getStubPile(5).addChecker(new Checker(5, Checker.Colour.WHITE));
        board.getStubPile(5).addChecker(new Checker(5, Checker.Colour.BLACK));
        board.getStubBarPile(0).addChecker(new Checker(-1, Checker.Colour.WHITE)); // Bar checkers have no position
        board.getStubHomePile(1).addChecker(new Checker(-1, Checker.Colour.BLACK)); // Home checkers have no position

        // Display the board
        boardRenderer.display();

        // Capture the output
        String output = outputStream.toString();

        // Validate specific parts of the board are displayed
        assertTrue(output.contains("13 14 15 16 17 18"), "Should display top pip numbers");
        assertTrue(output.contains("⚪"), "Should display WHITE checkers");
        assertTrue(output.contains("⚫"), "Should display BLACK checkers");
    }

    @Test
    void testUpdate() {
        // Simulate a board state change
        board.getStubPile(12).addChecker(new Checker(12, Checker.Colour.WHITE));
        board.getStubPile(18).addChecker(new Checker(18, Checker.Colour.BLACK));

        // Call update (observer method)
        boardRenderer.update(board);

        // Capture the output
        String output = outputStream.toString();

        // Validate the board is re-rendered
        assertTrue(output.contains("⚪"), "Should display the updated WHITE checker");
        assertTrue(output.contains("⚫"), "Should display the updated BLACK checker");
    }
}






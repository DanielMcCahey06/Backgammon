import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private StubBoard stubBoard;
    private TestGame testGame;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        // Initialize StubBoard
        stubBoard = new StubBoard();
        game = new Game() {
            @Override
            protected Board createBoard() {
                return stubBoard;
            }
        };
        // Initialize and direct System.out
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }

    @Test
    void testProcessMove() {
        // Set up the starting pile with one checker
        Checker whiteChecker = new Checker(0, Checker.Colour.WHITE);
        stubBoard.getStubPile(0).addChecker(whiteChecker);

        // Ensure the target pile is empty
        stubBoard.getStubPile(4).clear();

        // Prepare arguments for processMove
        String[] moveParts = {"1", "5"}; // From pile 1 to pile 5
        int moveDistance = 4;
        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);

        // Perform the move
        boolean result = game.processMove(moveParts, moveDistance, currentPlayer);

        // Validate results
        assertTrue(result);
        assertEquals(0, stubBoard.getStubPile(0).getNoOfCheckers());
        assertEquals(1, stubBoard.getStubPile(4).getNoOfCheckers());
        assertEquals(Checker.Colour.WHITE, stubBoard.getStubPile(4).get(0).getColour());
    }

    @Test
    void testProcessMoveWithCapture() {
        // Set up the starting pile with a white checker
        Checker whiteChecker = new Checker(0, Checker.Colour.WHITE);
        stubBoard.getStubPile(0).addChecker(whiteChecker);

        // Set up the target pile with a black checker
        Checker blackChecker = new Checker(4, Checker.Colour.BLACK);
        stubBoard.getStubPile(4).addChecker(blackChecker);

        // Ensure the bar pile is empty
        stubBoard.getStubBarPile(Board.PLAYER2BARINDEX).clear();

        // Prepare arguments for processMove
        String[] moveParts = {"1", "5"}; // From pile 1 to pile 5
        int moveDistance = 4;
        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);

        // Perform the move
        boolean result = game.processMove(moveParts, moveDistance, currentPlayer);

        // Validate results
        assertTrue(result);
        assertEquals(1, stubBoard.getStubPile(4).getNoOfCheckers());
        assertEquals(Checker.Colour.WHITE, stubBoard.getStubPile(4).get(0).getColour());
        assertEquals(1, stubBoard.getStubBarPile(Board.PLAYER2BARINDEX).getNoOfCheckers());
        assertEquals(Checker.Colour.BLACK, stubBoard.getStubBarPile(Board.PLAYER2BARINDEX).get(0).getColour());
    }

    @Test
    void testProcessMoveToHomePile() {
        // Set up a white checker near the home pile
        Checker whiteChecker = new Checker(22, Checker.Colour.WHITE);
        stubBoard.getStubPile(22).addChecker(whiteChecker);

        // Ensure the home pile is empty
        stubBoard.getStubHomePile(Board.PLAYER1HOMEINDEX).clear();

        // Prepare arguments for processMove
        String[] moveParts = {"23", "off"}; // From pile 23 to home
        int moveDistance = 1; // Moving off the board
        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);

        // Perform the move
        boolean result = game.processMove(moveParts, moveDistance, currentPlayer);

        // Validate results
        assertTrue(result);
        assertEquals(1, stubBoard.getStubHomePile(Board.PLAYER1HOMEINDEX).getNoOfCheckers());
        assertEquals(Checker.Colour.WHITE, stubBoard.getStubHomePile(Board.PLAYER1HOMEINDEX).get(0).getColour());
        assertEquals(0, stubBoard.getStubPile(22).getNoOfCheckers());
    }

    @Test
    void testCreateBoard() {
        // Invoke the createBoard method
        Board createdBoard = game.createBoard();

        // Validate that the returned object is not null and is of type Board
        assertNotNull(createdBoard, "createBoard should return a non-null Board object");
        assertInstanceOf(Board.class, createdBoard, "createBoard should return an instance of Board");
    }

    @Test
    void testProcessActionHint() {
        // Test the "HINT" action
        boolean result = game.processAction("HINT", new Player("Alice", Checker.Colour.WHITE), new Player("Bob", Checker.Colour.BLACK), 2);
        assertFalse(result, "HINT action should return false");
        // Validate that gameHelp was called (mocked/stubbed in actual test implementation)
    }

    @Test
    void testProcessActionInvalid() {
        // Test an invalid action
        boolean result = game.processAction("INVALID", new Player("Alice", Checker.Colour.WHITE), new Player("Bob", Checker.Colour.BLACK), 2);
        assertFalse(result, "Invalid action should return false");
    }

    @Test
    void testProcessActionQuit() {
        // Test the "Q" action
        boolean result = game.processAction("Q", new Player("Alice", Checker.Colour.WHITE), new Player("Bob", Checker.Colour.BLACK), 2);
        assertFalse(result, "Q action should return false");
        assertFalse(Game.gameInPlay, "gameInPlay should be set to false after 'Q'");
    }

    @Test
    void testProcessActionDouble() {
        TestGame testGame = new TestGame();

        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player otherPlayer = new Player("Bob", Checker.Colour.BLACK);

        Game.gameInPlay = true;

        boolean result = testGame.processAction("DOUBLE", currentPlayer, otherPlayer, 2);

        assertTrue(testGame.handleDoubleRequestCalled, "handleDoubleRequest should be called for DOUBLE action.");
        assertFalse(result, "DOUBLE action should return false if gameInPlay is set to false by handleDoubleRequest.");
    }

    @Test
    void testProcessActionRoll() {
        TestGame testGame = new TestGame();

        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player otherPlayer = new Player("Bob", Checker.Colour.BLACK);

        boolean result = testGame.processAction("ROLL", currentPlayer, otherPlayer, 2);

        assertTrue(testGame.handleRollCalled, "handleRoll should be called for ROLL action.");
        assertTrue(result, "ROLL action should return true if handleRoll completes the turn.");
    }

    @Test
    void testProcessActionPip() {
        TestGame testGame = new TestGame();

        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player otherPlayer = new Player("Bob", Checker.Colour.BLACK);

        boolean result = testGame.processAction("PIP", currentPlayer, otherPlayer, 2);

        assertTrue(testGame.pipCountCalled, "pipCount should be called for PIP action.");
        assertFalse(result, "PIP action should return false.");
    }

    @Test
    void testProcessActionDice() {
        TestGame testGame = new TestGame();

        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player otherPlayer = new Player("Bob", Checker.Colour.BLACK);

        boolean result = testGame.processAction("DICE", currentPlayer, otherPlayer, 2);

        assertTrue(testGame.setDiceCalled, "setDice should be called for DICE action.");
        assertFalse(result, "DICE action should return false.");
    }

    @Test
    void testStartOriginalMethod() {
        Game game = new Game();
        Player player1 = new Player("Alice", Checker.Colour.WHITE);
        Player player2 = new Player("Bob", Checker.Colour.BLACK);

        // Mock user input to simulate the game behavior
        // You may need to update this depending on how `start` interacts with Scanner or input
        boolean result = game.start(player1, player2);

        // Validate the result based on expected behavior
        assertFalse(result, "Game should not quit unexpectedly.");
    }

    @Test
    void testProcessMoveFromBarDirect() {
        // Set up the bar pile with one white checker
        Checker whiteChecker = new Checker(0, Checker.Colour.WHITE);
        stubBoard.getStubBarPile(Board.PLAYER1BARINDEX).addChecker(whiteChecker);

        // Ensure the target pile is empty
        stubBoard.getStubPile(4).clear();

        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);

        // Directly call processMoveFromBar
        boolean result = game.processMoveFromBar(currentPlayer, 5);

        // Validate results
        assertTrue(result);
        assertEquals(1, stubBoard.getStubPile(4).getNoOfCheckers());
        assertEquals(Checker.Colour.WHITE, stubBoard.getStubPile(4).get(0).getColour());
        assertEquals(0, stubBoard.getStubBarPile(Board.PLAYER1BARINDEX).getNoOfCheckers());
    }

    @Test
    void testGenerateNonDoubleMoves() {
        // Test setup
        Checker whiteChecker = new Checker(11, Checker.Colour.WHITE); // Position 11 (12th pile)
        stubBoard.getStubPile(11).addChecker(whiteChecker);

        int[] roll = {3, 5}; // Dice rolls
        boolean[] usedDice = {false, false}; // No dice used
        List <String> legalMoves = new ArrayList<>();
        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);

        // Case 1: Standard moves
        game.generateNonDoubleMoves(roll, usedDice, 4, legalMoves, currentPlayer, false, -1);

        assertEquals(1, legalMoves.size(), "One move should be generated");
        assertTrue(legalMoves.contains("Move 5-2"), "Move 5-2 should be in the legal moves");

        // Case 2: All in home range
        legalMoves.clear();
        game.generateNonDoubleMoves(roll, usedDice, 4, legalMoves, currentPlayer, true, -1);

        //assertEquals(3, legalMoves.size(), "Three moves should be generated (including move-off)");
        assertTrue(legalMoves.contains("Move 5-2"), "Move 4-1 should be in the legal moves");
        assertTrue(legalMoves.contains("Move 5-off"), "Move 4-off should be in the legal moves");

        // Case 3: Used dice
        legalMoves.clear();
        usedDice[0] = true; // First dice is used
        game.generateNonDoubleMoves(roll, usedDice, 11, legalMoves, currentPlayer, false, -1);

        assertEquals(1, legalMoves.size(), "One move should be generated with one dice already used");
        assertTrue(legalMoves.contains("Move 12-7"), "Move 12-7 should be in the legal moves");

        // Case 4: Illegal moves
        legalMoves.clear();
        usedDice[1] = true;
        usedDice[0] = true;
        stubBoard.getStubPile(6).addChecker(new Checker(6, Checker.Colour.BLACK)); // Pile position 7
        game.generateNonDoubleMoves(roll, usedDice, 11, legalMoves, currentPlayer, false, -1);

        assertEquals(0, legalMoves.size(), "No legal moves should be generated due to blockage");
    }

    @Test
    void testGenerateDoubleMoves() {
        // Set up a white checker at position 12
        Checker whiteChecker = new Checker(11, Checker.Colour.WHITE);
        stubBoard.getStubPile(11).addChecker(whiteChecker);

        int roll = 3; // Double roll
        boolean[] usedDice = {false, false, false, false};
        List<String> legalMoves = new ArrayList<>();
        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);

        // Test a valid move
        game.generateDoubleMoves(roll, usedDice, 11, legalMoves, currentPlayer, false, -1);
        assertEquals(1, legalMoves.size());
        assertTrue(legalMoves.contains("Move 12-9"));

        // Test move to home
        legalMoves.clear();
        game.generateDoubleMoves(roll, usedDice, 2, legalMoves, currentPlayer, true, -1);
        assertEquals(1, legalMoves.size());
        assertTrue(legalMoves.contains("Move 3-off"));

        // Test when dice is already used
        legalMoves.clear();
        usedDice[0] = true;
        usedDice[1] = true;
        usedDice[2] = true;
        usedDice[3] = true;
        game.generateDoubleMoves(roll, usedDice, 11, legalMoves, currentPlayer, false, -1);
        assertTrue(legalMoves.isEmpty());
    }

    @Test
    void testIsLegalTarget() {
        // Set up a legal target
        Checker.Colour white = Checker.Colour.WHITE;
        stubBoard.getStubPile(5).addChecker(new Checker(5, white));

        // Ensure the target is valid
        assertTrue(game.isLegalTarget(5, white), "Position 5 should be a legal target for WHITE checkers");

        // Test invalid positions (outside of board range)
        assertFalse(game.isLegalTarget(-1, white), "Negative position should not be a legal target");
        assertFalse(game.isLegalTarget(24, white), "Position outside the board should not be a legal target");

        // Test a position with an opposing checker
        Checker.Colour black = Checker.Colour.BLACK;
        stubBoard.getStubPile(6).addChecker(new Checker(6, black));
        assertTrue(game.isLegalTarget(6, white), "Position 6 should be legal even with an opposing checker (capturable)");
    }

    @Test
    void testGetHomePosition() {
        // Test for direction 1 (Black checkers moving to home position 24)
        int blackHome = game.getHomePosition(1);
        assertEquals(24, blackHome, "Direction 1 should return home position 24 for Black checkers");

        // Test for direction -1 (White checkers moving to home position -1)
        int whiteHome = game.getHomePosition(-1);
        assertEquals(-1, whiteHome, "Direction -1 should return home position -1 for White checkers");
    }

    @Test
    void testAllCheckersInHomeRange() {
        Player blackPlayer = new Player("Bob", Checker.Colour.BLACK);
        Player whitePlayer = new Player("Alice", Checker.Colour.WHITE);

        // Case 1: A black checker outside the home range
        stubBoard.getStubPile(17).addChecker(new Checker(17, Checker.Colour.BLACK));
        assertFalse(game.allCheckersInHomeRange(blackPlayer), "A black checker is outside the home range.");

        // Reset the board
        stubBoard = new StubBoard();

        // Case 2: A white checker outside the home range
        stubBoard.getStubPile(6).addChecker(new Checker(6, Checker.Colour.WHITE));
        assertFalse(game.allCheckersInHomeRange(whitePlayer), "A white checker is outside the home range.");
    }

    @Test
    public void testCheckIfDoubles() {
        int[] roll = {3, 3};
        boolean result = game.checkIfDoubles(roll);
        assertTrue(result);

        int[] roll1 = {1, 4};
        boolean result1 = game.checkIfDoubles(roll1);
        assertFalse(result1);

    }

    @Test
    public void testGenerateDiceMarker() {

        // Act & Assert: Test when it's doubles
        boolean[] resultDoubles = game.generateDiceMarker(true);
        assertArrayEquals(new boolean[]{false, false, false, false}, resultDoubles, "Expected 4 'false' values for doubles.");

        // Act & Assert: Test when it's not doubles
        boolean[] resultNonDoubles = game.generateDiceMarker(false);
        assertArrayEquals(new boolean[]{false, false}, resultNonDoubles, "Expected 2 'false' values for non-doubles.");
    }

    @Test
    public void testGenerateDiceRoll(){
        // Test function when the dice values are not set
        Game.isDiceSet = false;
        int[] roll1 = game.generateDiceRoll();
        assertArrayEquals(roll1, Game.dice.getSetDice());

        //Test function when dice values are set to 1, 4
        int [] setValues = {1,4};
        Game.dice.setDice(1,4);
        Game.isDiceSet = true;
        int[] roll2 = game.generateDiceRoll();
        assertArrayEquals(roll2, setValues);
        //int[] roll = Game.dice.getSetDice();
    }

    @Test
    public void testSetDice_ValidInput() {
        // Arrange: Simulate valid user input and capture output
        String simulatedInput = "4 5\n"; // Simulates entering "4 5" as dice values
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act: Call setDice
        game.setDice();

        // Assert: Verify the dice values are set correctly and the output is as expected
        assertTrue(Game.isDiceSet, "Dice should be marked as set.");
        String output = outContent.toString();
        assertTrue(output.contains("Dice values now set for the next dice roll."), "Expected success message in output.");
    }

    @Test
    public void testSetDice_InvalidInputThenValidInput() {
        // Arrange: Simulate invalid input followed by valid input
        String simulatedInput = "abc 5\n4 5\n"; // First input invalid, second input valid
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act: Call setDice
        game.setDice();

        // Assert: Verify the dice values are set correctly after valid input
        assertTrue(Game.isDiceSet, "Dice should be marked as set after valid input.");
        String output = outContent.toString();
        assertTrue(output.contains("Invalid input. Enter two valid integer values!"), "Expected error message for invalid input.");
        assertTrue(output.contains("Dice values now set for the next dice roll."), "Expected success message in output.");
    }

    @Test
    public void testGetUserMove(){
        List<String> legalMoves = new ArrayList<>();
        legalMoves.add("A)Move 1-3");
        legalMoves.add("B)Move 2-4");
        // Simulate user selecting the second move (input 'B')
        String simulatedInput = "B\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: Call getUserMove with the legalMoves
        String[] result = game.getUserMove(legalMoves);

        // Assert: Verify the parsed move matches the expected result
        String[] expectedMove = {"2", "4"};
        assertArrayEquals(expectedMove, result, "Expected parsed move to match the user's selection.");
    }

    @Test
    public void testGetUserMoveWithInvalidInput() {
        // Arrange: Create a list of legal moves and simulate invalid followed by valid input
        List<String> legalMoves = new ArrayList<>();
        legalMoves.add("Move 1-3");
        legalMoves.add("Move 2-4");

        // Simulate user first entering invalid input ('Z') and then valid input ('A')
        String simulatedInput = "Z\nA\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act: Call getUserMove with the legalMoves
        String[] result = game.getUserMove(legalMoves);

        // Assert: Verify the parsed move matches the valid input after the invalid input
        String[] expectedMove = {"1", "3"};
        assertArrayEquals(expectedMove, result, "Expected parsed move to match the user's valid selection after invalid input.");
    }

    @Test
    public void testPipCount() {
        game.pipCount();
        // This method prints pip counts. We check no exceptions are thrown.
        assertTrue(true, "Pip count should execute successfully.");
    }

    @Test
    public void testGenerateLegalMoves() {

        int[] diceRoll = {3, 4};
        Player player1 = new Player("Alice", Checker.Colour.WHITE);
        boolean[] usedDice = {false, false};
        List<String> moves = game.generateLegalMoves(player1, diceRoll, usedDice, false);
        assertNotNull(moves, "Legal moves list should not be null.");
    }

    @Test
    void testMarkUsedDice() {
        // Test scenario 1: Standard dice roll with move matching first die
        int[] diceRoll1 = {3, 4};
        boolean[] usedDice1 = {false, false};
        game.markUsedDice(diceRoll1, 3, usedDice1);
        assertTrue(usedDice1[0], "First die should be marked as used.");
        assertFalse(usedDice1[1], "Second die should not be marked as used.");

        // Test scenario 2: Standard dice roll with move matching second die
        int[] diceRoll2 = {3, 4};
        boolean[] usedDice2 = {false, false};
        game.markUsedDice(diceRoll2, 4, usedDice2);
        assertTrue(usedDice2[1], "Second die should be marked as used.");
        assertFalse(usedDice2[0], "First die should not be marked as used.");

        // Test scenario 3: Doubles roll with move matching one of the dice
        int[] doubleDiceRoll = {6, 6, 6, 6};
        boolean[] usedDice3 = {false, false, false, false};
        game.markUsedDice(doubleDiceRoll, 6, usedDice3);
        assertTrue(usedDice3[0] || usedDice3[1] || usedDice3[2] || usedDice3[3],
        "At least one die should be marked as used in a doubles roll.");

        // Test scenario 4: Attempting to use a dice value that doesn't match any die
        int[] diceRoll4 = {2, 5};
        boolean[] usedDice4 = {false, false};
        game.markUsedDice(diceRoll4, 3, usedDice4);
        assertFalse(usedDice4[0], "No die should be marked as used when move doesn't match.");
        assertFalse(usedDice4[1], "No die should be marked as used when move doesn't match.");

        // Test scenario 5: Repeated use of the same die (should not mark again)
        int[] diceRoll5 = {3, 4};
        boolean[] usedDice5 = {true, false};
        game.markUsedDice(diceRoll5, 3, usedDice5);
        assertTrue(usedDice5[0], "First die should remain marked as used.");
        assertFalse(usedDice5[1], "Second die should not be marked if not used.");

        // Test scenario 6: Doubles roll with all dice already used
        boolean[] usedDice6 = {true, true, true, true};
        game.markUsedDice(doubleDiceRoll, 6, usedDice6);
        assertTrue(usedDice6[0] && usedDice6[1] && usedDice6[2] && usedDice6[3],
        "No changes should occur when all dice are already marked as used.");
    }


    @Test
    public void testGetRemainingMoves() {
        boolean[] usedDice = {true, false};
        int remaining = game.getRemainingMoves(usedDice);
        assertEquals(1, remaining, "One move should remain.");
    }

    @Test
    void testSingleWinCondition() {
        Player winningPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player losingPlayer = new Player("Bob", Checker.Colour.BLACK);

        // Simulate losing player has borne off some checkers
        stubBoard.getStubHomePile(Board.PLAYER2HOMEINDEX).addChecker(new Checker(-1, Checker.Colour.BLACK));

        // Call getWinCondition
        int winCondition = game.getWinCondition(winningPlayer, losingPlayer);

        // Assert single win
        assertEquals(1, winCondition, "Expected Single Win condition.");
    }

    @Test
    void testGammonWinCondition() {
        Player winningPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player losingPlayer = new Player("Bob", Checker.Colour.BLACK);

        // Ensure losing player has no checkers on the bar or in the opponent's home board
        stubBoard.getStubBarPile(Board.PLAYER2BARINDEX).clear();
        for (int i = 18; i <= 23; i++) {
            stubBoard.getStubPile(i).clear(); // Clear losing player's checkers from the opponent's home board
        }

        // Call getWinCondition
        int winCondition = game.getWinCondition(winningPlayer, losingPlayer);

        // Assert Gammon win
        assertEquals(2, winCondition, "Expected Gammon Win condition.");
    }

    @Test
    void testBackgammonWinCondition() {
        Player winningPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player losingPlayer = new Player("Bob", Checker.Colour.BLACK);

        // Simulate losing player has checkers on the bar and in the opponent's home board
        stubBoard.getStubBarPile(Board.PLAYER2BARINDEX).addChecker(new Checker(-1, Checker.Colour.BLACK));
        stubBoard.getStubPile(0).addChecker(new Checker(0, Checker.Colour.BLACK)); // Opponent's home board

        // Call getWinCondition
        int winCondition = game.getWinCondition(winningPlayer, losingPlayer);

        // Assert Backgammon win
        assertEquals(3, winCondition, "Expected Backgammon Win condition.");
    }

    @Test
    public void testGetMoveDistanceFromBar(){
        Player player2 = new Player("Bob", Checker.Colour.BLACK);
        String[] moveParts = {"bar", "3"};
        assertEquals(game.getMoveDistance(moveParts, player2), 3);
    }
    @Test
    public void testGetMoveDistanceToOffBoard(){
        Player player2 = new Player("Bob", Checker.Colour.BLACK);
        String[] moveParts = {"24", "off"};
        assertEquals(game.getMoveDistance(moveParts, player2), 1);
    }
    @Test
    public void testGetMoveDistanceStandard() {
        String[] moveParts = {"8", "13"};
        Player player2 = new Player("Bob", Checker.Colour.BLACK);
        assertEquals(game.getMoveDistance(moveParts, player2), 5);
    }

    @Test
    void testDisplayPipNumbers() {
        // Set up the board state
        Player whitePlayer = new Player("Alice", Checker.Colour.WHITE);
        stubBoard.getStubPile(0).addChecker(new Checker(0, Checker.Colour.WHITE));
        stubBoard.getStubPile(0).addChecker(new Checker(0, Checker.Colour.WHITE));
        stubBoard.getStubPile(23).addChecker(new Checker(23, Checker.Colour.WHITE));

        Player blackPlayer = new Player("Bob", Checker.Colour.BLACK);
        stubBoard.getStubPile(5).addChecker(new Checker(5, Checker.Colour.BLACK));
        stubBoard.getStubPile(18).addChecker(new Checker(18, Checker.Colour.BLACK));
        stubBoard.getStubPile(18).addChecker(new Checker(18, Checker.Colour.BLACK));

        // Call the method for whitePlayer
        game.displayPipNumbers(whitePlayer, true);

        // Capture the output
        String output = outputStream.toString().trim();

        // Validate the output
        assertTrue(output.contains("Pip scores for Alice:"));
        assertTrue(output.contains("Cone 1 -> 2")); // 2 checkers on position 0 (pip score: 2 * 1)
        assertTrue(output.contains("Cone 24 -> 24")); // 1 checker on position 23 (pip score: 1 * 24)
    }

    @Test
    void testHandleAcceptedDouble() {
        // Arrange
        DoubleDice doubleDice = new DoubleDice();
        Player currentPlayer = new Player("Alice", Checker.Colour.WHITE);
        Player otherPlayer = new Player("Bob", Checker.Colour.BLACK);
        doubleDice.setOwner(1); // Current player owns the DoubleDice
        doubleDice.setDouble(2); // Initial stake multiplier

        // Act
        game.handleAcceptedDouble(currentPlayer, otherPlayer, 2);

        // Capture the output
        String output = outputStream.toString().trim();

        // Assert
        assertEquals(4, DoubleDice.getDouble(), "DoubleDice multiplier should be doubled.");
        assertEquals(4, game.stake, "Stake should match the doubled multiplier.");
        assertEquals(2, DoubleDice.getOwner(), "DoubleDice ownership should transfer to the other player.");
        assertTrue(output.contains("The stakes have been doubled"), "Output should indicate that the stakes were doubled.");
    }

}



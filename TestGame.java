import java.util.ArrayList;
import java.util.List;

class TestGame extends Game {
    boolean gameHelpCalled = false; // Flag to track if gameHelp() was called
    boolean handleDoubleRequestCalled = false; // Flag to track if handleDoubleRequest() was called
    boolean handleRollCalled = false; // Flag to track if handleRoll() was called
    boolean pipCountCalled = false; // Flag to track if pipCount() was called
    boolean setDiceCalled = false; // Flag to track if setDice() was called

    private final List<String> methodCallLog = new ArrayList<>(); // Log of method calls

    // Simulates the behavior of handleDoubleRequest and logs the call
    @Override
    protected boolean handleDoubleRequest(Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        handleDoubleRequestCalled = true; // Set flag to true
        methodCallLog.add("handleDoubleRequest"); // Log method call
        return true; // Simulated behavior
    }

    // Simulates the behavior of handleRoll and logs the call
    @Override
    protected boolean handleRoll(Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        handleRollCalled = true; // Set flag to true
        methodCallLog.add("handleRoll"); // Log method call
        return true; // Simulated behavior
    }

    // Simulates the behavior of pipCount and logs the call
    @Override
    protected void pipCount() {
        pipCountCalled = true; // Set flag to true
        methodCallLog.add("pipCount"); // Log method call
    }

    // Simulates the behavior of setDice and logs the call
    @Override
    protected void setDice() {
        setDiceCalled = true; // Set flag to true
        methodCallLog.add("setDice"); // Log method call
    }

}


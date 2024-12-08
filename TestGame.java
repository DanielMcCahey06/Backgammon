import java.util.ArrayList;
import java.util.List;

class TestGame extends Game {
    boolean gameHelpCalled = false;
    boolean handleDoubleRequestCalled = false;
    boolean handleRollCalled = false;
    boolean pipCountCalled = false;
    boolean setDiceCalled = false;

    private final List<String> methodCallLog = new ArrayList<>();

    @Override
    protected boolean handleDoubleRequest(Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        handleDoubleRequestCalled = true;
        methodCallLog.add("handleDoubleRequest");
        return true; // Simulated behavior
    }

    @Override
    protected boolean handleRoll(Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        handleRollCalled = true;
        methodCallLog.add("handleRoll");
        return true; // Simulated behavior
    }

    @Override
    protected void pipCount() {
        pipCountCalled = true;
        methodCallLog.add("pipCount");
    }

    @Override
    protected void setDice() {
        setDiceCalled = true;
        methodCallLog.add("setDice");
    }

}




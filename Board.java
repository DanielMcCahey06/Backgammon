import java.util.*;

// Represents the game board for Backgammon and manages piles, home, and bar positions.
public class Board implements Subject {
    public static final int TOTAL_CHECKERS = 15; // Total checkers per player
    private Pile[] piles = new Pile[24]; // Regular board piles
    private Pile[] home = new Pile[2]; // Home piles for each player
    private Pile[] bar = new Pile[2]; // Bar piles for captured checkers
    private final List<Observer> observers = new ArrayList<>(); // Observers for the board

    // Constants for bar and home pile indices
    public static int PLAYER1BARINDEX = 1; // White checkers' bar
    public static int PLAYER2BARINDEX = 0; // Black checkers' bar
    public static int PLAYER1HOMEINDEX = 0; // White checkers' home
    public static int PLAYER2HOMEINDEX = 1; // Black checkers' home

    // Constructor initializes piles, home, and bar, and sets up starting checkers
    public Board() {
        for (int i = 0; i < 24; i++) {
            piles[i] = new Pile();
        }
        for (int i = 0; i < 2; i++) {
            home[i] = new Pile();
            bar[i] = new Pile();
        }
        initialiseCheckers(); // Sets up initial positions of checkers
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer); // Adds an observer to the list
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this); // Notifies all observers of board changes
        }
    }

    public Pile[] getPiles() {
        return piles; // Returns all board piles
    }

    public Pile getPile(int index) {
        // Validates index and returns the specified pile
        if (index < 0 || index >= piles.length) {
            throw new IllegalArgumentException("Invalid pile index");
        }
        return piles[index];
    }

    public Pile getHomePile(int index) {
        // Validates index and returns the specified home pile
        if (index < 0 || index >= home.length) {
            throw new IllegalArgumentException("Invalid home pile index");
        }
        return home[index];
    }

    public Pile getBarPile(int index) {
        // Validates index and returns the specified bar pile
        if (index < 0 || index >= bar.length) {
            throw new IllegalArgumentException("Invalid bar pile index");
        }
        return bar[index];
    }

    public boolean checkWinCondition(Player currentPlayer) {
        // Checks if all checkers of a player are in their home pile
        int playerHomeIndex = (currentPlayer.getChecker() == Checker.Colour.WHITE) ? PLAYER1HOMEINDEX : PLAYER2HOMEINDEX;
        return home[playerHomeIndex].getNoOfCheckers() == TOTAL_CHECKERS;
    }

    public boolean isLegalMove(int targetPosition, Checker.Colour colour) {
        // Determines if a move to the target position is legal
        boolean isLegal = false;
        if (piles[targetPosition].getCheckers().isEmpty()) {
            // Move is legal if the pile is empty
            isLegal = true;
        } else {
            // Move is legal if the top checker in the pile matches the moving checker's colour
            if (piles[targetPosition].getCheckers().getLast().getColour() == colour) {
                isLegal = true;
            } else {
                // Move is legal if the pile contains only one checker
                if (piles[targetPosition].getNoOfCheckers() < 2) {
                    isLegal = true;
                }
            }
        }
        return isLegal;
    }

    public int[] getMaxHeight() {
        // Finds the maximum number of checkers in any pile, bar, and home
        int pilesMaxHeight = 0;
        int barMaxHeight = 0;
        int homeMaxHeight = 0;
        for (Pile pile : piles) {
            if (pile.getNoOfCheckers() > pilesMaxHeight) {
                pilesMaxHeight = pile.getNoOfCheckers();
            }
        }
        for (Pile pile : bar) {
            if (pile.getNoOfCheckers() > barMaxHeight) {
                barMaxHeight = pile.getNoOfCheckers();
            }
        }
        for (Pile pile : home) {
            if (pile.getNoOfCheckers() > homeMaxHeight) {
                homeMaxHeight = pile.getNoOfCheckers();
            }
        }
        return new int[]{pilesMaxHeight, barMaxHeight, homeMaxHeight};
    }

    private void initialiseCheckers() {
        // Sets up the initial positions of checkers for both players
        piles[23].addChecker(new Checker(23, Checker.Colour.WHITE));
        piles[23].addChecker(new Checker(23, Checker.Colour.WHITE));
        piles[18].addChecker(new Checker(18, Checker.Colour.BLACK));
        piles[18].addChecker(new Checker(18, Checker.Colour.BLACK));
        piles[18].addChecker(new Checker(18, Checker.Colour.BLACK));
        piles[18].addChecker(new Checker(18, Checker.Colour.BLACK));
        piles[18].addChecker(new Checker(18, Checker.Colour.BLACK));
        piles[16].addChecker(new Checker(16, Checker.Colour.BLACK));
        piles[16].addChecker(new Checker(16, Checker.Colour.BLACK));
        piles[16].addChecker(new Checker(16, Checker.Colour.BLACK));
        piles[12].addChecker(new Checker(12, Checker.Colour.WHITE));
        piles[12].addChecker(new Checker(12, Checker.Colour.WHITE));
        piles[12].addChecker(new Checker(12, Checker.Colour.WHITE));
        piles[12].addChecker(new Checker(12, Checker.Colour.WHITE));
        piles[12].addChecker(new Checker(12, Checker.Colour.WHITE));
        piles[11].addChecker(new Checker(11, Checker.Colour.BLACK));
        piles[11].addChecker(new Checker(11, Checker.Colour.BLACK));
        piles[11].addChecker(new Checker(11, Checker.Colour.BLACK));
        piles[11].addChecker(new Checker(11, Checker.Colour.BLACK));
        piles[11].addChecker(new Checker(11, Checker.Colour.BLACK));
        piles[7].addChecker(new Checker(7, Checker.Colour.WHITE));
        piles[7].addChecker(new Checker(7, Checker.Colour.WHITE));
        piles[7].addChecker(new Checker(7, Checker.Colour.WHITE));
        piles[5].addChecker(new Checker(5, Checker.Colour.WHITE));
        piles[5].addChecker(new Checker(5, Checker.Colour.WHITE));
        piles[5].addChecker(new Checker(5, Checker.Colour.WHITE));
        piles[5].addChecker(new Checker(5, Checker.Colour.WHITE));
        piles[5].addChecker(new Checker(5, Checker.Colour.WHITE));
        piles[0].addChecker(new Checker(0, Checker.Colour.BLACK));
        piles[0].addChecker(new Checker(0, Checker.Colour.BLACK));
    }
}
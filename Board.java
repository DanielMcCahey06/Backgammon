import java.util.*;

public class Board implements Subject{
    public static final int TOTAL_CHECKERS = 15;
    private Pile[] piles = new Pile[24];
    private Pile[] home = new Pile[2];
    private Pile[] bar = new Pile[2];
    private final List<Observer> observers = new ArrayList<>();

    public static int PLAYER1BARINDEX = 1; // White Checkers
    public static int PLAYER2BARINDEX = 0; // Black Checkers

    public static int PLAYER1HOMEINDEX = 0; // White Checkers (0)
    public static int PLAYER2HOMEINDEX = 1; // Black Checkers (1)

    public Board() {
        for (int i = 0; i < 24; i++) {
            piles[i] = new Pile();
        }
        for (int i = 0; i < 2; i++) {
            home[i] = new Pile();
            bar[i] = new Pile();
        }

        initialiseCheckers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public Pile[] getPiles(){
        return piles;
    }

    public Pile getPile(int index) {
        if (index < 0 || index >= piles.length) {
            throw new IllegalArgumentException("Invalid pile index");
        }
        return piles[index];
    }

    public Pile getHomePile(int index) {
        if (index < 0 || index >= home.length) {
            throw new IllegalArgumentException("Invalid home pile index");
        }
        return home[index];
    }

    public Pile getBarPile(int index) {
        if (index < 0 || index >= bar.length) {
            throw new IllegalArgumentException("Invalid bar pile index");
        }
        return bar[index];
    }

    public boolean checkWinCondition(Player currentPlayer){
        int playerHomeIndex = (currentPlayer.getChecker() == Checker.Colour.WHITE) ? PLAYER1HOMEINDEX : PLAYER2HOMEINDEX;
        return home[playerHomeIndex].getNoOfCheckers() == TOTAL_CHECKERS;
    }

    public boolean isLegalMove(int targetPosition, Checker.Colour colour) {
        boolean isLegal = false;

        // If pile is empty then the move is legal
        if (piles[targetPosition].getCheckers().isEmpty()) {
            isLegal = true;
        }
        else {
            // If the checkers in the pile are the same colour as the checker to move then it is legal
            if(piles[targetPosition].getCheckers().getLast().getColour() == colour){
                isLegal = true;
            }
            else // Checkers aren't the same colour
            {
                // If there is only one checker then the move is legal
                if(piles[targetPosition].getNoOfCheckers() < 2)
                {
                    isLegal = true;
                }
            }
        }
        return isLegal;
    }


    public int[] getMaxHeight() {
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
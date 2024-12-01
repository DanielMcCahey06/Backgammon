import java.util.*;

public class Board {
    public static final int TOTAL_CHECKERS = 15;
    public static Pile[] piles = new Pile[24];
    public static Pile[] home = new Pile[2];
    public static Pile[] bar = new Pile[2];

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
    }

    public static List<Checker> getCheckers(Checker.Colour colour) {
        List<Checker> checkers = new ArrayList<>();

        // Iterate through all piles on the board and get checkers of the specified color
        for (Pile pile : piles) {
            for (Checker checker : pile.getCheckers()) {
                if (checker.getColour() == colour) {
                    checkers.add(checker);
                }
            }
        }
        return checkers;
    }

    public void addChecker(int pile, Checker checker) {
        piles[pile].addChecker(checker);
    }

    public void removeChecker(int pile) {
        piles[pile].removeTopChecker();
    }

    public void addBarChecker(int index, Checker checker) {
        bar[index].addChecker(checker);
    }

    public void removeBarChecker(int pile) {
        bar[pile].removeTopChecker();
    }

    public void addHomeChecker(int pile, Checker checker) {
        home[pile].addChecker(checker);
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


    private static int[] getMaxHeight() {
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

    public static void display(boolean currentPlayer) {

        int[] maxHeight = getMaxHeight();
        int pileMaxHeight = maxHeight[0];
        int barMaxHeight = maxHeight[1];
        int homeMaxHeight = maxHeight[2];
        int actualMaxHeight;
        String player1TopPips = "13 14 15 16 17 18 | B | 19 20 21 22 23 24";
        String player1BottomPips = "12 11 10  9  8  7 | B |  6  5  4  3  2  1";

        if (DoubleDice.getOwner() == 2) {
            System.out.println(player1TopPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]   Double Dice Value x" + DoubleDice.getDouble());
        } else {
            System.out.println(player1TopPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
        }

        if (Math.max(Math.max(barMaxHeight, pileMaxHeight), homeMaxHeight) == homeMaxHeight) {
            actualMaxHeight = Math.min(homeMaxHeight, 6);
        } else {
            actualMaxHeight = Math.max(pileMaxHeight, barMaxHeight);
        }
        for (int row = 0; row < Math.max(actualMaxHeight, 6); row++) {
            for (int i = 12; i < 18; i++) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            if (row < bar[0].getNoOfCheckers()) {
                System.out.print("| " + bar[0].get(row).toString() + "| ");
            } else {
                System.out.print("|   | ");
            }

            for (int i = 18; i < 24; i++) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            if (row == 5) {
                System.out.print("  [⎼⎼⎼⎼⎼⎼⎼⎼⎼]");
            } else if (row < home[1].getNoOfCheckers()) {
                System.out.print("  | " + home[1].get(row).toString() + "| ");
            } else if (row > 5) {
                System.out.print(" ");
            } else {
                System.out.print("  |   |");
            }
            System.out.println();
        }

        if (DoubleDice.getOwner() == 0) {
            System.out.println("================= | B | ==================             Double Dice Value x" + DoubleDice.getDouble());
        } else {
            System.out.println("================= | B | ==================");
        }

        for (int row = Math.max(actualMaxHeight, 6) - 1; row >= 0; row--) {
            for (int i = 11; i > 5; i--) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            if (row < bar[1].getNoOfCheckers()) {
                System.out.print("| " + bar[1].get(row).toString() + "| ");
            } else {
                System.out.print("|   | ");
            }

            for (int i = 5; i >= 0; i--) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }

            if (row == 5) {
                System.out.print("  [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
            } else if (row < home[0].getNoOfCheckers()) {
                System.out.print("  | " + home[0].get(row).toString() + "| ");
            } else if (row > 5) {
                System.out.print(" ");
            }  else {
                System.out.print("  |   |");
            }
            System.out.println();
        }

        if (DoubleDice.getOwner() == 1) {
            System.out.println(player1BottomPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]   Double Dice Value x" + DoubleDice.getDouble());
        } else {
            System.out.println(player1BottomPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
        }
    }
}
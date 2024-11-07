public class Board {
    public static Pile[] piles = new Pile[24];
    public static Pile[] home = new Pile[2];
    public static Pile[] bar = new Pile[2];

    public Board() {
        for (int i = 0; i < 24; i++) {
            piles[i] = new Pile();
        }
        for (int i = 0; i < 2; i++) {
            home[i] = new Pile();
            bar[i] = new Pile();
        }
    }

    public void addChecker(int pile, Checker checker) {
        piles[pile].addChecker(checker);
    }

    public void removeChecker(int pile, Checker checker) {
        piles[pile].removeChecker(checker);
    }

    public void addBarChecker(int pile, Checker checker) {
        bar[pile].addChecker(checker);
    }

    public void removeBarChecker(int pile, Checker checker) {
        bar[pile].removeChecker(checker);
    }

    public void addHomeChecker(int pile, Checker checker) {
        home[pile].addChecker(checker);
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

        if (currentPlayer) {
            System.out.println(player1TopPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
        } else {
            System.out.println(player1BottomPips+ "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
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
            } else if (row < home[0].getNoOfCheckers()) {
                System.out.print("  | " + home[0].get(row).toString() + "| ");
            } else if (row > 5) {
                System.out.print(" ");
            } else {
                System.out.print("  |   |");
            }
            System.out.println();
        }

        System.out.println("================= | B | ==================");

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
            } else if (row < home[1].getNoOfCheckers()) {
                System.out.print("  | " + home[1].get(row).toString() + "| ");
            } else if (row > 5) {
                System.out.print(" ");
            }  else {
                System.out.print("  |   |");
            }
            System.out.println();
        }
        if (currentPlayer) {
            System.out.println(player1BottomPips + "    [⎼⎼⎼⎼⎼⎼⎼⎼⎼]");
        } else {
            System.out.println(player1TopPips + "    [⎼⎼⎼⎼⎼⎼⎼⎼⎼]");
        }
    }
}
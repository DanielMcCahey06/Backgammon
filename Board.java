public class Board {
    public static Pile[] piles = new Pile[24];

    public Board() {
        for (int i = 0; i < 24; i++) {
            piles[i] = new Pile();
        }
    }

    public void addChecker(int pile, Checker checker) {
        piles[pile].addChecker(checker);
    }

    public void removeChecker(int pile, Checker checker) {
        piles[pile].removeChecker(checker);
    }

    private static int getMaxHeight() {
        int maxHeight = 0;
        for (int i = 0; i < piles.length; i++) {
            if (piles[i].getNoOfCheckers() > maxHeight) {
                maxHeight = piles[i].getNoOfCheckers();
            }
        }
        return maxHeight;
    }

    public static void display(boolean currentPlayer) {

        int maxHeight = getMaxHeight();
        String player1TopPips = "13 14 15 16 17 18 | | 19 20 21 22 23 24";
        String player1BottomPips = "12 11 10  9  8  7 | |  6  5  4  3  2  1";

        if (currentPlayer) {
            System.out.println(player1TopPips);
        } else {
            System.out.println(player1BottomPips);
        }
        for (int row = 0; row < maxHeight; row++) {
            for (int i = 12; i < 18; i++) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.print("| | ");

            for (int i = 18; i < 24; i++) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println(Game.Reset);
        }

        System.out.println("================= | | ==================");

        for (int row = maxHeight - 1; row >= 0; row--) {
            for (int i = 11; i > 5; i--) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.print("| | ");

            for (int i = 5; i >= 0; i--) {
                if (row < piles[i].getNoOfCheckers()) {
                    System.out.print(piles[i].get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
        if (currentPlayer) {
            System.out.println(player1BottomPips);
        } else {
            System.out.println(player1TopPips);
        }
    }
}
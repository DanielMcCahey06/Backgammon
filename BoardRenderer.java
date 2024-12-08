public class BoardRenderer implements Observer{

    private final Board board;

    public BoardRenderer (Board board) {
        this.board = board;
    }

    // Called automatically when the Board state changes
    @Override
    public void update(Board board) {
        display(); // Re-render the board
    }

    public void display() {
        int[] maxHeight = board.getMaxHeight();
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
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            if (row < board.getBarPile(0).getNoOfCheckers()) {
                System.out.print("| " + board.getBarPile(0).get(row).toString() + "| ");
            } else {
                System.out.print("|   | ");
            }

            for (int i = 18; i < 24; i++) {
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            if (row == 5) {
                System.out.print("  [⎼⎼⎼⎼⎼⎼⎼⎼⎼]");
            } else if (row < board.getHomePile(1).getNoOfCheckers()) {
                System.out.print("  | " + board.getHomePile(1).get(row).toString() + "| ");
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
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }
            if (row < board.getBarPile(1).getNoOfCheckers()) {
                System.out.print("| " + board.getBarPile(1).get(row).toString() + "| ");
            } else {
                System.out.print("|   | ");
            }

            for (int i = 5; i >= 0; i--) {
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }

            if (row == 5) {
                System.out.print("  [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
            } else if (row < board.getHomePile(0).getNoOfCheckers()) {
                System.out.print("  | " + board.getHomePile(0).get(row).toString() + "| ");
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
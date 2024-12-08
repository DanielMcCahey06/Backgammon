public class BoardRenderer implements Observer {

    private final Board board; // Reference to the game board being rendered

    // Constructor initializes the BoardRenderer with the game board
    public BoardRenderer(Board board) {
        this.board = board;
    }

    // Called automatically whenever the Board state changes
    @Override
    public void update(Board board) {
        display(); // Re-render the board when an update occurs
    }

    // Displays the current state of the game board
    public void display() {
        // Get the maximum number of checkers in piles, bar, and home
        int[] maxHeight = board.getMaxHeight();
        int pileMaxHeight = maxHeight[0];
        int barMaxHeight = maxHeight[1];
        int homeMaxHeight = maxHeight[2];
        int actualMaxHeight;

        // Strings for Player 1's pip labels on the board
        String player1TopPips = "13 14 15 16 17 18 | B | 19 20 21 22 23 24";
        String player1BottomPips = "12 11 10  9  8  7 | B |  6  5  4  3  2  1";

        // Display the top pip labels with double dice info if Player 2 owns it
        if (DoubleDice.getOwner() == 2) {
            System.out.println(player1TopPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]   Double Dice Value x" + DoubleDice.getDouble());
        } else {
            System.out.println(player1TopPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
        }

        // Determine the actual height to display based on the max height
        if (Math.max(Math.max(barMaxHeight, pileMaxHeight), homeMaxHeight) == homeMaxHeight) {
            actualMaxHeight = Math.min(homeMaxHeight, 6); // Cap at 6 for home piles
        } else {
            actualMaxHeight = Math.max(pileMaxHeight, barMaxHeight);
        }

        // Render the upper portion of the board (Player 1's top side)
        for (int row = 0; row < Math.max(actualMaxHeight, 6); row++) {
            // Render piles 12–17 (right-to-left for Player 1's top side)
            for (int i = 12; i < 18; i++) {
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }

            // Render Player 2's bar pile
            if (row < board.getBarPile(0).getNoOfCheckers()) {
                System.out.print("| " + board.getBarPile(0).get(row).toString() + "| ");
            } else {
                System.out.print("|   | ");
            }

            // Render piles 18–23 (left-to-right for Player 1's top side)
            for (int i = 18; i < 24; i++) {
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }

            // Render Player 2's home pile
            if (row == 5) {
                System.out.print("  [⎼⎼⎼⎼⎼⎼⎼⎼⎼]");
            } else if (row < board.getHomePile(1).getNoOfCheckers()) {
                System.out.print("  | " + board.getHomePile(1).get(row).toString() + "| ");
            } else if (row > 5) {
                System.out.print(" ");
            } else {
                System.out.print("  |   |");
            }
            System.out.println(); // Move to the next row
        }

        // Separator row between Player 1's top and bottom sides
        if (DoubleDice.getOwner() == 0) {
            System.out.println("================= | B | ==================             Double Dice Value x" + DoubleDice.getDouble());
        } else {
            System.out.println("================= | B | ==================");
        }

        // Render the lower portion of the board (Player 1's bottom side)
        for (int row = Math.max(actualMaxHeight, 6) - 1; row >= 0; row--) {
            // Render piles 11–6 (left-to-right for Player 1's bottom side)
            for (int i = 11; i > 5; i--) {
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }

            // Render Player 1's bar pile
            if (row < board.getBarPile(1).getNoOfCheckers()) {
                System.out.print("| " + board.getBarPile(1).get(row).toString() + "| ");
            } else {
                System.out.print("|   | ");
            }

            // Render piles 5–0 (right-to-left for Player 1's bottom side)
            for (int i = 5; i >= 0; i--) {
                if (row < board.getPile(i).getNoOfCheckers()) {
                    System.out.print(board.getPile(i).get(row).toString() + " ");
                } else {
                    System.out.print("   ");
                }
            }

            // Render Player 1's home pile
            if (row == 5) {
                System.out.print("  [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
            } else if (row < board.getHomePile(0).getNoOfCheckers()) {
                System.out.print("  | " + board.getHomePile(0).get(row).toString() + "| ");
            } else if (row > 5) {
                System.out.print(" ");
            } else {
                System.out.print("  |   |");
            }
            System.out.println(); // Move to the next row
        }

        // Display the bottom pip labels with double dice info if Player 1 owns it
        if (DoubleDice.getOwner() == 1) {
            System.out.println(player1BottomPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]   Double Dice Value x" + DoubleDice.getDouble());
        } else {
            System.out.println(player1BottomPips + "    [⎺⎺⎺⎺⎺⎺⎺⎺⎺]");
        }
    }
}
public class Board {
    private static Pile[] piles = new Pile[24];

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

    public static void display() {
        System.out.println("13 14 15 16 17 18 | | 19 20 21 22 23 24");
        for (int i = 12; i < 18; i++) {
            System.out.print(piles[i].toString());
        }
        System.out.print("| | ");
        for (int i = 18; i < 24; i++) {
            System.out.print(piles[i].toString());
        }
        System.out.println(Checker.Reset);
        System.out.println("================= | | ==================");

        for (int i = 11; i > 5; i--) {
            System.out.print(piles[i].toString());
        }
        System.out.print("| | ");
        for (int i = 5; i >= 0; i--) {
            System.out.print(piles[i].toString());
        }
        System.out.println();
        System.out.println("12 11 10  9  8  7 | |  6  5  4  3  2  1");
    }
}
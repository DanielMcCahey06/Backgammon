import java.util.Scanner;

class TestMatch extends Match {
    public TestMatch(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void playMatch() {
        System.out.println("Simulated playMatch called.");
    }
}


import java.util.*;

public class Pile {
    private final List<Checker> checkers = new ArrayList<>(); // List to store checkers in the pile

    // Returns the list of checkers in the pile
    public List<Checker> getCheckers() {
        return checkers;
    }

    // Adds a checker to the pile
    public void addChecker(Checker checker) {
        checkers.add(checker);
    }

    // Removes and returns the top checker from the pile
    public Checker removeTopChecker() {
        return checkers.remove(checkers.size() - 1);
    }

    // Returns the number of checkers in the pile
    public int getNoOfCheckers() {
        return checkers.size();
    }

    // Returns the color of the checker on the top of the pile, or null if the pile is empty
    public Checker.Colour getColour() {
        if (!checkers.isEmpty()) {
            return checkers.get(0).getColour(); // Get color of the top checker
        } else {
            return null; // Return null if no checkers are in the pile
        }
    }

    // Returns the checker at the specified index in the pile
    public Checker get(int index) {
        return checkers.get(index);
    }
}

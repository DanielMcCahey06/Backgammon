import java.util.ArrayList;
import java.util.List;

class StubPile extends Pile {
    private final List<Checker> checkers = new ArrayList<>(); // List to store checkers in the pile

    // Returns the number of checkers in the pile
    @Override
    public int getNoOfCheckers() {
        return checkers.size(); // Return the size of the checkers list
    }

    // Returns the checker at the specified index, or null if index is out of bounds
    @Override
    public Checker get(int index) {
        return index < checkers.size() ? checkers.get(index) : null; // Return checker at index or null
    }

    // Adds a checker to the pile
    public void addChecker(Checker checker) {
        checkers.add(checker); // Add the checker to the list
    }

    // Removes and returns the top checker from the pile
    @Override
    public Checker removeTopChecker() {
        if (checkers.isEmpty()) {
            throw new IllegalStateException("No checkers to remove"); // Throw error if pile is empty
        }
        return checkers.remove(checkers.size() - 1); // Remove and return the top checker
    }

    // Clears all checkers from the pile
    public void clear() {
        checkers.clear(); // Remove all checkers from the list
    }

    // Returns the colour of the top checker, or null if the pile is empty
    @Override
    public Checker.Colour getColour() {
        if (!checkers.isEmpty()) {
            return checkers.get(0).getColour(); // Return the colour of the top checker
        }
        return null; // Return null if pile is empty
    }
}

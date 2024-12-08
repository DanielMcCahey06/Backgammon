import java.util.ArrayList;
import java.util.List;

class StubPile extends Pile {
    private final List<Checker> checkers = new ArrayList<>();

    @Override
    public int getNoOfCheckers() {
        return checkers.size();
    }

    @Override
    public Checker get(int index) {
        return index < checkers.size() ? checkers.get(index) : null;
    }

    public void addChecker(Checker checker) {
        checkers.add(checker);
    }

    @Override
    public Checker removeTopChecker() {
        if (checkers.isEmpty()) {
            throw new IllegalStateException("No checkers to remove");
        }
        return checkers.remove(checkers.size() - 1);
    }

    public void clear() {
        checkers.clear();
    }

    @Override
    public Checker.Colour getColour() {
        if (!checkers.isEmpty()) {
            return checkers.get(0).getColour(); // Return the colour of the top checker.
        }
        return null;
    }
}


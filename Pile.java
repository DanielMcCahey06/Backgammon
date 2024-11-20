import java.util.*;

public class Pile {
    private final List<Checker> checkers = new ArrayList<>();

    public List<Checker> getCheckers() {
        return checkers;
    }

    public void addChecker(Checker checker) {
        checkers.add(checker);
    }

    public Checker removeTopChecker() {
        return checkers.remove(checkers.size() - 1);
    }

    public int getNoOfCheckers() {
        return checkers.size();
    }

    public Checker.Colour getColour() {
        if (!checkers.isEmpty()) {
            return checkers.getFirst().getColour();
        } else {
            return null;
        }
    }

    public Checker get(int index) {
        return checkers.get(index);
    }

    /*public boolean canAddChecker(Checker checker) {
        if (checkers.isEmpty()) {
            return true;
        } else if (checkers.contains(getColour() == checker.getColour())) {}
    }*/
}

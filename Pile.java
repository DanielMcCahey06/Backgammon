import java.util.ArrayList;
import java.util.List;

public class Pile {
    private List<Checker> checkers = new ArrayList<>();

    public void addChecker(Checker checker) {
        checkers.add(checker);
    }

    public void removeChecker(Checker checker) {
        if (!checkers.isEmpty()) {
            checkers.removeLast();
        }
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

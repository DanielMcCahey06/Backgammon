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

    @Override
    public String toString() {
        if (checkers.isEmpty()) {
            return "   ";
        } return checkers.getFirst().toString() + getNoOfCheckers() + " ";
    }
}

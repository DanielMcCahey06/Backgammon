import java.util.*;

public class Game {
    protected boolean switchPlayer;
    protected static final int WHITEHOMEPOSITION = -1;
    protected static final int BLACKHOMEPOSITION = 24;
    protected static final int WHITEBARPOSITION = 25;
    protected Board board;
    protected static Dice dice;
    protected static boolean isDiceSet = false;
    protected boolean equalDice = true; // check if dice rolls are equal at the start to decide the first player
    public static boolean gameInPlay = true;
    protected boolean isPlayer1Turn = true; // keep track of whose turn it is
    protected static final int width = 50;
    protected int stake = 1; //how much points the game is worth. can be doubled
    protected DoubleDice doubleDice;

    public static final String border = "-".repeat(width);

    // Colour Codes
    public static final String Green = "\u001B[32m";
    public static final String Red = "\u001B[31m";
    public static final String Reset = "\u001B[0m";

    public Game() {
        initialiseGame(); // Set up game board with checkers in their starting positions
    }

    protected void initialiseGame() {
        board = createBoard();
        dice = new Dice();
        BoardRenderer boardRenderer = new BoardRenderer(board); // Create BoardRenderer
        board.addObserver(boardRenderer); // Register BoardRenderer as an observer
        doubleDice = new DoubleDice(); //intialise Double Dice
        outputMessage("Welcome To Backgammon!");
    }

    protected Board createBoard() {
        return new Board();
    }

    protected boolean start(Player player1, Player player2) {
        Scanner scanner = new Scanner(System.in); // allows reading from the console
        System.out.println("Now to determine who goes first!");

        // Call function which determines who goes first
        firstToPlay(player1, player2);

        /*
        Main game loop. Checks which players turn it is. Displays the board for that player and takes in the users
        input. Passes the users input to processaction function
         */
        while (gameInPlay) {
            Player currentPlayer = isPlayer1Turn ? player1 : player2;
            Player otherPlayer = isPlayer1Turn ? player2 : player1;
            int currentPlayerNumber = isPlayer1Turn ? 1 : 2;
            int otherPlayerNumber = isPlayer1Turn ? 2 : 1;

            // Display information for current player
            board.notifyObservers();
            outputMessage(player1.getName() + "'s score is " + player1.getScore() + "  " + player2.getName() + "'s score is " + player2.getScore() + "  Score to Win: " + Match.pointsToWin);
            outputMessage(currentPlayer.getName() + "'s turn ('" + currentPlayer.getDisplay() + "' Checkers)");
            displayPipNumbers(currentPlayer, isPlayer1Turn);
            outputMessage("Enter 'hint' for a list of possible commands");

            boolean turncomplete = false;

            while(!turncomplete) {
                String action = scanner.nextLine();// stores user input into 'action'
                action = action.toUpperCase();

                if (processAction(action, currentPlayer, otherPlayer, otherPlayerNumber)) {
                    isPlayer1Turn = !isPlayer1Turn;
                    turncomplete = true;
                }

                if(!gameInPlay){
                    return true;
                }
            }
        }
        return false;
    }

    /*
    Function to process the players command
     */
    protected boolean processAction(String action, Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        switch (action) {
            case "HINT" -> {
                gameHelp();
                return false;
            }
            case "DOUBLE" -> {
                gameInPlay = handleDoubleRequest(currentPlayer, otherPlayer, otherPlayerNumber);
                return !gameInPlay;
            }

            case "Q" -> {
                gameInPlay = false;
                errorMessage("Game over!");
                return false;
            }

            case "ROLL" -> {
                boolean play;
                play = handleRoll(currentPlayer, otherPlayer, otherPlayerNumber);
                return play;
            }

            case "PIP" -> {
                pipCount();
                return false;
            }

            case "DICE" -> {
                setDice();
                return false;
            }

            default -> {
                errorMessage("Invalid command");
                return false;
            }
        }
    }

    protected boolean handleRoll(Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        switchPlayer = false;
        int[] roll = generateDiceRoll(); // Generate dice roll - check if dice has been set and if not generate new values
        outputMessage("Dice roll:" + Dice.diceFace(roll[0]) + " " + Dice.diceFace(roll[1]));

        boolean isDoubles = checkIfDoubles(roll);
        if(isDoubles){
            roll = new int[]{roll[0], roll[0], roll[0], roll[0]};
        }
        boolean[] usedDice = generateDiceMarker(isDoubles);


        while (!switchPlayer) {
            // Generate list of legal moves
            List<String> legalMoves = generateLegalMoves(currentPlayer, roll, usedDice, isDoubles);

            if (legalMoves.isEmpty()) {
                outputMessage("No legal moves available. Turn passes to other player");
                return true;
            }

            // Display Legal Moves
            String[] moveParts = getUserMove(legalMoves); // function to display the legal moves available and process user input
            int moveDistance = getMoveDistance(moveParts, currentPlayer);

            if(processMove(moveParts, moveDistance, currentPlayer)){
                markUsedDice(roll, moveDistance, usedDice);
            }

            //Board.display(isPlayer1Turn);
            board.notifyObservers();
            if (board.checkWinCondition(currentPlayer)) {
                int winConditionMultiplier = getWinCondition(currentPlayer, otherPlayer);
                currentPlayer.increaseScore(stake * winConditionMultiplier);
                gameInPlay = false;
                return false;
            }
            int movesAvailable = getRemainingMoves(usedDice);
            outputMessage("Number of Moves Remaining: " + movesAvailable);
            switchPlayer = (movesAvailable == 0);
        }
        return true;
    }

    protected boolean processMove(String[] moveParts, int moveDistance, Player currentPlayer) {
        boolean moveCompleted = false;
        if (moveParts[0].equals("bar")) {
            int endPilePosition = Integer.parseInt(moveParts[1]);

            // Process move from bar
            if (processMoveFromBar(currentPlayer, endPilePosition)) {
                moveCompleted = true;
                //markUsedDice(roll, moveDistance, usedDice);
            }

        } else if (moveParts[1].equals("off")) {
            int startPilePosition = Integer.parseInt(moveParts[0]);
            if (processMoveToHome(currentPlayer, startPilePosition)) {
                moveCompleted = true;
                //markUsedDice(roll, moveDistance, usedDice);
            }
        } // Standard move around the board
        else {
            int startPilePosition = Integer.parseInt(moveParts[0]);
            int endPilePosition = Integer.parseInt(moveParts[1]);

            if (processMove(currentPlayer, startPilePosition, endPilePosition)) {
                moveCompleted = true;
                // Mark the dice value used
                //markUsedDice(roll, moveDistance, usedDice);
            }
        }
        return moveCompleted;
    }

    // Function to processMoveFromBar
    protected boolean processMoveFromBar(Player currentPlayer, int endPilePosition) {
        Pile targetPile = board.getPile(endPilePosition - 1);
        Pile barPile = currentPlayer.getChecker() == Checker.Colour.WHITE ? board.getBarPile(Board.PLAYER1BARINDEX) : board.getBarPile(Board.PLAYER2BARINDEX);
        Checker checkerToMove = barPile.removeTopChecker();

        if (targetPile.getNoOfCheckers() == 1) {
            Checker.Colour checkerOnEndPileColour = targetPile.getColour();
            if (checkerToMove.getColour() != checkerOnEndPileColour) {
                int opponentBarIndex = (checkerOnEndPileColour == Checker.Colour.BLACK ? 0 : 1);
                Checker checkerOnEndPile = targetPile.removeTopChecker();
                board.getBarPile(opponentBarIndex).addChecker(checkerOnEndPile);
            }
        }

        targetPile.addChecker(checkerToMove);
        checkerToMove.setPosition(endPilePosition - 1);

        System.out.println("Moved " + currentPlayer.getChecker() + " checker from bar to position " + (endPilePosition + 1));
        return true;
    }

    protected List<String> generateLegalMoves(Player currentPlayer, int[] roll, boolean[] usedDice, boolean isDoubles) {
        // List to store legal moves
        List<String> legalMoves = new ArrayList<>();
        StringBuilder availableDice = new StringBuilder(currentPlayer.getName() + " to play ");

        for (int i = 0; i < roll.length; i++) {
            if (!usedDice[i]) {
                availableDice.append(roll[i]);
                if (i != roll.length - 1) {
                    availableDice.append("-");
                }
            }
        }

        availableDice.append(". Select from:");
        System.out.println(availableDice);

        boolean allInHomeRange = allCheckersInHomeRange(currentPlayer);

        int playerBarIndex = (currentPlayer.getChecker() == Checker.Colour.BLACK) ? 0 : 1;

        // No checkers in the bar
        if (board.getBarPile(playerBarIndex).getCheckers().isEmpty()) {
            // Generate possible moves
            for (Pile pile : board.getPiles()) {
                // Check if pile has any checkers in it
                if (!pile.getCheckers().isEmpty()) {

                    Checker topChecker = pile.getCheckers().getLast();

                    if (topChecker.getColour() == currentPlayer.getChecker()) {

                        // Position of current Checker -- Range: 0 - 23
                        int position = topChecker.getPosition();

                        if (!isDoubles) {
                            if (topChecker.getColour() == Checker.Colour.BLACK) {
                                generateNonDoubleMoves(roll, usedDice, position, legalMoves, currentPlayer, allInHomeRange, 1);
                            } else if (topChecker.getColour() == Checker.Colour.WHITE) {
                                generateNonDoubleMoves(roll, usedDice, position, legalMoves, currentPlayer, allInHomeRange, -1);
                            }
                        } else {  // It is doubles
                            if (topChecker.getColour() == Checker.Colour.BLACK) {
                                generateDoubleMoves(roll[0], usedDice, position, legalMoves, currentPlayer, allInHomeRange, 1);
                            } else if (topChecker.getColour() == Checker.Colour.WHITE) {
                                generateDoubleMoves(roll[0], usedDice, position, legalMoves, currentPlayer, allInHomeRange, -1);
                            }
                        }
                    }
                }
            }
        } else {
            System.out.println(currentPlayer.getName() + " has checkers on the bar.");

            Set<Integer> uniqueEndPiles = new HashSet<>();

            // Generate legal moves from the bar
            for (int i = 0; i < usedDice.length; i++) {
                int diceRoll = roll[i];
                if (!usedDice[i]) {
                    int targetPileIndex = (currentPlayer.getChecker() == Checker.Colour.BLACK)
                            ? diceRoll - 1 // Black Checker
                            : 24 - diceRoll; // White Checkers

                    if (!uniqueEndPiles.contains(targetPileIndex)) {
                        Pile targetPile = board.getPile(targetPileIndex);
                        // Check if the target pile is a valid move
                        if (targetPile.getNoOfCheckers() < 2 || targetPile.getColour() == currentPlayer.getChecker()) {
                            legalMoves.add("Move bar-" + (targetPileIndex + 1));
                            uniqueEndPiles.add(targetPileIndex);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    protected void generateNonDoubleMoves(int[] roll, boolean[] usedDice, int position, List<String> legalMoves, Player currentPlayer, boolean allInHomeRange, int direction) {
        Checker.Colour colour = currentPlayer.getChecker();

        // Single dice moves
        if (!usedDice[0] && isLegalTarget(position + direction * roll[0], colour)) {
            legalMoves.add("Move " + (position + 1) + "-" + (position + direction * roll[0] + 1));
        }
        if (!usedDice[1] && isLegalTarget(position + direction * roll[1], colour)) {
            legalMoves.add("Move " + (position + 1) + "-" + (position + direction * roll[1] + 1));
        }

        // If all in home range
        if (allInHomeRange) {
            if (!usedDice[0] && position + direction * roll[0] == getHomePosition(direction)) {
                legalMoves.add("Move " + (position + 1) + "-off");
            }
            if (!usedDice[1] && position + direction * roll[1] == getHomePosition(direction)) {
                legalMoves.add("Move " + (position + 1) + "-off");
            }
        }
    }

    protected void generateDoubleMoves(int roll, boolean[] usedDice, int position, List<String> legalMoves, Player currentPlayer, boolean allInHomeRange, int direction) {
        Checker.Colour colour = currentPlayer.getChecker();

        // Move distance remains the same since we rolled doubles
        int targetPosition = position + direction * roll;
        boolean moveOn = false;

        for (int i = 0; i < usedDice.length; i++) {

            if (!usedDice[i] && isLegalTarget(targetPosition, colour)) {
                legalMoves.add("Move " + (position + 1) + "-" + (targetPosition + 1)); // Add legal move to list
                moveOn = true;
            }

            if (allInHomeRange) {
                if (!usedDice[i] && targetPosition == getHomePosition(direction)) {
                    legalMoves.add("Move " + (position + 1) + "-off");
                    moveOn = true;
                }
            }

            if (moveOn) {
                break;
            }
        }
    }

    protected boolean isLegalTarget(int targetPosition, Checker.Colour colour) {
        return targetPosition >= 0 && targetPosition <= 23 && board.isLegalMove(targetPosition, colour);
    }

    protected int getHomePosition(int direction) {
        return direction == 1 ? 24 : -1;
    }

    // Function to move a checker between two piles
    protected boolean processMove(Player currentPlayer, int startPos, int endPos) {
        Pile startPile = board.getPile(startPos - 1);
        Pile endPile = board.getPile(endPos - 1);
        Checker checkerToMove = startPile.removeTopChecker();

        if (endPile.getNoOfCheckers() == 1) {
            Checker.Colour checkerOnEndPileColour = endPile.getColour();
            if (checkerToMove.getColour() != checkerOnEndPileColour) {
                int opponentBarIndex = (checkerOnEndPileColour == Checker.Colour.BLACK ? 0 : 1);
                Checker checkerOnEndPile = endPile.removeTopChecker();
                board.getBarPile(opponentBarIndex).addChecker(checkerOnEndPile);
            }
        }
        endPile.addChecker(checkerToMove);
        checkerToMove.setPosition(endPos - 1);

        return true;
    }

    protected boolean processMoveToHome(Player currentPlayer, int startPos) {
        Pile startPile = board.getPile(startPos - 1);
        Checker checkerToMove = startPile.removeTopChecker();

        int homePosition = currentPlayer.isWhite() ? 0 : 1; // 0 for white, 1 for black

        board.getHomePile(homePosition).addChecker(checkerToMove);
        return true;
    }

    protected boolean allCheckersInHomeRange(Player player) {
        Checker.Colour colour = player.getChecker();
        for (Pile pile : board.getPiles()) {
            for (Checker checker : pile.getCheckers()) {
                if (checker.getColour() == colour) {
                    int position = checker.getPosition();
                    if ((colour == Checker.Colour.BLACK && position < 18) ||
                            (colour == Checker.Colour.WHITE && position > 5)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int getWinCondition(Player winningPlayer, Player otherPlayer) {
        int winningPlayerHomeIndex = (winningPlayer.getChecker() == Checker.Colour.WHITE) ? Board.PLAYER1HOMEINDEX : Board.PLAYER2HOMEINDEX;
        int losingPlayerHomeIndex = (otherPlayer.getChecker() == Checker.Colour.WHITE) ? Board.PLAYER1HOMEINDEX : Board.PLAYER2HOMEINDEX;
        int losingPlayerBarIndex = (otherPlayer.getChecker() == Checker.Colour.WHITE) ? Board.PLAYER1BARINDEX : Board.PLAYER2BARINDEX;

        boolean hasLosingPlayerBorneOffAny = board.getHomePile(losingPlayerHomeIndex).getNoOfCheckers() > 0;
        boolean hasLosingPlayerCheckersOnBar = board.getBarPile(losingPlayerBarIndex).getNoOfCheckers() > 0;

        // Check if losing player has any checkers in opponents home board
        boolean hasLosingPlayerInOppHome = false;
        int opponentStart = (winningPlayer.getChecker() == Checker.Colour.WHITE) ? 0 : 18; // Start of opponent home board
        int opponentEnd = (winningPlayer.getChecker() == Checker.Colour.WHITE) ? 5 : 23;  // End of opponent home board

        for (int i = opponentStart; i <= opponentEnd; i++) {
            if (!board.getPile(i).getCheckers().isEmpty() &&
                    board.getPile(i).getCheckers().get(0).getColour() == otherPlayer.getChecker()) {
                hasLosingPlayerInOppHome = true;
                break;
            }
        }

        // Determine win condition
        if (hasLosingPlayerBorneOffAny) {
            System.out.println("Congratulations on your single win!");
            return 1; // Single Win
        } else if (!hasLosingPlayerCheckersOnBar && !hasLosingPlayerInOppHome) {
            System.out.println("Congratulations on your Gammon win! Your score is now doubled.");
            return 2; // Gammon
        } else {
            System.out.println("Congratulations on your Backgammon win! Your score is now tripled.");
            return 3; // Backgammon
        }
    }

    protected void markUsedDice(int[] roll, int moveDistance, boolean[] usedDice) {

        // If the user is playing doubles
        if (roll.length == 4) {
            for (int i = 0; i < roll.length; i++) {
                if (!usedDice[i] && roll[i] == moveDistance) {
                    usedDice[i] = true;
                    return;
                }
            }
        } else {
            if (moveDistance == roll[0] && !usedDice[0]) {
                usedDice[0] = true;
            }
            if (moveDistance == roll[1] && !usedDice[1]) {
                usedDice[1] = true;
            }
        }
    }

    protected int getRemainingMoves(boolean[] usedDice) {
        int remaining = 0;
        for (boolean used : usedDice) {
            if (!used) {
                remaining++;
            }
        }
        return remaining;
    }

    public void displayPipNumbers(Player currentPlayer, boolean isPlayer1Turn) {
        System.out.println("Pip scores for " + currentPlayer.getName() + ":");

        for (int i = 0; i < 24; i++) {
            int checkerCount = board.getPile(i).getNoOfCheckers();

            if (board.getPile(i).getColour() == currentPlayer.getChecker() && board.getPile(i).getNoOfCheckers() > 0) {
                int pipNumber = isPlayer1Turn ? i + 1 : 24 - i;
                int pipScore = checkerCount * pipNumber;

                System.out.println("Cone " + (i + 1) + " -> " + pipScore);
            }
        }
    }

    public boolean isValidDicePair(String[] parts) {
        boolean isValid = true;

        try {
            // Parse both parts as integers
            int[] diceValues = new int[2];
            diceValues[0] = Integer.parseInt(parts[0]);
            diceValues[1] = Integer.parseInt(parts[1]);

            // Check if both values are between 1 and 6
            if (diceValues[0] < 1 || diceValues[0] > 6 || diceValues[1] < 1 || diceValues[1] > 6) {
                isValid = false;
            }

        } catch (NumberFormatException e) {
            // If parsing fails, the input is not valid
            isValid = false;
        }
        return isValid;
    }

    /*
    Function to calculate and display the pip count for each player
     */
    protected void pipCount() {
        int noOfPipsWhite = 0;
        int noOfPipsBlack = 0;
        for (int i = 0; i < 24; i++) {
            if (board.getPile(i).getColour() == Checker.Colour.WHITE) {
                noOfPipsWhite += board.getPile(i).getNoOfCheckers() * (i + 1) + board.getBarPile(1).getNoOfCheckers() * (24);
            } else {
                noOfPipsBlack += board.getPile(i).getNoOfCheckers() * (24 - i) + board.getBarPile(0).getNoOfCheckers() * (24);
            }
        }
        outputMessage("⚪ Pip count : " + noOfPipsWhite + "  ⚫ Pip count : " + noOfPipsBlack);
    }

    public static String stripAnsiCodes(String message) {
        return message.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    public static void errorMessage(String message) {
        int numberOfSpaces = (width - message.length()) / 2; // finds number of spaces needed to center message
        String spaces = " ".repeat(numberOfSpaces);
        System.out.println(Red + border); // creates border in red
        System.out.println(spaces + message + Red); // centers message in red
        System.out.println(border + Reset); // creates border in red and resets the colour back to default
    }

    public static void outputMessage(String message) {
        String formattedMessage = stripAnsiCodes(message); //
        if (formattedMessage.length() <= width) {
            int numberOfSpaces = (width - formattedMessage.length()) / 2; // finds number of spaces needed to center message
            String spaces = " ".repeat(numberOfSpaces);
            System.out.println(Green + border); // creates border in green
            System.out.println(spaces + message + Green); // centers message in green
            System.out.println(border + Reset); // creates border in green and resets the colour back to default
        } else { // method for long messages
            StringBuilder newMessage = new StringBuilder();
            String[] words = formattedMessage.split(" "); // splits words within the message
            int currentLineLength = 0;
            for (String word : words) {
                if (currentLineLength + word.length() > width) {
                    if (currentLineLength > 0) { // creates new line for the message if it exceeds the width
                        newMessage.append("\n");
                    }
                    currentLineLength = 0;
                }
                newMessage.append(word).append(" ");
                currentLineLength += word.length() + 1;
            }
            System.out.println(Green + border); // creates border in green
            for (String line : newMessage.toString().split("\n")) {
                int numberOfSpaces = (width - line.length()) / 2; // finds number of spaces needed to center message
                String spaces = " ".repeat(Math.max(0, numberOfSpaces)); // prevents negative spaces
                System.out.println(spaces + line.trim() + Green); // trims trailing space
            }
            System.out.println(border + Reset); // creates border in green and resets the colour back to default
        }
    }

    protected void gameHelp() {
        outputMessage("Enter 'roll' to roll the dice, 'Q' to quit the game, 'pip' to view pip count, 'double' to propose a double!");
        System.out.println();
    }

    private void firstToPlay(Player player1, Player player2) {
        while (equalDice) {
            int[] startingRoll = dice.roll();
            int player1Roll = startingRoll[0];
            int player2Roll = startingRoll[1];
            outputMessage(player1.getName() + " rolls a " + Dice.diceFace(player1Roll) + " and " + player2.getName() + " rolls a " + Dice.diceFace(player2Roll));
            if (player1Roll > player2Roll) {
                outputMessage(player1.getName() + " will go first!");
                equalDice = false;
            } else if (player2Roll > player1Roll) {
                outputMessage(player2.getName() + " will go first!");
                isPlayer1Turn = false;
                equalDice = false;
            } else {
                outputMessage("Dice rolls are equal so will go again!");
            }
        }
    }

    protected boolean handleDoubleRequest(Player currentPlayer, Player otherPlayer, int otherPlayerNumber){
        boolean playOn = true;
        Scanner scanner = new Scanner(System.in);
        if ((isPlayer1Turn && (DoubleDice.getOwner() == 2)) || (!isPlayer1Turn && (DoubleDice.getOwner() == 1))) {
            outputMessage(currentPlayer.getName() + "you are not currently in possession of the Double Dice so you cannot propose a double");
        } else {
            outputMessage(currentPlayer.getName() + " has proposed a double. " + otherPlayer.getName() + ", to accept and double the stakes of this game input 'A'. To Decline and forfeit this game input 'D'");
            String reaction = scanner.nextLine();
            reaction = reaction.toUpperCase();
            while (!reaction.equals("A") && !reaction.equals("D")) {
                errorMessage("Invalid input. Input must be A or D");
                reaction = scanner.nextLine().toUpperCase();
            }
            if (reaction.equals("A")) {
                handleAcceptedDouble(currentPlayer, otherPlayer, otherPlayerNumber);
            } else {
                outputMessage("Congratulations " + currentPlayer.getName() + " you win!");
                currentPlayer.increaseScore(stake);
                playOn = false;
            }
        }
        return playOn;
    }

    protected void handleAcceptedDouble(Player currentPlayer, Player otherPlayer, int otherPlayerNumber){
        doubleDice.increaseDouble(); //multiplier is doubled
        stake = DoubleDice.getDouble(); //stake equals new double amount
        doubleDice.setOwner(otherPlayerNumber); //set owner of double dice to opposition player
        outputMessage("The stakes have been doubled. This game is now worth " + DoubleDice.getDouble() + " points. " + otherPlayer.getName() + " is now the holder of the doubling dice.");
        System.out.println("Enter your  next move " + currentPlayer.getName() + ", or 'hint' for a list of possible commands: ");
    }

    protected boolean checkIfDoubles(int[] roll){
        boolean isDoubles;
        if(roll[0] == roll[1]){
            System.out.println("You rolled doubles!");
            isDoubles = true;
        }
        else{
            isDoubles = false;
        }
        return isDoubles;
    }

    protected int getMoveDistance(String[] moveParts, Player currentPlayer) {
        // Special case: move starts from the bar
        if (moveParts[0].equals("bar")) {
            int endPilePosition = Integer.parseInt(moveParts[1]);

            // White starts from 25, Black starts from 0
            return currentPlayer.isWhite()
                    ? WHITEBARPOSITION - endPilePosition // White distance calculation
                    : endPilePosition;                  // Black distance calculation
        }

        // Special case: move ends "off" the board
        if (moveParts[1].equals("off")) {
            int startPilePosition = Integer.parseInt(moveParts[0]);

            // White moves towards -1, Black moves towards 24
            return currentPlayer.isWhite()
                    ? (startPilePosition - 1) - WHITEHOMEPOSITION  // Distance for White
                    : BLACKHOMEPOSITION - (startPilePosition - 1); // Distance for Black
        }

        // Standard case: move from one pile to another
        int startPilePosition = Integer.parseInt(moveParts[0]);
        int endPilePosition = Integer.parseInt(moveParts[1]);

        // Calculate absolute move distance
        return Math.abs(endPilePosition - startPilePosition);
    }

    protected String[] getUserMove(List<String> legalMoves) {
        char moveLetter = 'A';
        for (String move : legalMoves) {
            System.out.println(moveLetter + ")" + move);
            moveLetter++;
        }
        System.out.println("--------------------------------------------------");
        Scanner scanner = new Scanner(System.in);
        char userChoice;

        while(true) {
            System.out.println("Enter a command letter from the list to make a move: ");
            String input = scanner.nextLine().toUpperCase(); // Convert input to uppercase for consistency

            // Validate input length and character
            if (input.length() == 1) {
                userChoice = input.charAt(0);

                // Check if userChoice is within the valid range of legal moves
                int choiceIndex = userChoice - 'A'; // Calculate the index from the character
                if (choiceIndex >= 0 && choiceIndex < legalMoves.size()) {
                    String selectedMove = legalMoves.get(choiceIndex);

                    // Parse the move into parts
                    String[] moveParts = selectedMove.split(" ");
                    String startPart = moveParts[1].split("-")[0];
                    String endPart = moveParts[1].split("-")[1];

                    return new String[] {startPart, endPart}; // Return the parsed parts
                }
            }
            // If input is invalid, prompt the user again
            System.out.println("Invalid input. Please enter a letter corresponding to a legal move.");
        }
    }

    protected void setDice() {
        Scanner scanner = new Scanner(System.in);
        int[] diceValues = new int[2];
        boolean validInput = false; // Flag to track if valid dice values are entered

        while (!validInput) {
            System.out.println("Enter the two dice values in the form: <int> <int>, ensuring they are separated by a space:");
            String values = scanner.nextLine();
            String[] parts = values.split(" ");

            if (isValidDicePair(parts)) {
                diceValues[0] = Integer.parseInt(parts[0]);
                diceValues[1] = Integer.parseInt(parts[1]);
                dice.setDice(diceValues[0], diceValues[1]);
                isDiceSet = true;
                validInput = true; // Exit the loop
                System.out.println("Dice values now set for the next dice roll.");
                System.out.println("Enter your next play or 'hint' for a list of possible commands: ");
            } else {
                errorMessage("Invalid input. Enter two valid integer values!");
            }
        }
    }

    protected int[] generateDiceRoll() {
        int[] roll;
        if (!isDiceSet) {
            // Roll the dice if no manual values are set
            roll = dice.roll();
        } else {
            // Return the manually set dice values
            isDiceSet = false; // Reset the flag after use
            roll = dice.getSetDice();
        }
        return roll;
    }

    protected boolean[] generateDiceMarker(boolean isDoubles){
        boolean[] usedDice;
        if (isDoubles) {
            usedDice = new boolean[]{false, false, false, false};
        } else {
            usedDice = new boolean[]{false, false};
        }
        return usedDice;
    }
}

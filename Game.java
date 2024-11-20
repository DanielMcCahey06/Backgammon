import java.util.*;

public class Game {
    protected Player player1; // White Checkers
    protected Player player2; // Black Checkers
    private boolean switchPlayer;
    private int WHITEHOMEPOSITION = -1;
    private int BLACKHOMEPOSITION = 24;
    protected Board board = new Board();
    private Dice dice;
    private boolean equalDice = true; // check if dice rolls are equal at the start to decide the first player
    private boolean gameInPlay = true;
    private boolean isPlayer1Turn = true; // keep track of whose turn it is
    public static final String Green = "\u001B[32m";
    public static final String Red = "\u001B[31m";
    public static final String Reset = "\u001B[0m";
    private static final int width = 50;
    public static final String border = "-".repeat(width);
    private static int noOfMoves = 0;
    private int stake = 1; //how much points the game is worth. can be doubled
    private DoubleDice DoubleDice;
    public Game() {
        initialiseGame(); // Set up game board with checkers in their starting positions
    }

    private void initialiseGame() {
        dice = new Dice();
        DoubleDice = new DoubleDice(); //intialise Double Dice

        // add initial checkers
        board.addChecker(23, new Checker(23, Checker.Colour.WHITE));
        board.addChecker(23, new Checker(23, Checker.Colour.WHITE));
        board.addChecker(18, new Checker(18, Checker.Colour.BLACK));
        board.addChecker(18, new Checker(18, Checker.Colour.BLACK));
        board.addChecker(18, new Checker(18, Checker.Colour.BLACK));
        board.addChecker(18, new Checker(18, Checker.Colour.BLACK));
        board.addChecker(18, new Checker(18, Checker.Colour.BLACK));
        board.addChecker(16, new Checker(16, Checker.Colour.BLACK));
        board.addChecker(16, new Checker(16, Checker.Colour.BLACK));
        board.addChecker(16, new Checker(16, Checker.Colour.BLACK));
        board.addChecker(12, new Checker(12, Checker.Colour.WHITE));
        board.addChecker(12, new Checker(12, Checker.Colour.WHITE));
        board.addChecker(12, new Checker(12, Checker.Colour.WHITE));
        board.addChecker(12, new Checker(12, Checker.Colour.WHITE));
        board.addChecker(12, new Checker(12, Checker.Colour.WHITE));
        board.addChecker(11, new Checker(11,Checker.Colour.BLACK));
        board.addChecker(11, new Checker(11,Checker.Colour.BLACK));
        board.addChecker(11, new Checker(11,Checker.Colour.BLACK));
        board.addChecker(11, new Checker(11,Checker.Colour.BLACK));
        board.addChecker(11, new Checker(11,Checker.Colour.BLACK));
        board.addChecker(7, new Checker(7,Checker.Colour.WHITE));
        board.addChecker(7, new Checker(7, Checker.Colour.WHITE));
        board.addChecker(7, new Checker(7, Checker.Colour.WHITE));
        board.addChecker(5, new Checker(5, Checker.Colour.WHITE));
        board.addChecker(5, new Checker(5, Checker.Colour.WHITE));
        board.addChecker(5, new Checker(5, Checker.Colour.WHITE));
        board.addChecker(5, new Checker(5, Checker.Colour.WHITE));
        board.addChecker(5, new Checker(5, Checker.Colour.WHITE));
        board.addChecker(0, new Checker(0, Checker.Colour.BLACK));
        board.addChecker(0, new Checker(0, Checker.Colour.BLACK));

        System.out.println("Welcome to Backgammon");
        System.out.println();
    }

    public void start(Player player1, Player player2) {
        Scanner scanner = new Scanner(System.in); // allows reading from the console
        /*System.out.println("Enter Player 1 name (White Checker):");
        String player1Name = scanner.nextLine();
        player1 = new Player(player1Name, Checker.Colour.WHITE);

        System.out.println("Enter Player 2 name (Black Checker):");
        String player2Name = scanner.nextLine();
        player2 = new Player(player2Name, Checker.Colour.BLACK);*/

        System.out.println("Now to determine who goes first!");

        /*
        Method to initialise player names and rolls dice to determine who starts first. Keeps running until there
        is a winner for who starts first.
         */
        while (equalDice) {
            int[] startingRoll = Dice.roll();
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

        /*
        Main game loop. Checks which players turn it is. Displays the board for that player and takes in the users
        input. Passes the users input to processaction function
         */
        while (gameInPlay) {
            Player currentPlayer = isPlayer1Turn ? player1 : player2;
            Player otherPlayer = isPlayer1Turn ? player2 : player1;
            int currentPlayerNumber = isPlayer1Turn ? 1:2;
            int otherPlayerNumber = isPlayer1Turn ? 2:1;

            Board.display(isPlayer1Turn);
            outputMessage(currentPlayer.getName() + "'s turn ('" + currentPlayer.getDisplay() + "' Checkers)");
            displayPipNumbers(currentPlayer, isPlayer1Turn);
            outputMessage(player1.getName() + "'s score is " + player1.getScore() + "  " + player2.getName() + "'s score is " + player2.getScore() + "  Score to Win: " + Match.pointsToWin);
            outputMessage("Enter 'hint' for a list of possible commands");
            String action = scanner.nextLine();// stores user input into 'action'
            action = action.toUpperCase();

            if(action.equals("HINT")) {
                gameHelp();
                System.out.println("Enter your choice " + currentPlayer.getName() + ": ");
                action = scanner.nextLine().toUpperCase();
            }
            if(action.equals("DOUBLE")){
                if((isPlayer1Turn && (DoubleDice.getOwner() == 2))||(!isPlayer1Turn && (DoubleDice.getOwner() == 1))){
                    outputMessage(currentPlayer.getName() + "you are not currently in possession of the Double Dice so you cannot propose a double");
                    outputMessage("Enter your move " + currentPlayer.getName() + ": ");
                    action = scanner.nextLine().toUpperCase();
                }
                else{
                outputMessage(currentPlayer.getName() + " has proposed a double. " + otherPlayer.getName() + ", to accept and double the stakes of this game input 'A'. To Decline and forfeit this game input 'D'");
                String reaction = scanner.nextLine();
                reaction = reaction.toUpperCase();
                while (!reaction.equals("A") && !reaction.equals("D")) {
                    errorMessage("Invalid input. Input must be A or D");
                    reaction = scanner.nextLine().toUpperCase();
                }
                if(reaction.equals("A")){
                    DoubleDice.increaseDouble(); //multiplier is doubled
                    stake = DoubleDice.getDouble(); //stake equals new double amount
                    DoubleDice.setOwner(otherPlayerNumber); //set owner of double dice to opposition player
                    outputMessage("The stakes have been doubled. This game is now worth "+DoubleDice.getDouble() + " points. " + otherPlayer.getName() + " is now the holder of the doubling dice.");
                    outputMessage("Enter your move " + currentPlayer.getName() + ": ");
                    action = scanner.nextLine().toUpperCase();
                } else if(reaction.equals("D")) {
                    outputMessage("Congratulations " + currentPlayer.getName() + " you win!");
                    currentPlayer.increaseScore(stake);
                    gameInPlay = false;
                    break;
                }
                }
            }

            if (processAction(action, currentPlayer)) {
                isPlayer1Turn = !isPlayer1Turn;
            }
        }
    }

    /*
    Function to process the players command
     */
    private boolean processAction(String action, Player currentPlayer) {
        switch (action) {
            case "Q" -> {
                gameInPlay = false;
                errorMessage("Game over!");
                return false;
            }

            case "ROLL" -> {
                int[] roll = Dice.roll();
                outputMessage("Dice roll:" + Dice.diceFace(roll[0]) + " " + Dice.diceFace(roll[1]));

                switchPlayer = false;
                boolean[] usedDice;

                boolean isDoubles = (roll[0] == roll[1]);

                if(isDoubles){
                    System.out.println("You rolled Doubles!");
                    System.out.println();
                    roll = new int[]{roll[0], roll[0], roll[0], roll[0]};
                    usedDice = new boolean[]{false, false, false, false};
                }
                else
                {
                    usedDice = new boolean[]{false, false};
                }

                while(!switchPlayer) {
                    // Generate list of legal moves
                    List<String> legalMoves = generateLegalMoves(currentPlayer, roll, usedDice, isDoubles);

                    if (legalMoves.isEmpty()) {
                        outputMessage("No legal moves available. Turn passes to other player");
                        return true;
                    }

                    // Display Legal Moves
                    char moveLetter = 'A';
                    for (String move : legalMoves) {
                        System.out.println(moveLetter + ")" + move);
                        moveLetter++;
                    }
                    System.out.println("--------------------------------------------------");
                    System.out.println("Enter a command letter from the list to make a move: ");

                    Scanner scanner = new Scanner(System.in);
                    char userChoice = scanner.nextLine().charAt(0);
                    userChoice = Character.toUpperCase(userChoice);
                    boolean correctInput = false;

                    // Process User Input
                    for (int i = 0; i < legalMoves.size(); i++) {
                        // Check which letter matches the user input
                        if (userChoice == 'A' + i) {
                            correctInput = true;
                            String selectedMove = legalMoves.get(i);

                            String[] moveParts = selectedMove.split(" ");
                            String startPart = moveParts[1].split("-")[0];
                            String endPart = moveParts[1].split("-")[1];
                            int moveDistance;

                            // Check if the move is off the bar
                            if(startPart.equals("bar")){
                                int endPilePosition = Integer.parseInt(endPart);

                                // Process move from bar
                                if(processMoveFromBar(currentPlayer, endPilePosition)){
                                    moveDistance = (currentPlayer.isWhite()) ? 25 - endPilePosition : endPilePosition;
                                    markUsedDice(roll, moveDistance, usedDice);
                                }
                            }
                            else if(endPart.equals("off")) {
                                int startPilePosition = Integer.parseInt(startPart);
                                moveDistance = currentPlayer.isWhite()
                                        ? (startPilePosition-1) - WHITEHOMEPOSITION  // White moves towards -1
                                        : BLACKHOMEPOSITION - (startPilePosition-1); // Black moves to 24


                                if(processMoveToHome(currentPlayer, startPilePosition)){
                                    markUsedDice(roll, moveDistance, usedDice);
                                }
                            } // Standard move around the board
                            else {
                                int startPilePosition = Integer.parseInt(startPart);
                                int endPilePosition = Integer.parseInt(endPart);
                                if (processMove(currentPlayer, startPilePosition, endPilePosition)) {
                                    System.out.println("Move Completed. ");
                                    System.out.println();

                                    // Mark the dice value used
                                    moveDistance = Math.abs(endPilePosition - startPilePosition);
                                    markUsedDice(roll, moveDistance, usedDice);
                                }
                            }
                        }
                    }
                    if(!correctInput){
                        errorMessage("Invalid Command!");
                        continue;
                    }
                    Board.display(isPlayer1Turn);
                    if (isGameWon(currentPlayer)) {
                        outputMessage("Congratulations " + currentPlayer.getName() + " you win!");
                        currentPlayer.increaseScore(stake);
                        gameInPlay = false;
                        return false;
                    }
                    int movesAvailable = getRemainingMoves(usedDice);
                    System.out.println("-----------------------------------------------");
                    System.out.println("Number of remaining moves: " + movesAvailable);
                    System.out.println("-----------------------------------------------");
                    switchPlayer = (movesAvailable == 0);
                }
                noOfMoves += 1;
                return true;
            }
            case "PIP" -> {
                pipCount();
                return true;
            }
            case "DICE" -> {
                Scanner scanner = new Scanner(System.in);
                int[] bothValues = new int[2];
                System.out.println("Enter the two dice values: ");
                String values = scanner.nextLine();
                String[] parts = values.split(" ");

                if (parts.length != 2) {
                    errorMessage("Enter exactly two values");
                    return false;
                } else {
                    try {
                        bothValues[0] = Integer.parseInt(parts[0]);
                        bothValues[1] = Integer.parseInt(parts[1]);
                        if ( bothValues[0] < 1 || bothValues[1] < 1 || bothValues[0] > 6 || bothValues[1] > 6) {
                            errorMessage("Error : Each dice value must be between 1 and 6");
                            return false;
                        }

                        int[] roll = Dice.roll(bothValues[0], bothValues[1]);
                        outputMessage("Dice roll:" + Dice.diceFace(roll[0]) + " " + Dice.diceFace(roll[1]));
                        return true;
                    } catch (NumberFormatException e) {
                        errorMessage("Invalid input. Enter two integer values!");
                        return false;
                    }
                }
            }
            default -> {
                errorMessage("Invalid command");
                return false;
            }
        }
    }

    private boolean processMoveFromBar(Player currentPlayer, int endPilePosition) {
        Pile targetPile = Board.piles[endPilePosition-1];
        Pile barPile = currentPlayer.getChecker() == Checker.Colour.WHITE ? Board.bar[1] : Board.bar[0];
        Checker checkerToMove = barPile.removeTopChecker();

        if(targetPile.getNoOfCheckers() == 1){
            Checker.Colour checkerOnEndPileColour = targetPile.getColour();
            if(checkerToMove.getColour() != checkerOnEndPileColour){
                int opponentBarIndex = (checkerOnEndPileColour == Checker.Colour.BLACK ? 0 : 1);
                Checker checkerOnEndPile = targetPile.removeTopChecker();
                Board.bar[opponentBarIndex].addChecker(checkerOnEndPile);
            }
        }

        targetPile.addChecker(checkerToMove);
        checkerToMove.setPosition(endPilePosition-1);

        System.out.println("Moved " + currentPlayer.getChecker() + " checker from bar to position " + (endPilePosition + 1));
        return true;
    }

    private List<String> generateLegalMoves(Player currentPlayer, int[] roll, boolean[] usedDice, boolean isDoubles) {
        // List to store legal moves
        List<String> legalMoves = new ArrayList<>();
        StringBuilder availableDice = new StringBuilder(currentPlayer.getName() + " to play ");

        for (int i = 0; i < roll.length; i++) {
            if (!usedDice[i]) {
                availableDice.append(roll[i]);
                if(i != roll.length-1){
                    availableDice.append("-");
                }
            }
        }

        availableDice.append(". Select from:");
        System.out.println(availableDice);

        boolean allInHomeRange = allCheckersInHomeRange(currentPlayer);

        int playerBarIndex = (currentPlayer.getChecker() == Checker.Colour.BLACK) ? 0 : 1;

        // No checkers in the bar
        if(Board.bar[playerBarIndex].getCheckers().isEmpty()) {
            // Generate possible moves
            for (Pile pile : Board.piles) {
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
        }
        else {
            System.out.println(currentPlayer.getName() + " has checkers on the bar.");

            Set<Integer> uniqueEndPiles = new HashSet<>();

            // Generate legal moves from the bar
            for (int i=0 ; i < usedDice.length; i++) {
                int diceRoll = roll[i];
                if (!usedDice[i]) {
                    int targetPileIndex = (currentPlayer.getChecker() == Checker.Colour.BLACK)
                            ? diceRoll - 1 // Black Checker
                            : 24 - diceRoll; // White Checkers

                    if(!uniqueEndPiles.contains(targetPileIndex)) {
                        Pile targetPile = Board.piles[targetPileIndex];
                        // Check if the target pile is a valid move
                        if(targetPile.getNoOfCheckers() < 2 || targetPile.getColour() == currentPlayer.getChecker()){
                            legalMoves.add("Move bar-" + (targetPileIndex + 1));
                            uniqueEndPiles.add(targetPileIndex);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    private void generateNonDoubleMoves(int[] roll, boolean[] usedDice, int position, List<String> legalMoves, Player currentPlayer, boolean allInHomeRange, int direction){
        Checker.Colour colour = currentPlayer.getChecker();

        // Single dice moves
        if (!usedDice[0] && isLegalTarget(position + direction * roll[0], colour)) {
            legalMoves.add("Move " + (position + 1) + "-" + (position + direction * roll[0] + 1));
        }
        if (!usedDice[1] && isLegalTarget(position + direction * roll[1], colour)) {
            legalMoves.add("Move " + (position + 1) + "-" + (position + direction * roll[1] + 1));
        }

        // If all in home range
        if(allInHomeRange){
            if (!usedDice[0] && position + direction * roll[0] == getHomePosition(direction)) {
                legalMoves.add("Move " + (position + 1) + "-off");
            }
            if (!usedDice[1] && position + direction * roll[1] == getHomePosition(direction)) {
                legalMoves.add("Move " + (position + 1) + "-off");
            }
        }
    }

    private void generateDoubleMoves(int roll, boolean[] usedDice, int position, List<String> legalMoves, Player currentPlayer, boolean allInHomeRange, int direction){
        Checker.Colour colour = currentPlayer.getChecker();

        // Move distance remains the same since we rolled doubles
        int targetPosition = position + direction * roll;
        boolean moveOn = false;

        for (int i=0 ; i < usedDice.length ; i++){

            if (!usedDice[i] && isLegalTarget(targetPosition, colour)) {
                legalMoves.add("Move " + (position + 1) + "-" + (targetPosition + 1)); // Add legal move to list
                moveOn = true;
            }

            if(allInHomeRange){
                if(!usedDice[i] && targetPosition == getHomePosition(direction)){
                    legalMoves.add("Move " + (position + 1) + "-off");
                    moveOn = true;
                }
            }

            if(moveOn){
                break;
            }
        }
    }

    private boolean isLegalTarget(int targetPosition, Checker.Colour colour){
        return targetPosition >= 0 && targetPosition <= 23 && board.isLegalMove(targetPosition, colour);
    }

    private int getHomePosition(int direction){
        return direction == 1 ? 24 : -1;
    }

    // Function to move a checker between two piles
    private boolean processMove(Player currentPlayer, int startPos, int endPos) {
        Pile startPile = Board.piles[startPos-1];
        Pile endPile = Board.piles[endPos-1];
        Checker checkerToMove = startPile.removeTopChecker();

        if(endPile.getNoOfCheckers() == 1){
            Checker.Colour checkerOnEndPileColour = endPile.getColour();
            if(checkerToMove.getColour() != checkerOnEndPileColour) {
                int opponentBarIndex = (checkerOnEndPileColour == Checker.Colour.BLACK ? 0 : 1);
                Checker checkerOnEndPile = endPile.removeTopChecker();
                Board.bar[opponentBarIndex].addChecker(checkerOnEndPile);
            }
        }
        endPile.addChecker(checkerToMove);
        checkerToMove.setPosition(endPos - 1);

        return true;
    }

    private boolean processMoveToHome(Player currentPlayer, int startPos){
        Pile startPile = Board.piles[startPos-1];
        Checker checkerToMove = startPile.removeTopChecker();

        int homePosition = currentPlayer.isWhite() ? 0 : 1; // 0 for white, 1 for black

        Board.home[homePosition].addChecker(checkerToMove);
        return true;
    }

    private boolean allCheckersInHomeRange(Player player){
        Checker.Colour colour = player.getChecker();
        for (Pile pile : Board.piles) {
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

    /*
    ISSUE: usedDice is coming into this function already marked.
     */
    private void markUsedDice(int[] roll, int moveDistance, boolean[] usedDice){

        // If the user is playing doubles
        if(roll.length == 4) {
            for (int i = 0; i < roll.length; i++) {
                if (!usedDice[i] && roll[i] == moveDistance) {
                    usedDice[i] = true;
                    return;
                }
            }
        }
        else
        {
            if (moveDistance == roll[0] && !usedDice[0]) {
                usedDice[0] = true;
            }
            if (moveDistance == roll[1] && !usedDice[1]) {
                usedDice[1] = true;
            }
        }
    }

    private int getRemainingMoves(boolean[] usedDice){
        int remaining = 0;
        for (boolean used : usedDice){
            if(!used) {
                remaining++;
            }
        }
        return remaining;
    }

    public void displayPipNumbers(Player currentPlayer, boolean isPlayer1Turn){
        System.out.println("Pip scores for " + currentPlayer.getName() + ":");

        for (int i=0; i<24; i++){
            int checkerCount = Board.piles[i].getNoOfCheckers();

            if(Board.piles[i].getColour() == currentPlayer.getChecker() && Board.piles[i].getNoOfCheckers() > 0){
                int pipNumber = isPlayer1Turn ? i+1 : 24-i;
                int pipScore = checkerCount * pipNumber;

                System.out.println("Cone " + (i + 1) + " -> " + pipScore);
            }
        }
    }

    /*
    Function to calculate and display the pip count for each player
     */
    private void pipCount() {
        int noOfPipsWhite = 0;
        int noOfPipsBlack = 0;
        for (int i = 0; i < 24; i++) {
                if (Board.piles[i].getColour() == Checker.Colour.WHITE) {
                    noOfPipsWhite += Board.piles[i].getNoOfCheckers() * (i + 1) + Board.bar[1].getNoOfCheckers() * (24);
                } else {
                    noOfPipsBlack += Board.piles[i].getNoOfCheckers() * (24 - i) + Board.bar[0].getNoOfCheckers() * (24);
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

    private void gameHelp() {
        outputMessage("Enter 'roll' to roll the dice, 'Q' to quit the game, 'pip' to view pip count, 'd'to propose a double!");
        System.out.println();
    }

    private boolean isGameWon(Player currentPlayer) {
        return board.checkWinCondition(currentPlayer);
    }
}

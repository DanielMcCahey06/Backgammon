import java.util.*;

public class Game {
    // Indicates whether to switch turns
    protected boolean switchPlayer;
    // Constants for keyboard positions
    protected static final int WHITEHOMEPOSITION = -1;
    protected static final int BLACKHOMEPOSITION = 24;
    protected static final int WHITEBARPOSITION = 25;
    // Game components
    protected Board board;
    protected static Dice dice;
    // Flags for game state
    protected static boolean isDiceSet = false;
    protected boolean equalDice = true; // Tracks if initial rolls are equal
    public static boolean gameInPlay = true;
    protected boolean isPlayer1Turn = true; // Tracks current player's turn
    // Formatting and game settings
    protected static final int width = 50;
    protected int stake = 1; // Current game stake
    protected DoubleDice doubleDice;
    // Output formatting
    public static final String border = "-".repeat(width);
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
    // Creates and returns a new board
    protected Board createBoard() {
        return new Board();
    }
    // Main game loop
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
                    isPlayer1Turn = !isPlayer1Turn; // Switch turn
                    turncomplete = true;
                }

                if(!gameInPlay){
                    return true;
                }
            }
        }
        return false;
    }

    // Processes a player's action based on their input
    protected boolean processAction(String action, Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        switch (action) {
            case "HINT" -> {
                gameHelp(); // Display help commands
                return false;
            }
            case "DOUBLE" -> {
                // Handle doubling the stakes; ends the game if declined
                gameInPlay = handleDoubleRequest(currentPlayer, otherPlayer, otherPlayerNumber);
                return !gameInPlay;
            }
            case "Q" -> {
                // Quit the game
                gameInPlay = false;
                errorMessage("Game over!");
                return false;
            }
            case "ROLL" -> {
                // Handle dice rolling and moves
                boolean play;
                play = handleRoll(currentPlayer, otherPlayer, otherPlayerNumber);
                return play;
            }
            case "PIP" -> {
                pipCount(); // Display pip count
                return false;
            }
            case "DICE" -> {
                setDice(); // Allow manual dice setting
                return false;
            }
            default -> {
                // Handle invalid input
                errorMessage("Invalid command");
                return false;
            }
        }
    }

    // Handles the dice roll and player actions during their turn
    protected boolean handleRoll(Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        switchPlayer = false; // Initialize without switching players
        int[] roll = generateDiceRoll(); // Generate or retrieve dice values
        outputMessage("Dice roll:" + Dice.diceFace(roll[0]) + " " + Dice.diceFace(roll[1]));

        boolean isDoubles = checkIfDoubles(roll); // Check for doubles
        if (isDoubles) {
            roll = new int[]{roll[0], roll[0], roll[0], roll[0]}; // Use all four dice as doubles
        }
        boolean[] usedDice = generateDiceMarker(isDoubles); // Track dice usage

        while (!switchPlayer) {
            // Generate and display legal moves for the current player
            List<String> legalMoves = generateLegalMoves(currentPlayer, roll, usedDice, isDoubles);

            if (legalMoves.isEmpty()) { // Pass turn if no moves are available
                outputMessage("No legal moves available. Turn passes to other player");
                return true;
            }

            // Process player's selected move
            String[] moveParts = getUserMove(legalMoves);
            int moveDistance = getMoveDistance(moveParts, currentPlayer);

            if (processMove(moveParts, moveDistance, currentPlayer)) {
                markUsedDice(roll, moveDistance, usedDice); // Mark dice as used
            }

            board.notifyObservers(); // Update board display

            if (board.checkWinCondition(currentPlayer)) { // Check for win condition
                int winConditionMultiplier = getWinCondition(currentPlayer, otherPlayer);
                currentPlayer.increaseScore(stake * winConditionMultiplier); // Update score
                gameInPlay = false; // End game if win condition is met
                return false;
            }

            // Determine remaining moves and switch turns if none are left
            int movesAvailable = getRemainingMoves(usedDice);
            outputMessage("Number of Moves Remaining: " + movesAvailable);
            switchPlayer = (movesAvailable == 0);
        }
        return true;
    }


    // Processes a move based on its type (from bar, to home, or standard)
    protected boolean processMove(String[] moveParts, int moveDistance, Player currentPlayer) {
        boolean moveCompleted = false;

        if (moveParts[0].equals("bar")) { // Move from bar to a pile
            int endPilePosition = Integer.parseInt(moveParts[1]);

            if (processMoveFromBar(currentPlayer, endPilePosition)) {
                moveCompleted = true;
            }
        } else if (moveParts[1].equals("off")) { // Move a checker to home
            int startPilePosition = Integer.parseInt(moveParts[0]);

            if (processMoveToHome(currentPlayer, startPilePosition)) {
                moveCompleted = true;
            }
        } else { // Standard move between piles
            int startPilePosition = Integer.parseInt(moveParts[0]);
            int endPilePosition = Integer.parseInt(moveParts[1]);

            if (processMove(currentPlayer, startPilePosition, endPilePosition)) {
                moveCompleted = true;
            }
        }
        return moveCompleted; // Return whether the move was successful
    }


    // Processes a move from the bar to a target pile
    protected boolean processMoveFromBar(Player currentPlayer, int endPilePosition) {
        Pile targetPile = board.getPile(endPilePosition - 1); // Get target pile
        Pile barPile = currentPlayer.getChecker() == Checker.Colour.WHITE
                ? board.getBarPile(Board.PLAYER1BARINDEX) // Get white player's bar pile
                : board.getBarPile(Board.PLAYER2BARINDEX); // Get black player's bar pile
        Checker checkerToMove = barPile.removeTopChecker(); // Remove the top checker from the bar

        // Handle hitting an opponent's single checker
        if (targetPile.getNoOfCheckers() == 1) {
            Checker.Colour checkerOnEndPileColour = targetPile.getColour();
            if (checkerToMove.getColour() != checkerOnEndPileColour) {
                int opponentBarIndex = (checkerOnEndPileColour == Checker.Colour.BLACK ? 0 : 1); // Get opponent's bar index
                Checker checkerOnEndPile = targetPile.removeTopChecker(); // Remove opponent's checker
                board.getBarPile(opponentBarIndex).addChecker(checkerOnEndPile); // Send it to their bar
            }
        }

        targetPile.addChecker(checkerToMove); // Add the moved checker to the target pile
        checkerToMove.setPosition(endPilePosition - 1); // Update checker's position

        System.out.println("Moved " + currentPlayer.getChecker() + " checker from bar to position " + (endPilePosition + 1));
        return true; // Move completed successfully
    }


    // Generates a list of legal moves for the current player based on the dice roll
    protected List<String> generateLegalMoves(Player currentPlayer, int[] roll, boolean[] usedDice, boolean isDoubles) {
        List<String> legalMoves = new ArrayList<>(); // List to store legal moves
        StringBuilder availableDice = new StringBuilder(currentPlayer.getName() + " to play ");

        // Build a string showing the available dice to play
        for (int i = 0; i < roll.length; i++) {
            if (!usedDice[i]) {
                availableDice.append(roll[i]);
                if (i != roll.length - 1) {
                    availableDice.append("-"); // Separate dice with a dash
                }
            }
        }
        availableDice.append(". Select from:");
        System.out.println(availableDice); // Display available dice

        boolean allInHomeRange = allCheckersInHomeRange(currentPlayer); // Check if all checkers are in the home range

        int playerBarIndex = (currentPlayer.getChecker() == Checker.Colour.BLACK) ? 0 : 1; // Determine the player's bar index

        // If no checkers are in the bar
        if (board.getBarPile(playerBarIndex).getCheckers().isEmpty()) {
            // Generate possible moves for checkers not on the bar
            for (Pile pile : board.getPiles()) {
                if (!pile.getCheckers().isEmpty()) { // If pile has checkers
                    Checker topChecker = pile.getCheckers().getLast(); // Get the top checker

                    if (topChecker.getColour() == currentPlayer.getChecker()) { // Check if checker belongs to the current player
                        int position = topChecker.getPosition(); // Get the checker's position

                        // Handle non-double or double moves
                        if (!isDoubles) {
                            if (topChecker.getColour() == Checker.Colour.BLACK) {
                                generateNonDoubleMoves(roll, usedDice, position, legalMoves, currentPlayer, allInHomeRange, 1);
                            } else {
                                generateNonDoubleMoves(roll, usedDice, position, legalMoves, currentPlayer, allInHomeRange, -1);
                            }
                        } else { // Handle doubles
                            if (topChecker.getColour() == Checker.Colour.BLACK) {
                                generateDoubleMoves(roll[0], usedDice, position, legalMoves, currentPlayer, allInHomeRange, 1);
                            } else {
                                generateDoubleMoves(roll[0], usedDice, position, legalMoves, currentPlayer, allInHomeRange, -1);
                            }
                        }
                    }
                }
            }
        } else { // If player has checkers on the bar
            System.out.println(currentPlayer.getName() + " has checkers on the bar.");

            Set<Integer> uniqueEndPiles = new HashSet<>(); // To ensure moves are not duplicated

            // Generate legal moves from the bar
            for (int i = 0; i < usedDice.length; i++) {
                int diceRoll = roll[i];
                if (!usedDice[i]) { // If the dice has not been used
                    int targetPileIndex = (currentPlayer.getChecker() == Checker.Colour.BLACK)
                            ? diceRoll - 1 // Black checker move
                            : 24 - diceRoll; // White checker move

                    if (!uniqueEndPiles.contains(targetPileIndex)) {
                        Pile targetPile = board.getPile(targetPileIndex); // Get the target pile
                        // Check if the target pile is a valid move
                        if (targetPile.getNoOfCheckers() < 2 || targetPile.getColour() == currentPlayer.getChecker()) {
                            legalMoves.add("Move bar-" + (targetPileIndex + 1)); // Add valid move to the list
                            uniqueEndPiles.add(targetPileIndex); // Mark pile as processed
                        }
                    }
                }
            }
        }

        return legalMoves; // Return the list of legal moves
    }


    // Generates legal non-double moves for the current player
    protected void generateNonDoubleMoves(int[] roll, boolean[] usedDice, int position, List<String> legalMoves, Player currentPlayer, boolean allInHomeRange, int direction) {
        Checker.Colour colour = currentPlayer.getChecker(); // Get current player's checker color

        // Single dice moves - check if the dice value can move the checker to a legal target
        if (!usedDice[0] && isLegalTarget(position + direction * roll[0], colour)) {
            legalMoves.add("Move " + (position + 1) + "-" + (position + direction * roll[0] + 1)); // Add legal move to list
        }
        if (!usedDice[1] && isLegalTarget(position + direction * roll[1], colour)) {
            legalMoves.add("Move " + (position + 1) + "-" + (position + direction * roll[1] + 1)); // Add legal move to list
        }

        // If all checkers are in the home range, allow moving them off the board
        if (allInHomeRange) {
            if (!usedDice[0] && position + direction * roll[0] == getHomePosition(direction)) {
                legalMoves.add("Move " + (position + 1) + "-off"); // Move checker off the board
            }
            if (!usedDice[1] && position + direction * roll[1] == getHomePosition(direction)) {
                legalMoves.add("Move " + (position + 1) + "-off"); // Move checker off the board
            }
        }
    }


    // Generates legal double moves for the current player
    protected void generateDoubleMoves(int roll, boolean[] usedDice, int position, List<String> legalMoves, Player currentPlayer, boolean allInHomeRange, int direction) {
        Checker.Colour colour = currentPlayer.getChecker(); // Get current player's checker color

        // Target position is the same for all four moves since we rolled doubles
        int targetPosition = position + direction * roll;
        boolean moveOn = false;

        // Check each dice move for legality
        for (int i = 0; i < usedDice.length; i++) {
            // Add move if it's legal
            if (!usedDice[i] && isLegalTarget(targetPosition, colour)) {
                legalMoves.add("Move " + (position + 1) + "-" + (targetPosition + 1)); // Add legal move to list
                moveOn = true; // Mark move as valid
            }

            // Allow moving off the board if all checkers are in home range
            if (allInHomeRange) {
                if (!usedDice[i] && targetPosition == getHomePosition(direction)) {
                    legalMoves.add("Move " + (position + 1) + "-off"); // Move checker off the board
                    moveOn = true; // Mark move as valid
                }
            }

            if (moveOn) {
                break; // Stop if a valid move has been found
            }
        }
    }


    // Checks if the target position is within bounds and the move is legal for the given color
    protected boolean isLegalTarget(int targetPosition, Checker.Colour colour) {
        return targetPosition >= 0 && targetPosition <= 23 && board.isLegalMove(targetPosition, colour); // Validates position and legality
    }

    // Returns the home position for the current player based on direction (1 for Player 1, -1 for Player 2)
    protected int getHomePosition(int direction) {
        return direction == 1 ? 24 : -1; // Player 1's home is at 24, Player 2's is at -1
    }


    // Function to move a checker between two piles
    // Processes a move from startPos to endPos for the current player
    protected boolean processMove(Player currentPlayer, int startPos, int endPos) {
        Pile startPile = board.getPile(startPos - 1); // Get the starting pile
        Pile endPile = board.getPile(endPos - 1); // Get the target pile
        Checker checkerToMove = startPile.removeTopChecker(); // Remove the top checker from the start pile

        // If the target pile has one checker, check if it's an opponent's checker
        if (endPile.getNoOfCheckers() == 1) {
            Checker.Colour checkerOnEndPileColour = endPile.getColour();
            if (checkerToMove.getColour() != checkerOnEndPileColour) {
                int opponentBarIndex = (checkerOnEndPileColour == Checker.Colour.BLACK ? 0 : 1); // Determine the opponent's bar
                Checker checkerOnEndPile = endPile.removeTopChecker(); // Remove opponent's checker
                board.getBarPile(opponentBarIndex).addChecker(checkerOnEndPile); // Place it in the opponent's bar
            }
        }

        // Move the checker to the target pile and update its position
        endPile.addChecker(checkerToMove);
        checkerToMove.setPosition(endPos - 1);

        return true; // Move completed successfully
    }


    // Processes a move to the player's home pile
    protected boolean processMoveToHome(Player currentPlayer, int startPos) {
        Pile startPile = board.getPile(startPos - 1); // Get the starting pile
        Checker checkerToMove = startPile.removeTopChecker(); // Remove the top checker from the start pile

        int homePosition = currentPlayer.isWhite() ? 0 : 1; // Determine home position (0 for white, 1 for black)

        board.getHomePile(homePosition).addChecker(checkerToMove); // Add checker to home pile
        return true; // Move completed successfully
    }

    // Checks if all checkers of a player are within their home range
    protected boolean allCheckersInHomeRange(Player player) {
        Checker.Colour colour = player.getChecker(); // Get player's checker color

        // Loop through all piles and check each checker
        for (Pile pile : board.getPiles()) {
            for (Checker checker : pile.getCheckers()) {
                if (checker.getColour() == colour) { // Check if the checker belongs to the player
                    int position = checker.getPosition();
                    // If any checker is outside the home range, return false
                    if ((colour == Checker.Colour.BLACK && position < 18) ||
                            (colour == Checker.Colour.WHITE && position > 5)) {
                        return false;
                    }
                }
            }
        }
        return true; // All checkers are within home range
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

    // Marks the dice as used after a move
    protected void markUsedDice(int[] roll, int moveDistance, boolean[] usedDice) {

        // If the user is playing doubles, mark both dice of the roll
        if (roll.length == 4) {
            for (int i = 0; i < roll.length; i++) {
                if (!usedDice[i] && roll[i] == moveDistance) {
                    usedDice[i] = true; // Mark the dice as used
                    return;
                }
            }
        } else {
            // For regular rolls, mark the corresponding dice as used
            if (moveDistance == roll[0] && !usedDice[0]) {
                usedDice[0] = true;
            }
            if (moveDistance == roll[1] && !usedDice[1]) {
                usedDice[1] = true;
            }
        }
    }

    // Calculates the number of remaining moves based on unused dice
    protected int getRemainingMoves(boolean[] usedDice) {
        int remaining = 0;

        // Count how many dice have not been used
        for (boolean used : usedDice) {
            if (!used) {
                remaining++; // Increment if the dice has not been used
            }
        }

        return remaining; // Return the number of remaining moves
    }


    // Displays the pip scores for the current player
    public void displayPipNumbers(Player currentPlayer, boolean isPlayer1Turn) {
        System.out.println("Pip scores for " + currentPlayer.getName() + ":");

        // Loop through all piles to calculate pip scores
        for (int i = 0; i < 24; i++) {
            int checkerCount = board.getPile(i).getNoOfCheckers(); // Get the number of checkers in the pile

            // If the current player has checkers on this pile, calculate the pip score
            if (board.getPile(i).getColour() == currentPlayer.getChecker() && checkerCount > 0) {
                int pipNumber = isPlayer1Turn ? i + 1 : 24 - i; // Determine pip number based on player’s turn
                int pipScore = checkerCount * pipNumber; // Calculate pip score

                // Output the pip score for this pile
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

    // Calculates and displays the total pip count for both players
    protected void pipCount() {
        int noOfPipsWhite = 0; // Tracks white player's pip count
        int noOfPipsBlack = 0; // Tracks black player's pip count

        // Loop through all piles to calculate the pip count for each player
        for (int i = 0; i < 24; i++) {
            if (board.getPile(i).getColour() == Checker.Colour.WHITE) {
                // Calculate pip score for white player
                noOfPipsWhite += board.getPile(i).getNoOfCheckers() * (i + 1) + board.getBarPile(1).getNoOfCheckers() * (24);
            } else {
                // Calculate pip score for black player
                noOfPipsBlack += board.getPile(i).getNoOfCheckers() * (24 - i) + board.getBarPile(0).getNoOfCheckers() * (24);
            }
        }

        // Display the pip count for both players
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

    // Displays a list of available game commands to the player
    protected void gameHelp() {
        outputMessage("Enter 'roll' to roll the dice, 'Q' to quit the game, 'pip' to view pip count, 'double' to propose a double!"); // Display commands
        System.out.println(); // Add a blank line for better formatting
    }


    // Determines which player goes first by rolling the dice
    private void firstToPlay(Player player1, Player player2) {
        while (equalDice) {
            int[] startingRoll = dice.roll(); // Roll the dice for both players
            int player1Roll = startingRoll[0];
            int player2Roll = startingRoll[1];
            outputMessage(player1.getName() + " rolls a " + Dice.diceFace(player1Roll) + " and " + player2.getName() + " rolls a " + Dice.diceFace(player2Roll));

            // Compare rolls to determine who goes first
            if (player1Roll > player2Roll) {
                outputMessage(player1.getName() + " will go first!");
                equalDice = false; // End the loop if player 1 wins
            } else if (player2Roll > player1Roll) {
                outputMessage(player2.getName() + " will go first!");
                isPlayer1Turn = false; // End the loop if player 2 wins
                equalDice = false;
            } else {
                outputMessage("Dice rolls are equal so will go again!"); // Re-roll if the dice are equal
            }
        }
    }

    // Handles the process when a player proposes a double and the opponent accepts or declines
    protected boolean handleDoubleRequest(Player currentPlayer, Player otherPlayer, int otherPlayerNumber) {
        boolean playOn = true;
        Scanner scanner = new Scanner(System.in);

        // Check if the current player has the double dice
        if ((isPlayer1Turn && (DoubleDice.getOwner() == 2)) || (!isPlayer1Turn && (DoubleDice.getOwner() == 1))) {
            outputMessage(currentPlayer.getName() + " you are not currently in possession of the Double Dice so you cannot propose a double");
        } else {
            outputMessage(currentPlayer.getName() + " has proposed a double. " + otherPlayer.getName() + ", to accept and double the stakes of this game input 'A'. To Decline and forfeit this game input 'D'");

            // Get and validate the opponent's reaction
            String reaction = scanner.nextLine().toUpperCase();
            while (!reaction.equals("A") && !reaction.equals("D")) {
                errorMessage("Invalid input. Input must be A or D");
                reaction = scanner.nextLine().toUpperCase();
            }

            // Handle the case where the opponent accepts or declines the double
            if (reaction.equals("A")) {
                handleAcceptedDouble(currentPlayer, otherPlayer, otherPlayerNumber); // Process accepted double
            } else {
                outputMessage("Congratulations " + currentPlayer.getName() + " you win!");
                currentPlayer.increaseScore(stake); // Player wins if double is declined
                playOn = false;
            }
        }
        return playOn; // Return whether the game continues
    }


    protected void handleAcceptedDouble(Player currentPlayer, Player otherPlayer, int otherPlayerNumber){
        doubleDice.increaseDouble(); //multiplier is doubled
        stake = DoubleDice.getDouble(); //stake equals new double amount
        doubleDice.setOwner(otherPlayerNumber); //set owner of double dice to opposition player
        outputMessage("The stakes have been doubled. This game is now worth " + DoubleDice.getDouble() + " points. " + otherPlayer.getName() + " is now the holder of the doubling dice.");
        System.out.println("Enter your  next move " + currentPlayer.getName() + ", or 'hint' for a list of possible commands: ");
    }

    // Checks if the dice roll results in doubles
    protected boolean checkIfDoubles(int[] roll) {
        boolean isDoubles;

        // If both dice have the same value, it's a double
        if (roll[0] == roll[1]) {
            System.out.println("You rolled doubles!"); // Inform the player they rolled doubles
            isDoubles = true;
        } else {
            isDoubles = false; // Otherwise, it's not a double
        }

        return isDoubles; // Return whether it's a double
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

    // Allows the user to manually set the dice values
    protected void setDice() {
        Scanner scanner = new Scanner(System.in); // Scanner for user input
        int[] diceValues = new int[2]; // Array to store dice values
        boolean validInput = false; // Flag to track if valid input is entered

        // Prompt the user until valid input is provided
        while (!validInput) {
            System.out.println("Enter the two dice values in the form: <int> <int>, ensuring they are separated by a space:");
            String values = scanner.nextLine(); // Read input
            String[] parts = values.split(" "); // Split input into parts

            // Validate the input and set the dice if valid
            if (isValidDicePair(parts)) {
                diceValues[0] = Integer.parseInt(parts[0]); // Set the first dice value
                diceValues[1] = Integer.parseInt(parts[1]); // Set the second dice value
                dice.setDice(diceValues[0], diceValues[1]); // Apply the dice values
                isDiceSet = true; // Mark dice as set
                validInput = true; // Exit the loop
                System.out.println("Dice values now set for the next dice roll.");
                System.out.println("Enter your next play or 'hint' for a list of possible commands: ");
            } else {
                errorMessage("Invalid input. Enter two valid integer values!"); // Error message for invalid input
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

    // Generates a marker array to track which dice have been used
    protected boolean[] generateDiceMarker(boolean isDoubles) {
        boolean[] usedDice;

        // If it's a double, initialize an array for four dice
        if (isDoubles) {
            usedDice = new boolean[]{false, false, false, false};
        } else {
            // Otherwise, initialize for two dice
            usedDice = new boolean[]{false, false};
        }

        return usedDice; // Return the marker array
    }

}
class StubBoard extends Board {
    private final StubPile[] piles = new StubPile[24]; // Array of piles for the game board
    private final StubPile[] barPiles = new StubPile[2]; // Array for bar piles (2 for black and white checkers)
    private final StubPile[] homePiles = new StubPile[2]; // Array for home piles (1 for each player)

    // Constructor initializes stub piles for testing
    public StubBoard() {
        // Initialize 24 regular piles
        for (int i = 0; i < 24; i++) {
            piles[i] = new StubPile();
        }
        // Initialize 2 bar piles and 2 home piles
        for (int i = 0; i < 2; i++) {
            barPiles[i] = new StubPile();
            homePiles[i] = new StubPile();
        }
    }

    // Overrides getPile method to return the correct pile or home pile
    @Override
    public Pile getPile(int index) {
        // Return the appropriate home pile for the specified home positions
        if (index == Game.WHITEHOMEPOSITION) {
            return getHomePile(PLAYER1HOMEINDEX);
        } else if (index == Game.BLACKHOMEPOSITION) {
            return getHomePile(PLAYER2HOMEINDEX);
        } else if (index < 0 || index >= piles.length) {
            throw new IllegalArgumentException("Invalid pile index: " + index); // Handle invalid index
        }
        return piles[index]; // Return the pile for valid indices
    }

    // Returns the home pile for a given index
    @Override
    public Pile getHomePile(int index) {
        if (index < 0 || index >= homePiles.length) {
            throw new IllegalArgumentException("Invalid home pile index: " + index); // Handle invalid index
        }
        return homePiles[index]; // Return the specified home pile
    }

    // Returns the bar pile for a given index
    @Override
    public Pile getBarPile(int index) {
        if (index < 0 || index >= barPiles.length) {
            throw new IllegalArgumentException("Invalid bar pile index: " + index); // Handle invalid index
        }
        return barPiles[index]; // Return the specified bar pile
    }

    // Getter for a specific stub pile by index
    public StubPile getStubPile(int index) {
        return piles[index]; // Return the specific stub pile
    }

    // Getter for a specific stub bar pile by index
    public StubPile getStubBarPile(int index) {
        return barPiles[index]; // Return the specified stub bar pile
    }

    // Getter for a specific stub home pile by index
    public StubPile getStubHomePile(int index) {
        return homePiles[index]; // Return the specified stub home pile
    }
}




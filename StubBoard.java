class StubBoard extends Board {
    private final StubPile[] piles = new StubPile[24];
    private final StubPile[] barPiles = new StubPile[2];
    private final StubPile[] homePiles = new StubPile[2];

    public StubBoard() {
        // Initialize stub piles for testing
        for (int i = 0; i < 24; i++) {
            piles[i] = new StubPile();
        }
        for (int i = 0; i < 2; i++) {
            barPiles[i] = new StubPile();
            homePiles[i] = new StubPile();
        }
    }

    @Override
    public Pile getPile(int index) {
        if (index == Game.WHITEHOMEPOSITION) {
            return getHomePile(PLAYER1HOMEINDEX);
        } else if (index == Game.BLACKHOMEPOSITION) {
            return getHomePile(PLAYER2HOMEINDEX);
        } else if (index < 0 || index >= piles.length) {
            throw new IllegalArgumentException("Invalid pile index: " + index);
        }
        return piles[index];
    }

    @Override
    public Pile getHomePile(int index) {
        if (index < 0 || index >= homePiles.length) {
            throw new IllegalArgumentException("Invalid home pile index: " + index);
        }
        return homePiles[index];
    }

    @Override
    public Pile getBarPile(int index) {
        if (index < 0 || index >= barPiles.length) {
            throw new IllegalArgumentException("Invalid bar pile index: " + index);
        }
        return barPiles[index];
    }

    public StubPile getStubPile(int index) {
        return piles[index];
    }

    public StubPile getStubBarPile(int index) {
        return barPiles[index];
    }

    public StubPile getStubHomePile(int index) {
        return homePiles[index];
    }
}




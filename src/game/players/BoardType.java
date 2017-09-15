package game.players;

public enum BoardType {
    EMPTY("EMPTY"),
    MINE("MINE"),
    SHIP("SHIP"),
    SHIP_HIT("SHIP_HIT"), MINE_HIT("MINE_HIT"), MISS("MISS");

    private final String text;

    private BoardType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
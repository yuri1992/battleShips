package game.model.boards;

public enum BoardType {
    EMPTY("EMPTY"),
    MINE("MINE"),
    SHIP("SHIP"),
    SHIP_HIT("SHIP_HIT"),
    MINE_HIT("MINE_HIT"),
    MISS("MISS");

    private final String text;

    BoardType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
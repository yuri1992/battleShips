package game.engine;

public enum TurnType {
    HIT("HIT"),
    MISS("MISS"),
    HIT_MINE("HIT_MINE"),
    NOT_EMPTY("NOT_EMPTY");

    private final String text;

    private TurnType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
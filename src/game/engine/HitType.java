package game.engine;

public enum HitType {
    HIT("HIT"),
    MISS("MISS"),
    PLACE_MINE("PLACE MINE"),
    HIT_MINE("HIT MINE"),
    NOT_EMPTY("NOT EMPTY");

    private final String text;

    private HitType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
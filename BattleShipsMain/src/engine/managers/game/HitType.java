package engine.managers.game;

public enum HitType {
    HIT("HIT"),
    MISS("MISS"),
    PLACE_MINE("PLACE MINE"),
    HIT_MINE("HIT MINE"),
    NOT_EMPTY("NOT EMPTY");

    private final String text;

    HitType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
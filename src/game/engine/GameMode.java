package game.engine;

public enum GameMode {
    BASIC("BASIC"),
    ADVANCE("ADVANCE");

    private final String text;

    private GameMode(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
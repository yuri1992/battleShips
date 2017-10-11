package engine.managers.game;

public enum GameMode {
    BASIC("BASIC"),
    ADVANCE("ADVANCE");

    private final String text;

    GameMode(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
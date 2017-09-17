package game.engine;

/**
 * Created by amirshavit on 9/16/17.
 */
public enum GameState {
    LOADED("LOADED"),
    IN_PROGRESS("IN PROGRESS"),
    REPLAY("REPLAY");

    private final String text;

    GameState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

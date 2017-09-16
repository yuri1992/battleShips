package runners.console;

public enum ConsoleMenu {
    LOAD_GAME("Load Game Settings (from Xml)"),
    START_GAME("Start Game"),
    SHOW_GAME_STATUS("Show Game Status"),
    PLAY_TURN("Play Turn"),
    SHOW_STATISTICS("Show Statistics"),
    RESIGN_GAME("Resign Game"),
    END_GAME("End Game");


    private final String text;

    ConsoleMenu(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

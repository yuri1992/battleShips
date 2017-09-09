package game.exceptions;

public class BoardSizeIsTooBig extends GameSettingsInitializationException {
    public BoardSizeIsTooBig(String s) {
        super(s);
    }
}

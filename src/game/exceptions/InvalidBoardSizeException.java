package game.exceptions;

public class InvalidBoardSizeException extends GameSettingsInitializationException {
    public InvalidBoardSizeException(String s) {
        super(s);
    }
}

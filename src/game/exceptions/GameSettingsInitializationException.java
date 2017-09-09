package game.exceptions;

public class GameSettingsInitializationException extends Exception {
    String msg = "";

    public GameSettingsInitializationException(String message) {
        super(message);
        this.msg = message;
    }

    @Override
    public String toString() {
        return "Error Creating Game: " + this.msg;
    }

}

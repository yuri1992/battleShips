package engine.exceptions;

public class InvalidFileFormatException extends GameSettingsInitializationException {
    public InvalidFileFormatException() {
        super("Xml Provided is not valid xml format");
    }
}

package game.exceptions;

public class FileNotXmlFormat extends GameSettingsInitializationException {
    public FileNotXmlFormat() {
        super("Xml Provided is not valid xml format");
    }
}

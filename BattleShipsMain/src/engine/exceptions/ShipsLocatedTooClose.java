package engine.exceptions;

public class ShipsLocatedTooClose extends BoardBuilderException {
    public ShipsLocatedTooClose(String errorMessage) {
        super(errorMessage);
    }
}

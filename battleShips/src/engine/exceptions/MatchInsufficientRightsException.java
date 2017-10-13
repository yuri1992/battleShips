package engine.exceptions;

public class MatchInsufficientRightsException extends Exception {

    public MatchInsufficientRightsException() {
        super("User tried to perform Owner actions");
    }

}

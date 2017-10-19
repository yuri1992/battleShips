package engine.exceptions;

public class MatchInsufficientRightsException extends MatchException {

    public MatchInsufficientRightsException() {
        super("User tried to perform Owner actions");
    }

}

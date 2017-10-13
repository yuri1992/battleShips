package engine.exceptions;

public class MatchNotFoundException extends MatchException {

    public MatchNotFoundException(int id) {
        super("Match id " + id + " not found");
    }

    public MatchNotFoundException(String matchName) {
        super("Match '" + matchName + "' not found");
    }

}

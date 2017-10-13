package engine.exceptions;

/**
 * Created by amirshavit on 10/11/17.
 */
public class MatchException extends Exception {

    String msg = "";

    public MatchException(String message) {
        super(message);
        this.msg = message;
    }

    @Override
    public String toString() {
        return "Match Error: " + this.msg;
    }

}

package engine.exceptions;

/**
 * Created by amirshavit on 10/11/17.
 */
public class UserException extends Exception {

    String msg = "";

    public UserException(String message) {
        super(message);
        this.msg = message;
    }

    @Override
    public String toString() {
        return "User Error: " + this.msg;
    }

}

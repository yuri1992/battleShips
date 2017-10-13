package engine.exceptions;

public class UserNameTakenException extends UserException {

    public UserNameTakenException(String name) {
        super("Username " + name + " is already in use");
    }

}

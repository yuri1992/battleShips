package engine.exceptions;

import engine.model.multi.User;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(String name) {
        super("User " + name + " is not logged in");
    }

    public UserNotFoundException(int id) {
        super("User id " + id + " is not logged in");
    }

}

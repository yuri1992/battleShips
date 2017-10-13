package engine.exceptions;

import engine.model.multi.User;

public class UserNotInMatchException extends MatchException {

    public UserNotInMatchException(User user) {
        super("User " + user.getName() + " is not registered to this game");
    }

}

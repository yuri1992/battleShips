package engine.exceptions;

public class UserNotFoundException extends Exception {
    String userName = "";
    int userId = 0;

    public UserNotFoundException(String name) {
        super(msgBuilder(name));
        this.userName = name;
    }

    public UserNotFoundException(int id) {
        super(msgBuilder(id));
        this.userId = id;
    }

    @Override
    public String toString() {
        return "User not found: " + super.getMessage();
    }

    private static String msgBuilder(String name) {
        return "User " + name + " is not logged in";
    }

    private static String msgBuilder(int id) {
        return "User id " + id + " is not logged in";
    }

}

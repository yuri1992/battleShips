package engine.exceptions;

public class UserNameTakenException extends Exception {
    String msg = "";

    public UserNameTakenException(String name) {
        super(msgBuilder(name));
        this.msg = msgBuilder(name);
    }

    @Override
    public String toString() {
        return "Error logging in : " + this.msg;
    }

    private static String msgBuilder(String name) {
        return "Username " + name + " is already in use";
    }

}

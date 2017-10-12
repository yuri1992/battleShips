package engine.exceptions;

public class MatchNameTakenException extends Exception {

    public MatchNameTakenException(String name) {
        super("Match named " + name + " submitted already");
    }

}

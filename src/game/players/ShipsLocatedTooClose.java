package game.players;

public class ShipsLocatedTooClose extends BoardBuilderException {
    ShipsLocatedTooClose(String errorMessage) {
        super(errorMessage);
    }
}

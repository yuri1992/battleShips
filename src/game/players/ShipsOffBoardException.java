package game.players;

/**
 * Created by AmirShavit on 09/09/2017.
 */
public class ShipsOffBoardException extends BoardBuilderException {
    ShipsOffBoardException(String errorMessage) {
        super(errorMessage);
    }
}

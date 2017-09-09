package game.exceptions;

/**
 * Created by AmirShavit on 09/09/2017.
 */
public class ShipsOffBoardException extends BoardBuilderException {
    public ShipsOffBoardException(String errorMessage) {
        super(errorMessage);
    }
}

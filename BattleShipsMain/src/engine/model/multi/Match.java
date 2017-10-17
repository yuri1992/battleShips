package engine.model.multi;

import engine.exceptions.UserNotInMatchException;
import engine.managers.game.GameManager;
import engine.managers.game.GameMode;
import engine.managers.game.GameState;

/**
 * Created by amirshavit on 10/11/17.
 */
public class Match {

    private final int matchId;
    private final String matchName;
    private final User submittingUser;
    private final GameManager gameManager;
    private User player1 = null;
    private User player2 = null;

    public Match(int matchId, String matchName, User submittingUser, GameManager gameManager) {
        this.matchId = matchId;
        this.matchName = matchName;
        this.submittingUser = submittingUser;
        this.gameManager = gameManager;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getMatchName() {
        return matchName;
    }

    public User getSubmittingUser() {
        return submittingUser;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public int getBoardSize() {
        return gameManager.getBoardSize();
    }

    public GameMode getGameType() {
        return gameManager.getMode();
    }

    public boolean isMatchRegisteredTo() {
        return (player1 != null);
    }

    public boolean isMatchAvailableToJoin() {
        return (player2 == null);
    }

    public boolean addUserToMatch(User user) {
        if (isMatchAvailableToJoin()) {
            synchronized (this) { // Lock and double check
                if (isMatchAvailableToJoin()) {
                    if (player1 == null)
                        player1 = user;
                    else {
                        player2 = user;
                        gameManager.startGame();
                    }
                    return true;
                }
                else return false;
            }
        } else return false;
    }

    /// TODO: Amir: verify game not started instead of able to join
    public boolean removeUserFromMatch(User user) throws UserNotInMatchException {
        if (isMatchAvailableToJoin()) {
            synchronized (this) { // Lock and double check
                if (isMatchAvailableToJoin()) {
                    if (player1 == user) {
                        player1 = null;
                        return true;
                    }
                    else throw new UserNotInMatchException(user);
                }
                else return false;
            }
        } else return false;
    }

    public boolean isUserRegistered(User user) {
        return (user != null &&
                (user.equals(player1) || user.equals(player2)));
    }
}

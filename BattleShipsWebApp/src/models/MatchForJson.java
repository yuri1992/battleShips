package models;

import engine.managers.game.GameMode;
import engine.model.multi.Match;
import engine.model.multi.User;

/**
 * Created by amirshavit on 10/17/17.
 */
public class MatchForJson {

    private final int matchId;
    private final String matchName;
    private final UserForJson submittingUser;
    private final UserForJson player1;
    private final UserForJson player2;
    private final GameMode gameMode;
    private final int boardSize;

    public MatchForJson(Match match) {

        this.matchId = match.getMatchId();
        this.matchName = match.getMatchName();
        this.submittingUser = new UserForJson(match.getSubmittingUser());
        this.player1 = (match.getPlayer1() != null ? new UserForJson(match.getPlayer1()) : null);
        this.player2 = (match.getPlayer2() != null ? new UserForJson(match.getPlayer2()) : null);
        this.gameMode = match.getGameType();
        this.boardSize = match.getBoardSize();
    }

}

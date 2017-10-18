package models;

import engine.managers.game.GameStatistics;
import engine.managers.game.GameTurn;
import engine.model.boards.Player;
import engine.model.multi.Match;
import engine.model.multi.User;

import java.util.List;

/**
 * Created by amirshavit on 10/18/17.
 */
public class GameStatisticsObj {

    private final GameStatistics gameStatistics;
    private List<GameTurn> turnList;
    private User winner;

    public GameStatisticsObj(Match match) {
        this.gameStatistics = match.getGameManager().getGameStatistics();
        this.turnList = match.getGameManager().getTurnList();

        Player winner = match.getGameManager().getWinner();
        if (winner != null) {
            if (match.getGameManager().getPlayerList().indexOf(winner) == 0)
                this.winner = match.getPlayer1();
            else
                this.winner = match.getPlayer2();
        } else {
            this.winner = null;
        }
    }
}

package models;

import engine.managers.game.GameStatistics;
import engine.model.multi.Match;

/**
 * Created by amirshavit on 10/18/17.
 */
public class GameStatisticsObj {

    private final GameStatistics gameStatistics;

    public GameStatisticsObj(Match match) {
        this.gameStatistics = match.getGameManager().getGameStatistics();
    }
}

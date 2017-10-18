package models;

import engine.managers.game.GameState;
import engine.managers.game.GameTurn;
import engine.model.boards.AttackBoard;
import engine.model.boards.Player;
import engine.model.boards.PlayerStatistics;
import engine.model.boards.ShipsBoard;
import engine.model.multi.Match;
import engine.model.multi.User;

import java.util.List;

/**
 * Created by amirshavit on 10/17/17.
 */
public class GameStatusObj {

    private final int matchId;
    private final String matchName;
    private final GameState state;
    private final GameStatus gameStatus;

    private final UserForJson player;
    private final ShipsBoard shipsBoard;
    private final AttackBoard attackBoard;
    private final int score;
    private final PlayerStatistics statistics;
    private final List<GameTurn> turns;

    public GameStatusObj(Match match, User sessionUser) {

        this.matchId = match.getMatchId();
        this.matchName = match.getMatchName();
        this.state = match.getGameManager().getState();

        this.player = new UserForJson(sessionUser);
        int playerIndex = match.getPlayer1().equals(sessionUser) ? 0 : 1;

        List<Player> playerList = match.getGameManager().getPlayerList();
        Player player = playerList.get(playerIndex);

        this.shipsBoard = player.getShipsBoard();
        this.attackBoard = player.getAttackBoard();
        this.score = player.getScore();

        this.turns = match.getGameManager().getPlayerMoves(player);
        this.statistics = match.getGameManager().getPlayerStatistics(player, null);

        if (state == GameState.IN_PROGRESS) {
            if (player == match.getGameManager().getCurrentPlayer())
                this.gameStatus = GameStatus.PLAYER_TURN;
            else
                this.gameStatus = GameStatus.OPONENT_TURN;
        } else if (state == GameState.REPLAY) {
            this.gameStatus = GameStatus.ENDED;
        } else if (match.getPlayer2() == null) {
            this.gameStatus = GameStatus.WAITING_FOR_SECOND_PLAYER_TO_JOIN;
        } else {
            this.gameStatus = GameStatus.UNDEFINED;
        }
    }

}

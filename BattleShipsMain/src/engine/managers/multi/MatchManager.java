package engine.managers.multi;

import engine.exceptions.*;
import engine.managers.game.GameManager;
import engine.managers.game.GameManagerFactory;
import engine.model.multi.Match;
import engine.model.multi.User;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by amirshavit on 10/11/17.
 */
public class MatchManager {

    private static MatchManager instance;
    private final Set<Match> matchSet;
    private static int matchCounter = 0;

    private MatchManager() {
        this.matchSet = new HashSet<>();
    }

    public static MatchManager sharedInstance() {
        if (instance == null)
            instance = new MatchManager();
        return instance;
    }

    public Set<Match> getMatchList() {
        return Collections.unmodifiableSet(matchSet);
    }

    public Match addMatch(String matchName, User submittedBy, InputStream xml) throws MatchNameTakenException, FileNotFoundException, JAXBException, GameSettingsInitializationException {
        if (isMatchNameTaken(matchName))
            throw new MatchNameTakenException(matchName);

        GameManager gm = GameManagerFactory.loadGameManager(xml);
        Match out = new Match(matchCounter, matchName, submittedBy, gm);
        matchSet.add(out);
        matchCounter++;
        return out;
    }

    public void removeMatch(int matchId, User removedBy) throws MatchException {
        Match m = getMatchById(matchId);
        if (m.getPlayer1() != null) {
            throw new MatchException("Cannot remove a game when users are playing");
        } else if (m.getSubmittingUser().getId() != removedBy.getId()) {
            throw new MatchInsufficientRightsException();
        }
        matchSet.remove(m);
    }

    public Match getMatchById(int matchId) throws MatchNotFoundException {
        List<Match> list = getMatchList().stream().filter(m -> m.getMatchId() == matchId).collect(Collectors.toList());
        if (list.size() == 0)
            throw new MatchNotFoundException(matchId);
        return list.get(0);
    }

    private boolean isMatchNameTaken(String matchName) {
        List<Match> list = getMatchList().stream().filter(m -> m.getMatchName().equals(matchName)).collect(Collectors
                .toList());
        return list.size() > 0;
    }

    public boolean registerUserToMatch(int matchId, User user) throws MatchNotFoundException {
        Match match = getMatchById(matchId);
        return match.addUserToMatch(user);
    }

    public boolean removeUserFromMatch(int matchId, User user) throws MatchNotFoundException, UserNotInMatchException {
        Match match = getMatchById(matchId);
        return match.removeUserFromMatch(user);
    }

}

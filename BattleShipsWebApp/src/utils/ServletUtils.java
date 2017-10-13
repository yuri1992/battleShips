package utils;

import engine.managers.multi.MatchManager;
import engine.managers.multi.UsersManager;

/**
 * Created by amirshavit on 10/13/17.
 */
public class ServletUtils {

    public static UsersManager getUserManager() {
        return UsersManager.sharedInstance();
    }

    public static MatchManager getMatchManager() {
        return MatchManager.sharedInstance();
    }

}

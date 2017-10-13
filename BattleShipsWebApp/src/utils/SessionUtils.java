package utils;

import constants.Constants;
import engine.exceptions.UserNotFoundException;
import engine.model.multi.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static constants.Constants.PARAMETER_ERROR_INT;

/**
 * Created by amirshavit on 10/13/17.
 */
public class SessionUtils {

    public static User getSessionUser (HttpServletRequest request) {
        int userId = getSessionIntParameter(request.getSession(false), Constants.SESSION_USER_ID);
        if (userId != PARAMETER_ERROR_INT) {
            try {
                return ServletUtils.getUserManager().getUser(userId);
            } catch (UserNotFoundException e) {

            }
        }
        return null;
    }

    public static void setSessionUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute(Constants.SESSION_USER_ID, user.getId());
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

    private static int getSessionIntParameter(HttpSession session , String name) {
        if (session != null) {
            Object value = session.getAttribute(name);
            if (value != null) {
                try {
                    return Integer.parseInt(value.toString());
                } catch (NumberFormatException numberFormatException) {
                }
            }
        }
        return PARAMETER_ERROR_INT;
    }
}

package servlets;

import engine.managers.game.GameState;
import engine.model.multi.Match;
import engine.model.multi.User;
import utils.SessionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/13/17.
 */
public class MatchHubServlet extends BaseServlet {

    private static final String PAGE_SIGNUP_JSP = "/pages/signup";
    private static final String PAGE_GAME_JSP = "/pages/game";
    private static final String MATCH_HUB_JSP = "matchhub.jsp";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtils.getSessionUser(req);
        if (user == null) { // User already logged id in
            resp.sendRedirect(req.getContextPath() + PAGE_SIGNUP_JSP);
            return;
        }

        Match match = SessionUtils.getSessionMatch(req);
        if (match != null && match.getGameManager().getState() != GameState.REPLAY) {
            resp.sendRedirect(req.getContextPath() + PAGE_GAME_JSP);
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(MATCH_HUB_JSP);
        rd.forward(req, resp);
    }
}


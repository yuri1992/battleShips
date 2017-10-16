package servlets;

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
public class LoginServlet extends BaseServlet {

    public static final String SIGNUP_JSP = "signup.jsp";
    private final String GAME_HUB_URL = "../pages/game";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtils.getSessionUser(req);
        if (user != null) { // User already logged id in
            resp.sendRedirect(GAME_HUB_URL);
            return;
        }
        // Fowarding to signup page
        RequestDispatcher rd = req.getRequestDispatcher(SIGNUP_JSP);
        rd.forward(req, resp);
    }

    @Override
    public String getServletInfo() {
        return this.getClass().toString();
    }
}


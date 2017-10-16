package servlets;

import engine.model.multi.User;
import utils.SessionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/13/17.
 */
public class GameServlet extends HttpServlet {


    public static final String PAGE_SIGNUP_JSP = "/pages/signup";
    public static final String GAME_JSP = "game.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = SessionUtils.getSessionUser(req);
        if (user == null) { // User already logged id in
            resp.sendRedirect(PAGE_SIGNUP_JSP);
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(GAME_JSP);
        rd.forward(req, resp);
    }

    @Override
    public String getServletInfo() {
        return this.getClass().toString();
    }

}


package servlets;

import engine.exceptions.UserNameTakenException;
import engine.model.multi.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/13/17.
 */
public class LoginServlet extends HttpServlet {

    public static final String USERNAME = "username";

    private final String GAME_HUB_URL = "../pages/gamehub.html";
    private final String SIGN_IN_URL = "../pages/signin.html";
//    private final String LOGIN_ERROR_URL = "/pages/loginerror/login_attempt_after_error.jsp";  // must start with '/' since will be used in request dispatcher...

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User user = SessionUtils.getSessionUser(req);
        if (user != null) { // User already loggid in
            resp.sendRedirect(GAME_HUB_URL);
            return;
        }

        String userName = validateUserName(req.getParameter(USERNAME));
        if (userName != null) {
            try {
                user = ServletUtils.getUserManager().addUser(userName);
                SessionUtils.setSessionUser(req, user);
                resp.sendRedirect(GAME_HUB_URL);
            } catch (UserNameTakenException e) {
                /// TODO: Amir: Handle name taken
            }
        } else {
            /// TODO: Amir: Handle empty name
        }

        resp.sendRedirect(SIGN_IN_URL);
    }

    //<editor-fold defaultstate="collapsed" desc="HttpServlet Overrides"

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    public String getServletInfo() {
        return this.getClass().toString();
    }

    //</editor-fold>

    private String validateUserName(String un) {
        /// TODO: Amir: Consider name/char validation
        if (un != null && !un.isEmpty())
            return un.trim();
        else return null;
    }
}


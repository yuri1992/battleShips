package servlets;

import engine.exceptions.UserNotFoundException;
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
public class LogoutServlet extends HttpServlet {

    private final String SIGN_IN_URL = "../pages/signin.html";

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = SessionUtils.getSessionUser(req);
        if (user != null) {
            try {
                SessionUtils.clearSession(req);
                ServletUtils.getUserManager().removeUser(user.getId());
            } catch (UserNotFoundException e) { }
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
}

package servlets;

import engine.exceptions.UserNotFoundException;
import engine.model.multi.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/13/17.
 */
public class LogoutServlet extends BaseServlet {

    private final String SIGN_IN_URL = "../pages/signin.jsp";

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SessionUtils.getSessionUser(request);
        if (user != null) {
            try {
                SessionUtils.clearSession(request);
                ServletUtils.getUserManager().removeUser(user.getId());
            } catch (UserNotFoundException e) { }
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private void processRequestRedirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = SessionUtils.getSessionUser(req);
        if (user != null) {
            try {
                SessionUtils.clearSession(req);
                ServletUtils.getUserManager().removeUser(user.getId());
            } catch (UserNotFoundException e) { }
        }

        resp.sendRedirect(SIGN_IN_URL);
    }

}

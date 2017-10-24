package servlets;

import engine.exceptions.UserNotFoundException;
import engine.model.multi.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/13/17.
 */
public class LogoutServlet extends BaseServlet {
    private static final String PAGE_SIGNUP_JSP = "/pages/signup";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = SessionUtils.getSessionUser(request);
        if (user != null) {
            try {
                SessionUtils.clearSession(request);
                ServletUtils.getUserManager().removeUser(user.getId());
            } catch (UserNotFoundException e) {
            }
        }

        response.sendRedirect(request.getContextPath() + PAGE_SIGNUP_JSP);
    }

}

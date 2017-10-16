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
public class APILogoutServlet extends JsonServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);

        User user = SessionUtils.getSessionUser(request);
        if (user != null) {
            try {
                SessionUtils.clearSession(request);
                ServletUtils.getUserManager().removeUser(user.getId());
            } catch (UserNotFoundException e) {
            }
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}

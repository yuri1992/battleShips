package servlets;

import com.google.gson.Gson;
import engine.model.multi.User;
import utils.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by amirshavit on 10/15/17.
 */
public class UserServlet extends BaseServlet {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (!isSessionValid(request, response)) return;

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new Gson().toJson(new UsersResponse(ServletUtils.getUserManager().getUsers())));
        response.getWriter().flush();
    }


    private class UsersResponse {

        final private Set<User> users;
        final private int size;

        public UsersResponse(Set<User> users) {
            this.users = users;
            this.size = users.size();
        }
    }

}

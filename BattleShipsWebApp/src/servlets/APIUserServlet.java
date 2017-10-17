package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import engine.exceptions.UserNameTakenException;
import engine.model.multi.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Created by amirshavit on 10/15/17.
 */
public class APIUserServlet extends JsonServlet {
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    /// TODO: Amir: Add GET /me instead of session servlet

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isSessionValid(request, response)) return;

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new Gson().toJson(new UsersResponse(ServletUtils.getUserManager().getUsers())));
        response.getWriter().flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);

        PrintWriter out = response.getWriter();

        User user = SessionUtils.getSessionUser(request);
        if (user != null) { // User already logged id in
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(new Gson().toJson(user));
        } else {

            String userName = validateUserName(request.getParameter(USERNAME));
            if (userName != null) {
                try {
                    user = ServletUtils.getUserManager().addUser(userName, request.getParameter(EMAIL), request.getParameter(PASSWORD));
                    SessionUtils.setSessionUser(request, user);
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.println(new Gson().toJson(user));
                } catch (UserNameTakenException e) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    JsonObject obj = new JsonObject();
                    obj.addProperty("desc", "Requested user name not available");
                    out.println(obj);
                }
            } else {
                // Handle empty name
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject obj = new JsonObject();
                obj.addProperty("desc", "Missing username parameter");
                out.println(obj);
            }
        }
        out.flush();
    }

    // <editor-fold defaultstate="collapsed" desc="User Response Object">
    private class UsersResponse {

        final private Set<User> users;
        final private int size;

        public UsersResponse(Set<User> users) {
            this.users = users;
            this.size = users.size();
        }
    }
    // </editor-fold>

    private String validateUserName(String un) {
        /// TODO: Amir: Consider name/char validation
        if (un != null && !un.isEmpty())
            return un.trim();
        else return null;
    }
}

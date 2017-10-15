package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import engine.exceptions.UserNameTakenException;
import engine.model.multi.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by amirshavit on 10/13/17.
 */
public class LoginServlet extends HttpServlet {

    public static final String USERNAME = "username";

    private final String GAME_HUB_URL = "../pages/gamehub.html";
    private final String SIGN_IN_URL = "../pages/signin.html";

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        User user = SessionUtils.getSessionUser(request);
        if (user != null) { // User already logged id in
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(new Gson().toJson(user));
        } else {

            String userName = validateUserName(request.getParameter(USERNAME));
            if (userName != null) {
                try {
                    user = ServletUtils.getUserManager().addUser(userName);
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

    private void processRequestRedirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        User user = SessionUtils.getSessionUser(req);
        if (user != null) { // User already logged id in
            resp.sendRedirect(GAME_HUB_URL);
            return;
        }

        String userName = validateUserName(req.getParameter(USERNAME));
        if (userName != null) {
            try {
                user = ServletUtils.getUserManager().addUser(userName);
                SessionUtils.setSessionUser(req, user);
                resp.sendRedirect(GAME_HUB_URL);
                return;
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
        processRequestRedirect(req, resp);
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


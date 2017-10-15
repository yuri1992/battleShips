package servlets;

import com.google.gson.JsonObject;
import engine.model.multi.User;
import utils.SessionUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {
    protected boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = SessionUtils.getSessionUser(request);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject obj = new JsonObject();
            obj.addProperty("desc", "User not logged in");
            response.getWriter().println(obj);
            response.getWriter().flush();
            return false;
        }
        return true;
    }
}

package servlets;

import com.google.gson.JsonObject;
import engine.model.multi.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/15/17.
 */
public abstract class BaseServlet extends HttpServlet{

    abstract protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException;

    //<editor-fold defaultstate="collapsed" desc="HttpServlet Overrides"

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        processRequest(req, resp);
    }

    @Override
    public String getServletInfo() {

        return this.getClass().toString();
    }

    //</editor-fold>

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

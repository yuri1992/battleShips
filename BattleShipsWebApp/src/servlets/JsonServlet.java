package servlets;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/15/17.
 */
public abstract class JsonServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        resp.setContentType("application/json;charset=UTF-8");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        resp.setContentType("application/json;charset=UTF-8");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        resp.setContentType("application/json;charset=UTF-8");
    }

    protected void setResponseError(HttpServletResponse response, int errorCode, String description) throws IOException {
        response.setStatus(errorCode);
        JsonObject obj = new JsonObject();
        obj.addProperty("desc", description);
        response.getWriter().println(obj);
        response.getWriter().flush();
    }

}

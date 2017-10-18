package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import engine.exceptions.UserNameTakenException;
import engine.model.multi.User;
import models.UserForJson;
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
public class APISessionServlet extends JsonServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isSessionValid(request, response)) return;

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new Gson().toJson(new UserForJson(SessionUtils.getSessionUser(request))));
        response.getWriter().flush();
    }
}

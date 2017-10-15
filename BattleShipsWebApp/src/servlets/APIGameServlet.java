package servlets;

import com.google.gson.Gson;
import engine.model.multi.Match;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


public class APIGameServlet extends JsonServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Todo: Should create a game
        // Handle XML upload
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);

        if (!isSessionValid(request, response)) return;
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new Gson().toJson(new APIGameServlet.GameListResponse(ServletUtils.getMatchManager().getMatchList())));
        response.getWriter().flush();
    }


    private class GameListResponse {

        final private Set<Match> matches;

        public GameListResponse(Set<Match> matches) {
            this.matches = matches;
        }
    }
}

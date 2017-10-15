package servlets;

import com.google.gson.Gson;
import engine.model.multi.Match;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Set;


public class APIGameServlet extends JsonServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("name");
        Part filePart = request.getPart("file");
        String fileName = Paths.get(filePart.getName()).getFileName().toString();
        InputStream fileContent = filePart.getInputStream();
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

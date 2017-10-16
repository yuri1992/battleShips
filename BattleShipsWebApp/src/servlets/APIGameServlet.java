package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import engine.exceptions.GameSettingsInitializationException;
import engine.exceptions.MatchNameTakenException;
import engine.model.multi.Match;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Set;

import static utils.SessionUtils.getSessionUser;

@MultipartConfig
public class APIGameServlet extends JsonServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        PrintWriter out = response.getWriter();

        String gameName = request.getParameter("name");
        if (gameName == null || gameName.isEmpty() || gameName.trim().isEmpty()) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Game name is required");
            return;
        }

        Part filePart = request.getPart("file");
        if (filePart == null) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "File was not selected");
            return;
        }

        gameName = gameName.trim();
        InputStream fileContent = filePart.getInputStream();

        try {
            Match match = ServletUtils.getMatchManager().addMatch(gameName, getSessionUser(request), fileContent);
            response.setStatus(HttpServletResponse.SC_OK);
            out.println(new Gson().toJson(match));
        } catch (MatchNameTakenException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Match name already exists");
        } catch (JAXBException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid XML format");
        } catch (GameSettingsInitializationException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }

        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);

        if (!isSessionValid(request, response)) return;
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new Gson().toJson(new APIGameServlet.GameListResponse(ServletUtils.getMatchManager().getMatchList())));
        response.getWriter().flush();
    }

    //<editor-fold defaultstate="collapsed" desc="Game List Response Object">
    private class GameListResponse {

        final private Set<Match> matches;
        final private int size;

        public GameListResponse(Set<Match> matches) {
            this.matches = matches;
            this.size = matches.size();
        }
    }
    //</editor-fold>
}

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


        String description = request.getParameter("name");
        if (description == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject obj = new JsonObject();
            obj.addProperty("desc", "Game name is required");
            out.println(obj);
            return;
        }

        Part filePart = request.getPart("file");
        if (filePart == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject obj = new JsonObject();
            obj.addProperty("desc", "File does not selected");
            out.println(obj);
            return;
        }

        String fileName = Paths.get(filePart.getName()).getFileName().toString();
        InputStream fileContent = filePart.getInputStream();

        try {
            Match match = ServletUtils.getMatchManager().addMatch(description, getSessionUser(request), fileContent);
            response.setStatus(HttpServletResponse.SC_OK);
            out.println(new Gson().toJson(match));
            return;
        } catch (MatchNameTakenException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject obj = new JsonObject();
            obj.addProperty("desc", "Match name already exists");
            out.println(obj);
        } catch (JAXBException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject obj = new JsonObject();
            obj.addProperty("desc", "Invalid XML format");
            out.println(obj);
        } catch (GameSettingsInitializationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonObject obj = new JsonObject();
            obj.addProperty("desc", e.toString());
            out.println(obj);
        }

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

        public GameListResponse(Set<Match> matches) {
            this.matches = matches;
        }
    }
    //</editor-fold>
}

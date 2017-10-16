package servlets;

import com.google.gson.Gson;
import engine.exceptions.GameSettingsInitializationException;
import engine.exceptions.MatchInsufficientRightsException;
import engine.exceptions.MatchNameTakenException;
import engine.exceptions.MatchNotFoundException;
import engine.model.multi.Match;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.regex.Pattern;

import static utils.SessionUtils.getSessionUser;

@MultipartConfig
public class APIGameServlet extends JsonServlet {

    private class RouteRestRequest {
        // Accommodate two requests, one for all resources, another for a specific resource
        private Pattern regExAllPattern = Pattern.compile("/");
        private Pattern regExIdPattern = Pattern.compile("/([0-9]*)");

        private Integer id = null;

        private RouteRestRequest(String pathInfo) throws ServletException {

            // Check for ID case first, since the All pattern would also match
            try {
                if (regExIdPattern.matcher(pathInfo).find()) {
                    String idStr = pathInfo.substring(1);
                    if (idStr.endsWith("/")) {
                        idStr = idStr.substring(0, idStr.length()-1);
                    }
                    id = Integer.parseInt(idStr);
                    return;
                } else if (regExAllPattern.matcher(pathInfo).find()) {
                    return;
                } else {
                    throw new ServletException("Invalid URI");
                }
            } catch (Exception e) {
                throw new ServletException("Invalid URI");
            }
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

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

        try {
            RouteRestRequest route = new RouteRestRequest(request.getPathInfo());
            if (route.getId() != null) {
                getSingleGame(response, route.getId().intValue());
            } else {
                getAllGames(response);
            }
        } catch (ServletException e) {
            setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Invalid request");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        super.doDelete(request, response);
        if (!isSessionValid(request, response)) return;

        try {
            RouteRestRequest route = new RouteRestRequest(request.getPathInfo());
            if (route.getId() != null) {
                deleteSingleGame(request, response, route.getId().intValue());
                return;
            }
        } catch (ServletException e) { }

        setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Invalid request");
    }

    private void getSingleGame(HttpServletResponse response, int matchId) throws IOException {
        try {
            Match match = ServletUtils.getMatchManager().getMatchById(matchId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new Gson().toJson(match));
            response.getWriter().flush();
        } catch (MatchNotFoundException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }
    }

    private void getAllGames(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(new Gson().toJson(new GameListResponse(ServletUtils.getMatchManager().getMatchList())));
        response.getWriter().flush();
    }

    private void deleteSingleGame(HttpServletRequest request, HttpServletResponse response, int matchId)
            throws IOException {
        try {
            ServletUtils.getMatchManager().removeMatch(matchId, SessionUtils.getSessionUser(request));
        } catch (MatchNotFoundException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, e.toString());
        } catch (MatchInsufficientRightsException e) {
            setResponseError(response, HttpServletResponse.SC_UNAUTHORIZED, e.toString());
        }
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

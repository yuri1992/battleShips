package servlets;

import com.google.gson.Gson;
import engine.exceptions.*;
import engine.model.multi.Match;
import models.MatchMetaForJson;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static utils.SessionUtils.getSessionUser;

@MultipartConfig
public class APIGamesHubServlet extends JsonServlet {

    private static final String PARAM_GAME_NAME = "name";
    private static final String PARAM_FILE_DATA = "file";

    private enum APIGamesPathTypes {
        NONE,
        REGISTER
    }

    // Accommodate several REST requests for same endpoint
    private class RouteRestRequest {
        private final String registerPath = "register";

        private Integer id = null;
        private APIGamesPathTypes pathType = APIGamesPathTypes.NONE;

        private RouteRestRequest(String pathInfoOriginal) throws ServletException {

            // Remove open / if exist
            String pathInfo = pathInfoOriginal;
            if (pathInfo != null && pathInfo.startsWith("/"))
                pathInfo = pathInfo.substring(1);
            String[] tokens = (pathInfo != null ? pathInfo.split("/") : null);

            // Check for ID case first
            if (tokens != null && tokens.length > 0 && !tokens[0].isEmpty()) {
                // token has at least 1, and it is not empty. try to extract match id from 1st token
                try {
                    id = Integer.parseInt(tokens[0]);
                } catch (NumberFormatException e) {
                    throw new ServletException("Invalid URI");
                }

                // Extracted id, now check if has more path
                if (tokens.length == 2) {
                    // check 2nd token
                    if (registerPath.equals(tokens[1]))
                        pathType = APIGamesPathTypes.REGISTER;
                    else
                        throw new ServletException("Invalid URI");
                } else if (tokens.length > 2) {
                    // to many tokens
                    throw new ServletException("Invalid URI");
                }
            }
        }

        public Integer getId() {
            return id;
        }

        public APIGamesPathTypes getPathType() {
            return pathType;
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        if (!isSessionValid(request, response)) return;

        try {
            RouteRestRequest route = new RouteRestRequest(request.getPathInfo());
            if (route.getPathType() == APIGamesPathTypes.NONE && route.getId() == null) {
                postNewGame(request, response);
            } else if (route.getPathType() == APIGamesPathTypes.REGISTER) {
                postRegisterToGame(request, response, route.getId().intValue());
            } else {
                setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Unsupported POST request");
            }
        } catch (ServletException e) {
            setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Invalid request");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
        if (!isSessionValid(request, response)) return;

        try {
            RouteRestRequest route = new RouteRestRequest(request.getPathInfo());
            if (route.getPathType() != APIGamesPathTypes.NONE) {
                setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Unsupported GET request");
            } else if (route.getId() != null) {
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
            if (route.getId() != null && route.getPathType() == APIGamesPathTypes.NONE) {
                deleteSingleGame(request, response, route.getId().intValue());
            } else {
                setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Unsupported DELETE request");
            }
        } catch (ServletException e) {
            setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Invalid request");
        }
    }

    private void getSingleGame(HttpServletResponse response, int matchId) throws IOException {
        try {
            Match match = ServletUtils.getMatchManager().getMatchById(matchId);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new Gson().toJson(new MatchMetaForJson(match)));
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
        } catch (MatchInsufficientRightsException e) {
            setResponseError(response, HttpServletResponse.SC_UNAUTHORIZED, e.toString());
        } catch (MatchException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }
    }

    private void postNewGame(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();

        String gameName = request.getParameter(PARAM_GAME_NAME);
        if (gameName == null || gameName.isEmpty() || gameName.trim().isEmpty()) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Game name is required");
            return;
        }

        Part filePart = request.getPart(PARAM_FILE_DATA);
        if (filePart == null) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "File was not selected");
            return;
        }

        gameName = gameName.trim();
        InputStream fileContent = filePart.getInputStream();

        try {
            Match match = ServletUtils.getMatchManager().addMatch(gameName, getSessionUser(request), fileContent);
            response.setStatus(HttpServletResponse.SC_OK);
            out.println(new Gson().toJson(new MatchMetaForJson(match)));
        } catch (MatchNameTakenException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Match name already exists");
        } catch (JAXBException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid XML format");
        } catch (GameSettingsInitializationException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }

        out.flush();
    }

    private void postRegisterToGame(HttpServletRequest request, HttpServletResponse response, int matchId) throws
            IOException {
        try {
            ServletUtils.getMatchManager().registerUserToMatch(matchId, SessionUtils.getSessionUser(request));
            SessionUtils.setSessionMatch(request, matchId);
        } catch (MatchNotFoundException e) {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, e.toString());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Game List Response Object">
    private class GameListResponse {

        final private List<MatchMetaForJson> matches = new ArrayList<>();
        final private int size;

        public GameListResponse(Set<Match> matches) {
            List<Match> l = new ArrayList<Match>(matches);
            l.sort((o1, o2) -> Integer.compare(o2.getMatchId(), o1.getMatchId()));
            for (Match match : l) {
                this.matches.add(new MatchMetaForJson(match));
            }
            this.size = this.matches.size();
        }
    }
    //</editor-fold>
}

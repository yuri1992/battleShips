package servlets;

import com.google.gson.Gson;
import engine.model.multi.Match;
import models.GameStatisticsObj;
import models.GameStatusObj;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class APIGameServlet extends JsonServlet  {

    private enum APIGamePathTypes {
        NONE,
        STATISTICS,
        PLAY
    }

    // Accommodate several REST requests for same endpoint
    private class RouteRestRequest {
        private final String statisticsPath = "statistics";
        private final String playPath = "play";

        private APIGamePathTypes pathType = APIGamePathTypes.NONE;

        private RouteRestRequest(String pathInfoOriginal) throws ServletException {

            // Remove first / if exist
            String pathInfo = pathInfoOriginal;
            if (pathInfo != null && pathInfo.startsWith("/"))
                pathInfo = pathInfo.substring(1);
            String[] tokens = (pathInfo != null ? pathInfo.split("/") : null);

            if (tokens == null || tokens.length == 0 || tokens[0].isEmpty()) return;

            // Check for ID case first
            if (tokens.length == 1) {
                // check 2nd token
                if (statisticsPath.equals(tokens[0]))
                    pathType = APIGamePathTypes.STATISTICS;
                else if (playPath.equals(tokens[0]))
                    pathType = APIGamePathTypes.PLAY;
                else
                    throw new ServletException("Invalid URI");
            } else {
                // to many tokens
                throw new ServletException("Invalid URI");
            }
        }

        public APIGamePathTypes getPathType() {
            return pathType;
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
            handle move
         */


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
        if (!isSessionValid(request, response)) return;

        try {
            RouteRestRequest route = new RouteRestRequest(request.getPathInfo());
            switch (route.getPathType()) {
                case NONE:
                    getGameStatusData(request, response);
                    break;
                case STATISTICS:
                    getGameStatisticsData(request, response);
                    break;
                default:
                    setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Unsupported GET request");
            }
        } catch (ServletException e) {
            setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Invalid request");
        }
    }

    private void getGameStatusData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Match match = SessionUtils.getSessionMatch(request);
        if (match != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new Gson().toJson(new GameStatusObj(match, SessionUtils.getSessionUser(request))));
            response.getWriter().flush();
        } else {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "User is not registered to any match");
        }
    }

    private void getGameStatisticsData(HttpServletRequest request, HttpServletResponse response) throws
            IOException {
        Match match = SessionUtils.getSessionMatch(request);
        if (match != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new Gson().toJson(new GameStatisticsObj(match)));
            response.getWriter().flush();
        } else {
            setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "User is not registered to any match");
        }
    }

}

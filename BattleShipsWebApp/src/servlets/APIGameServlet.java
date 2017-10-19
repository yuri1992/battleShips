package servlets;

import com.google.gson.Gson;
import engine.model.boards.GridPoint;
import engine.model.boards.Player;
import engine.model.multi.Match;
import engine.model.multi.User;
import models.GameStatisticsObj;
import models.GameStatusObj;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class APIGameServlet extends JsonServlet  {

    private enum APIGamePathTypes {
        NONE,
        STATISTICS,
        TURN
    }

    private enum APIGameTurnType {
        ATTACK,
        MINE,
        UNDEFINED
    }

    //<editor-fold desc="Private Data Models" defaultstate="collapsed">

    private static final String PARAM_KEY_TURN_TYPE = "type";
    private static final String PARAM_VAL_ATTACK_TYPE = "attack";
    private static final String PARAM_VAL_MINE_TYPE = "mine";
    private static final String PARAM_KEY_ROW = "row";
    private static final String PARAM_KEY_COL = "col";

    // Accommodate several REST requests for same endpoint
    private class RouteRestRequest {
        private final String statisticsPath = "statistics";
        private final String turnPath = "turn";

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
                else if (turnPath.equals(tokens[0]))
                    pathType = APIGamePathTypes.TURN;
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

    private class PlayTurnData {

        private APIGameTurnType turnType = APIGameTurnType.UNDEFINED;
        private GridPoint gridPoiont;
        private boolean isLegal;

        private PlayTurnData(HttpServletRequest request, HttpServletResponse response) throws IOException {
            isLegal = true;
            String turnTypeStr = request.getParameter(PARAM_KEY_TURN_TYPE);
            String rowStr = request.getParameter(PARAM_KEY_ROW);
            String colStr = request.getParameter(PARAM_KEY_COL);
            if (turnTypeStr == null || turnTypeStr.isEmpty() || turnTypeStr.trim().isEmpty() ||
                    rowStr == null || rowStr.isEmpty() || rowStr.trim().isEmpty() ||
                    colStr == null || colStr.isEmpty() || colStr.trim().isEmpty()) {
                setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing params");
                isLegal = false;
                return;
            }

            int row;
            int col;
            try {
                row = Integer.parseInt(rowStr);
                col = Integer.parseInt(colStr);
                gridPoiont = new GridPoint(row, col);
            } catch (NumberFormatException e) {
                setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Bad points params");
                isLegal = false;
                return;
            }

            // Make sure it is user's turn
            Match match = SessionUtils.getSessionMatch(request);
            User user = SessionUtils.getSessionUser(request);
            if (!match.isUserTurn(user)) {
                setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Not user's turn");
                isLegal = false;
                return;
            }

            if (PARAM_VAL_ATTACK_TYPE.equals(turnTypeStr)) {
                turnType = APIGameTurnType.ATTACK;
            } else if (PARAM_VAL_MINE_TYPE.equals(turnTypeStr)) {
                turnType = APIGameTurnType.MINE;
            } else {
                setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, "Illegal turn type");
                isLegal = false;
                return;
            }
        }

        public APIGameTurnType getTurnType() {
            return turnType;
        }

        public GridPoint getGridPoiont() {
            return gridPoiont;
        }

        public boolean isLegal() {
            return isLegal;
        }
    }

    //</editor-fold>

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        if (!isSessionValid(request, response)) return;

        try {
            RouteRestRequest route = new RouteRestRequest(request.getPathInfo());
            switch (route.getPathType()) {
                case TURN:
                    postPlayTurn(request, response);
                    break;
                default:
                    setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Unsupported GET request");
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

    private void postPlayTurn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Got to POST a move");

        PlayTurnData turn = new PlayTurnData(request, response);
        if (!turn.isLegal()) return;

        Match match = SessionUtils.getSessionMatch(request);
        User user = SessionUtils.getSessionUser(request);

        if (turn.getTurnType() == APIGameTurnType.ATTACK) {
            postPlayAttackTurn(match, user, turn.getGridPoiont(), response);
        } else if (turn.getTurnType() == APIGameTurnType.MINE) {
            postPlayMineTurn(match, user, turn.getGridPoiont(), response);
        }
    }

    private void postPlayMineTurn(Match match, User user, GridPoint gp, HttpServletResponse response) throws
            IOException {
        if (match.getGameManager().placeMine(gp)) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            setResponseError(response, HttpServletResponse.SC_CONFLICT, "You can not put mine in that place");
        }
    }

    private void postPlayAttackTurn(Match match, User user, GridPoint gp, HttpServletResponse response) {
    }


    @Override
    protected boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!super.isSessionValid(request, response)) return false;

        // Make sure user is registered to game
        Match match = SessionUtils.getSessionMatch(request);
        User user = SessionUtils.getSessionUser(request);
        return (match.isUserRegistered(user));
    }

}

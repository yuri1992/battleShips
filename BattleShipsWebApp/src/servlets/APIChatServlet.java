package servlets;

import com.google.gson.Gson;
import engine.model.multi.Match;
import engine.model.multi.User;
import models.Chat;
import models.ChatMessage;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class APIChatServlet extends JsonServlet {

    private static final String PARAM_MSG = "msg";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
        if (!isSessionValid(request, response)) return;
        Match match = SessionUtils.getSessionMatch(request);
        User sessionUser = SessionUtils.getSessionUser(request);
        if (match != null) {
            Chat chat = ServletUtils.getChatManager().getOrCreateChat(match.getMatchId());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new Gson().toJson(chat.getMessages()));
            response.getWriter().flush();
            return;
        } else {
            setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Invalid request");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
        if (!isSessionValid(request, response)) return;

        Match match = SessionUtils.getSessionMatch(request);
        if (match != null) {
            User sessionUser = SessionUtils.getSessionUser(request);
            Chat chat = ServletUtils.getChatManager().getOrCreateChat(match.getMatchId());
            String msg = request.getParameter(PARAM_MSG);
            ChatMessage chatMessage = chat.addMessage(msg, sessionUser);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(new Gson().toJson(chatMessage));
            response.getWriter().flush();
            return;
        } else {
            setResponseError(response, HttpServletResponse.SC_NOT_FOUND, "Invalid request");
        }

    }

}

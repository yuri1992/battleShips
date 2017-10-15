package servlets;

import engine.exceptions.UserNameTakenException;
import engine.model.multi.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by amirshavit on 10/13/17.
 */
public class GameServlet extends HttpServlet {


    //<editor-fold defaultstate="collapsed" desc="HttpServlet Overrides"

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Fowarding to signup page
        RequestDispatcher rd = req.getRequestDispatcher("game.jsp");
        rd.forward(req, resp);

    }

    @Override
    public String getServletInfo() {
        return this.getClass().toString();
    }

}


package Servlets;

import DB.DBInfo;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Kept for compatibility with older clients. Private invitations are generated
 * only while a room is created; this endpoint never mints access tokens.
 */
@WebServlet(name = "GenerateURLServlet", urlPatterns = {"/GenerateURL"})
public class GenerateURLServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new Gson().toJson(
                Collections.singletonMap("lobbyUrl", DBInfo.APP_BASE_URL + "/WelcomePage"),
                response.getWriter());
    }
}

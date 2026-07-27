package Servlets;

import Classes.Chat;
import Classes.RoomSummary;
import Classes.TextParser;
import DB.ConnectionPool;
import DB.TagsDAO;
import Sockets.ChatEndpoint;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {
    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String line = request.getParameter("line");
        if (line == null || line.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/topRooms");
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            TagsDAO tags = new TagsDAO(connection);
            ArrayList<Chat> matches = tags.searchChats(TextParser.parseForSearch(line));
            List<RoomSummary> result = new ArrayList<>();
            for (Chat chat : matches) {
                result.add(new RoomSummary(
                        chat,
                        ChatEndpoint.getActiveUserCount(chat.getID()),
                        tags.getTags(chat.getID())));
            }
            response.getWriter().print(GSON.toJson(result));
        } catch (SQLException exception) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Search is unavailable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

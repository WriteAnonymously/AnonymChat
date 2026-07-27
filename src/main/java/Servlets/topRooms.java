package Servlets;

import Classes.Chat;
import Classes.RoomSummary;
import DB.ChatInfoDAO;
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

@WebServlet(name = "topRooms", urlPatterns = {"/topRooms"})
public class topRooms extends HttpServlet {
    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            ChatInfoDAO chats = new ChatInfoDAO(connection);
            TagsDAO tags = new TagsDAO(connection);
            List<RoomSummary> result = new ArrayList<>();
            for (Chat chat : chats.getTopNChats(24)) {
                result.add(new RoomSummary(
                        chat,
                        ChatEndpoint.getActiveUserCount(chat.getID()),
                        tags.getTags(chat.getID())));
            }
            response.getWriter().print(GSON.toJson(result));
        } catch (SQLException exception) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Rooms are unavailable.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

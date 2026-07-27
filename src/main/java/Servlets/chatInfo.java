package Servlets;

import Classes.Chat;
import Classes.PrivateChat;
import Classes.User;
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

@WebServlet(name = "chatInfo", urlPatterns = {"/chatInfo"})
public class chatInfo extends HttpServlet {
    private static final Gson GSON = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store");
        try {
            long id = Long.parseLong(request.getParameter("id"));
            try (Connection connection = ConnectionPool.getInstance().getConnection()) {
                Chat chat = new ChatInfoDAO(connection).getChatInfo(id);
                if (chat == null || !canView(chat, request)) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                RoomSummary summary = new RoomSummary(
                        chat,
                        ChatEndpoint.getActiveUserCount(id),
                        new TagsDAO(connection).getTags(id));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(GSON.toJson(summary));
            }
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid room.");
        } catch (SQLException exception) {
            getServletContext().log("Could not load room details", exception);
            response.sendError(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Room details are unavailable.");
        }
    }

    private boolean canView(Chat chat, HttpServletRequest request) {
        if (!(chat instanceof PrivateChat)) {
            return true;
        }
        Object user = request.getSession(false) == null
                ? null
                : request.getSession(false).getAttribute(Long.toString(chat.getID()));
        return user instanceof User;
    }
}

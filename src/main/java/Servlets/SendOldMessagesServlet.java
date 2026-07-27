package Servlets;

import Classes.Constants;
import Classes.Message;
import Classes.User;
import DB.ConnectionPool;
import DB.MessageInfoDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "OldMessages", urlPatterns = {"/OldMessages"})
public class SendOldMessagesServlet extends HttpServlet {
    private static final Gson GSON = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "private, no-store");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.CHAT_ID) == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            long chatId = Long.parseLong(session.getAttribute(Constants.CHAT_ID).toString());
            User user = (User) session.getAttribute(Long.toString(chatId));
            if (user == null || user.getChatId() != chatId) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            int requested = Math.max(
                    1,
                    Math.min(1000, Integer.parseInt(request.getParameter(Constants.NUM_MESSAGE))));

            try (Connection connection = ConnectionPool.getInstance().getConnection()) {
                List<Message> messages =
                        new MessageInfoDAO(connection).getLastNMessages(requested, chatId);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().print(GSON.toJson(messages));
            }
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException exception) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}

package Servlets;

import Classes.MailMessage;
import Classes.MessageSender;
import DB.DBInfo;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "ContactServlet", urlPatterns = {"/ContactServlet"})
public class ContactServlet extends HttpServlet {
    private static final Pattern EMAIL =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final long COOLDOWN_MILLIS = 60_000;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        Long lastSent = (Long) session.getAttribute("contactLastSent");
        long now = System.currentTimeMillis();
        if (lastSent != null && now - lastSent < COOLDOWN_MILLIS) {
            response.sendError(429, "Please wait a minute.");
            return;
        }

        String name = clean(request.getParameter("name"), 100);
        String email = clean(request.getParameter("e-mail"), 254);
        String content = clean(request.getParameter("content"), 4_000);
        if (name.isEmpty() || content.isEmpty() || !EMAIL.matcher(email).matches()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Check the contact form.");
            return;
        }

        MailMessage message = new MailMessage(
                DBInfo.CONTACT_EMAIL,
                content + "\n\nReply to: " + email,
                "AnonymChat message from " + name);
        try {
            new MessageSender(List.of(message)).send();
            session.setAttribute("contactLastSent", now);
            response.sendRedirect(request.getContextPath() + "/Models/Conctact.html?sent=1");
        } catch (MessagingException exception) {
            getServletContext().log("Could not send contact email", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private String clean(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        String cleaned = value.strip();
        return cleaned.length() > maxLength ? cleaned.substring(0, maxLength) : cleaned;
    }
}

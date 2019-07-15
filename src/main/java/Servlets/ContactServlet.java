package Servlets;

import Classes.Constants;
import Classes.MailMessage;
import Classes.Message;
import Classes.MessageSender;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ContactServlet", urlPatterns = {"/ContactServlet"})
public class ContactServlet extends HttpServlet {
    /**
     * Sends Message from a user to our mail
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String mail = request.getParameter("e-mail");
        String content = request.getParameter("content");
        String fullText = content +  "  Mail to respond : " + mail;
        String subject = "Message From " + name;
        MailMessage message = new MailMessage(Constants.CONTACT_MAIL,fullText,subject);

        List<MailMessage> toSend = new ArrayList<MailMessage>();
        toSend.add(message);

        MessageSender sender = new MessageSender(toSend);
        try {
            sender.send();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/WelcomeServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

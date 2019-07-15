package Servlets;

import Classes.Message;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.server.UID;
import java.util.UUID;

@WebServlet(name = "GenerateURLServlet", urlPatterns = {"/GenerateURL"})
public class GenerateURLServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long chatID = Long.parseLong((String) request.getSession().getAttribute("chatID"));
        String url = String.valueOf(request.getRequestURL());
        Cookie[] cookies = request.getCookies();
        Cookie cookie = new Cookie("chatID", "1");
        UUID uuid = UUID.randomUUID();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String jsonString = gson.toJson(url);
        out.println(jsonString);
    }
}

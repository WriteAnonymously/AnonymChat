package Sockets;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class RequestListener implements ServletRequestListener {
    public void requestDestroyed(ServletRequestEvent sre) {
        // TODO Auto-generated method stub
    }

    public void requestInitialized(ServletRequestEvent sre) {
     //   System.out.println("reqqq");
        ((HttpServletRequest) sre.getServletRequest()).getSession();
    }
}

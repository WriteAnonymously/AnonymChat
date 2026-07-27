package Servlets;

import DB.DBInfo;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String fetchSite = request.getHeader("Sec-Fetch-Site");
        if (isStateChanging(request.getMethod())) {
            String origin = request.getHeader("Origin");
            boolean wrongOrigin = origin != null
                    && !"*".equals(DBInfo.ALLOWED_ORIGIN)
                    && !DBInfo.ALLOWED_ORIGIN.equalsIgnoreCase(origin);
            if ("cross-site".equalsIgnoreCase(fetchSite) || wrongOrigin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        response.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");
        response.setHeader(
                "Content-Security-Policy",
                "default-src 'self'; "
                        + "connect-src 'self' ws: wss:; "
                        + "img-src 'self' data:; "
                        + "style-src 'self'; "
                        + "script-src 'self'; "
                        + "font-src 'self'; "
                        + "form-action 'self'; frame-ancestors 'none'; base-uri 'self'");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private boolean isStateChanging(String method) {
        return "POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "PATCH".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method);
    }
}

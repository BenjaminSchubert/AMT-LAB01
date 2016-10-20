package ch.heigvd.amt.amtproject.web;

import ch.heigvd.amt.amtproject.util.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet handling the requests related to the logout action.
 *
 * @author Benjamin Schubert and Basile Vu
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Session.isCurrentUserConnected(request)) {
            Session.disconnectCurrentUser(request);
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
}

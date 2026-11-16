package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Servlet Controller xử lý yêu cầu Quên mật khẩu (/forgot-password).
 */
@WebServlet(urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập địa chỉ email đã đăng ký!");
            req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        email = email.trim();
        req.setAttribute("email", email);

        boolean isSent = userService.forgotPassword(email);
        if (isSent) {
            HttpSession session = req.getSession();
            session.setAttribute("resetEmail", email);
            session.setAttribute("infoMessage", "Mã xác thực OTP đã được gửi đến email " + email + ". Vui lòng nhập mã OTP và mật khẩu mới!");
            resp.sendRedirect(req.getContextPath() + "/reset-password");
        } else {
            req.setAttribute("errorMessage", "Email '" + email + "' không tồn tại trong hệ thống!");
            req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
        }
    }
}

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
import java.util.regex.Pattern;

/**
 * Servlet Controller xử lý yêu cầu Quên mật khẩu (/forgot-password).
 * Server-side Validation: kiểm tra null/rỗng và định dạng Email Regex.
 */
@WebServlet(urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

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
        if (email == null || email.isBlank()) {
            req.setAttribute("errorMessage", "Vui lòng nhập địa chỉ email đã đăng ký!");
            req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
            return;
        }

        email = email.trim();
        req.setAttribute("email", email);

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            req.setAttribute("errorMessage", "Địa chỉ email không đúng định dạng! Ví dụ: user@example.com");
            req.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(req, resp);
            return;
        }

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

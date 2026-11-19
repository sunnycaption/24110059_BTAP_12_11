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
 * Servlet Controller xử lý Đặt lại mật khẩu mới (/reset-password).
 */
@WebServlet(urlPatterns = {"/reset-password"})
public class ResetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String email = req.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            email = (String) session.getAttribute("resetEmail");
        } else {
            session.setAttribute("resetEmail", email);
        }

        req.setAttribute("email", email);

        String infoMessage = (String) session.getAttribute("infoMessage");
        if (infoMessage != null) {
            req.setAttribute("infoMessage", infoMessage);
            session.removeAttribute("infoMessage");
        }

        req.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        String email = req.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            email = (String) session.getAttribute("resetEmail");
        }

        req.setAttribute("email", email);

        // Gửi lại mã OTP
        if ("resend".equalsIgnoreCase(action)) {
            if (email != null && !email.trim().isEmpty()) {
                boolean sent = userService.resendOtp(email.trim(), "Đặt lại mật khẩu");
                if (sent) {
                    req.setAttribute("infoMessage", "Mã OTP mới đã được gửi lại vào email của bạn!");
                } else {
                    req.setAttribute("errorMessage", "Không thể gửi lại mã OTP. Vui lòng kiểm tra lại email!");
                }
            }
            req.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(req, resp);
            return;
        }

        String otp = req.getParameter("otp");
        String password = req.getParameter("password");
        if (password == null || password.isBlank()) {
            password = req.getParameter("newPassword");
        }
        String confirmPassword = req.getParameter("confirmPassword");

        if (email == null || email.isBlank() ||
            otp == null || otp.isBlank() ||
            password == null || password.isBlank() ||
            confirmPassword == null || confirmPassword.isBlank()) {
            req.setAttribute("errorMessage", "Vui lòng nhập đầy đủ tất cả các trường!");
            req.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(req, resp);
            return;
        }

        otp = otp.trim();
        if (!otp.matches("^[0-9]{6}$")) {
            req.setAttribute("errorMessage", "Mã OTP phải gồm đúng 6 chữ số!");
            req.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("errorMessage", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            req.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(req, resp);
            return;
        }

        boolean resetSuccess = userService.resetPassword(email.trim(), otp.trim(), password);
        if (resetSuccess) {
            session.removeAttribute("resetEmail");
            session.setAttribute("successMessage", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập bằng mật khẩu mới.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("errorMessage", "Mã OTP không chính xác hoặc đã hết hạn!");
            req.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(req, resp);
        }
    }
}

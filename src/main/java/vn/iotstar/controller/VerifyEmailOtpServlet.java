package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * Servlet Controller xử lý xác thực mã OTP đổi địa chỉ Email (/verify-email-otp).
 */
@WebServlet(urlPatterns = {"/verify-email-otp"})
public class VerifyEmailOtpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) (session.getAttribute("currentUser") != null ? session.getAttribute("currentUser") : session.getAttribute("user")) : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy thông tin người dùng mới nhất từ database
        User freshUser = userService.findById(currentUser.getId());
        String pendingEmail = (freshUser != null) ? freshUser.getPendingEmail() : null;
        if (pendingEmail == null || pendingEmail.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        req.setAttribute("pendingEmail", pendingEmail);

        String infoMessage = (String) session.getAttribute("infoMessage");
        if (infoMessage != null) {
            req.setAttribute("infoMessage", infoMessage);
            session.removeAttribute("infoMessage");
        }

        req.getRequestDispatcher("/WEB-INF/views/verify-email-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) (session.getAttribute("currentUser") != null ? session.getAttribute("currentUser") : session.getAttribute("user")) : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        User freshUser = userService.findById(currentUser.getId());
        String pendingEmail = (freshUser != null) ? freshUser.getPendingEmail() : null;

        // Xử lý gửi lại mã OTP
        if ("resend".equalsIgnoreCase(action)) {
            if (pendingEmail != null && !pendingEmail.isBlank()) {
                boolean sent = userService.requestChangeEmail(currentUser.getId(), pendingEmail);
                if (sent) {
                    req.setAttribute("infoMessage", "Mã OTP mới đã được gửi lại vào email mới: " + pendingEmail);
                } else {
                    req.setAttribute("errorMessage", "Không thể gửi lại mã OTP. Vui lòng thử lại sau!");
                }
            }
            req.setAttribute("pendingEmail", pendingEmail);
            req.getRequestDispatcher("/WEB-INF/views/verify-email-otp.jsp").forward(req, resp);
            return;
        }

        String otp = req.getParameter("otp");
        if (otp == null || otp.isBlank() || !otp.trim().matches("^[0-9]{6}$")) {
            req.setAttribute("errorMessage", "Vui lòng nhập đúng 6 chữ số mã OTP!");
            req.setAttribute("pendingEmail", pendingEmail);
            req.getRequestDispatcher("/WEB-INF/views/verify-email-otp.jsp").forward(req, resp);
            return;
        }

        boolean verified = userService.verifyChangeEmailOtp(currentUser.getId(), otp.trim());
        if (verified) {
            User updatedUser = userService.findById(currentUser.getId());
            session.setAttribute("currentUser", updatedUser);
            session.setAttribute("user", updatedUser);
            session.setAttribute("successMessage", "Thay đổi địa chỉ email thành công! Email mới của bạn là: " + updatedUser.getEmail());
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            req.setAttribute("errorMessage", "Mã OTP không chính xác hoặc đã hết hạn! Vui lòng thử lại.");
            req.setAttribute("pendingEmail", pendingEmail);
            req.getRequestDispatcher("/WEB-INF/views/verify-email-otp.jsp").forward(req, resp);
        }
    }
}

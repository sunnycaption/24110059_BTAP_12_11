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
 * Servlet Controller xử lý xác thực mã OTP kích hoạt tài khoản (/verify-otp).
 */
@WebServlet(urlPatterns = {"/verify-otp"})
public class VerifyOtpServlet extends HttpServlet {
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
            email = (String) session.getAttribute("verifyEmail");
        } else {
            session.setAttribute("verifyEmail", email);
        }

        req.setAttribute("email", email);

        // Chuyển thông báo từ session sang request nếu có
        String infoMessage = (String) session.getAttribute("infoMessage");
        if (infoMessage != null) {
            req.setAttribute("infoMessage", infoMessage);
            session.removeAttribute("infoMessage");
        }

        req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        HttpSession session = req.getSession();
        if (email == null || email.trim().isEmpty()) {
            email = (String) session.getAttribute("verifyEmail");
        }

        req.setAttribute("email", email);

        // Trường hợp người dùng yêu cầu gửi lại mã OTP
        if ("resend".equalsIgnoreCase(action)) {
            if (email != null && !email.trim().isEmpty()) {
                boolean resent = userService.resendOtp(email.trim(), "Kích hoạt tài khoản");
                if (resent) {
                    req.setAttribute("infoMessage", "Mã OTP mới đã được gửi lại vào email của bạn!");
                } else {
                    req.setAttribute("errorMessage", "Không thể gửi lại mã OTP. Vui lòng kiểm tra lại email!");
                }
            } else {
                req.setAttribute("errorMessage", "Vui lòng nhập email để gửi lại mã OTP!");
            }
            req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(req, resp);
            return;
        }

        // Kiểm tra mã OTP nhập vào
        if (otp == null || otp.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập mã OTP 6 chữ số!");
            req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(req, resp);
            return;
        }

        boolean verified = userService.verifyOtp(email, otp.trim());
        if (verified) {
            session.removeAttribute("verifyEmail");
            session.setAttribute("successMessage", "Kích hoạt tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("errorMessage", "Mã OTP không chính xác hoặc đã hết hạn! Vui lòng thử lại hoặc bấm Gửi lại mã.");
            req.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(req, resp);
        }
    }
}

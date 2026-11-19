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
 * Servlet Controller xử lý chức năng Đăng nhập người dùng (MVC - Controller).
 * Hỗ trợ xác thực mật khẩu hash BCrypt và kiểm tra trạng thái kích hoạt OTP.
 */
@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Nếu đã đăng nhập thì chuyển hướng thẳng về trang chủ
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Đọc thông báo thành công từ session nếu có (ví dụ sau khi kích hoạt OTP hoặc đổi pass)
        if (session != null) {
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }
        }

        // Chuyển hướng tới giao diện login.jsp
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 1. Server-side Validation: kiểm tra null/rỗng
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("errorMessage", "Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu!");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        username = username.trim();
        password = password.trim();

        // 2. Server-side Validation: độ dài mật khẩu >= 6 ký tự
        if (password.length() < 6) {
            req.setAttribute("errorMessage", "Mật khẩu phải chứa ít nhất 6 ký tự!");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // 3. Gọi tầng Service kiểm tra đăng nhập qua BCrypt
        User user = userService.login(username, password);

        if (user != null) {
            // 3. Kiểm tra trạng thái kích hoạt tài khoản
            if (!user.isStatus()) {
                HttpSession session = req.getSession();
                session.setAttribute("verifyEmail", user.getEmail());
                req.setAttribute("notActivated", true);
                req.setAttribute("userEmail", user.getEmail());
                req.setAttribute("errorMessage", "Tài khoản của bạn chưa được kích hoạt bằng OTP!");
                req.setAttribute("username", username);
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
                return;
            }

            // Đăng nhập thành công -> lưu thông tin vào HttpSession
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);

            // Chuyển hướng sang trang chủ
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            // Đăng nhập thất bại
            req.setAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}

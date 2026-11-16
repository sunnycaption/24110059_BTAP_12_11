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
 * Servlet Controller xử lý chức năng Đăng ký tài khoản (MVC - Controller).
 * Tích hợp cơ chế gửi OTP kích hoạt qua Email.
 */
@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Chuyển hướng tới giao diện register.jsp
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");

        // Giữ lại giá trị người dùng đã nhập để hiển thị lại nếu có lỗi
        req.setAttribute("username", username);
        req.setAttribute("fullname", fullname);
        req.setAttribute("email", email);

        // 1. Kiểm tra không để trống các trường
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty() ||
            fullname == null || fullname.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            
            req.setAttribute("errorMessage", "Vui lòng điền đầy đủ tất cả các trường thông tin!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        username = username.trim();
        fullname = fullname.trim();
        email = email.trim();

        // 2. Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp với mật khẩu đã nhập!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 3. Kiểm tra độ dài mật khẩu (tối thiểu 6 ký tự)
        if (password.length() < 6) {
            req.setAttribute("errorMessage", "Mật khẩu phải chứa ít nhất 6 ký tự!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 4. Kiểm tra username đã tồn tại chưa
        if (userService.checkExistUsername(username)) {
            req.setAttribute("errorMessage", "Tên đăng nhập '" + username + "' đã tồn tại! Vui lòng chọn tên khác.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 5. Kiểm tra email đã tồn tại chưa
        if (userService.checkExistEmail(email)) {
            req.setAttribute("errorMessage", "Email '" + email + "' đã được đăng ký! Vui lòng chọn email khác.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 6. Thực hiện đăng ký tài khoản (Tạo tài khoản chưa kích hoạt, gửi OTP qua Email)
        boolean isSuccess = userService.register(username, password, fullname, email);

        if (isSuccess) {
            // Lưu email vào session để tiện trang verify-otp sử dụng
            HttpSession session = req.getSession();
            session.setAttribute("verifyEmail", email);
            session.setAttribute("infoMessage", "Mã xác thực OTP đã được gửi về email " + email + ". Vui lòng nhập mã để kích hoạt tài khoản!");

            // Chuyển hướng sang trang nhập OTP xác thực
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
        } else {
            req.setAttribute("errorMessage", "Đăng ký không thành công do lỗi hệ thống. Vui lòng thử lại!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}

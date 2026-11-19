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
 * Servlet Controller xử lý chức năng Đăng ký tài khoản (MVC - Controller).
 * Tích hợp cơ chế gửi OTP kích hoạt qua Email.
 * Server-side Validation đầy đủ: null/rỗng, email regex, phone VN, password >= 6, confirm match.
 */
@WebServlet(urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_VN_PATTERN = Pattern.compile("^0[0-9]{9}$");

    @Override
    public void init() throws ServletException {
        super.init();
        this.userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        String phone = req.getParameter("phone");

        // Giữ lại giá trị người dùng đã nhập
        req.setAttribute("username", username);
        req.setAttribute("fullname", fullname);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);

        // 1. Kiểm tra không để trống
        if (username == null || username.isBlank() ||
            password == null || password.isBlank() ||
            confirmPassword == null || confirmPassword.isBlank() ||
            fullname == null || fullname.isBlank() ||
            email == null || email.isBlank()) {
            req.setAttribute("errorMessage", "Vui lòng điền đầy đủ tất cả các trường bắt buộc (*)!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        username = username.trim();
        fullname = fullname.trim();
        email = email.trim();

        // 2. Validate định dạng Email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            req.setAttribute("errorMessage", "Địa chỉ email không đúng định dạng! Ví dụ: abc@gmail.com");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 3. Validate số điện thoại VN (nếu có nhập)
        if (phone != null && !phone.isBlank()) {
            phone = phone.trim();
            if (!PHONE_VN_PATTERN.matcher(phone).matches()) {
                req.setAttribute("errorMessage", "Số điện thoại phải bắt đầu bằng 0 và gồm đúng 10 chữ số!");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }
        }

        // 4. Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp với mật khẩu đã nhập!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 5. Kiểm tra độ dài mật khẩu (tối thiểu 6 ký tự)
        if (password.length() < 6) {
            req.setAttribute("errorMessage", "Mật khẩu phải chứa ít nhất 6 ký tự!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 6. Kiểm tra username đã tồn tại
        if (userService.checkExistUsername(username)) {
            req.setAttribute("errorMessage", "Tên đăng nhập '" + username + "' đã tồn tại! Vui lòng chọn tên khác.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 7. Kiểm tra email đã tồn tại
        if (userService.checkExistEmail(email)) {
            req.setAttribute("errorMessage", "Email '" + email + "' đã được đăng ký! Vui lòng chọn email khác.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        // 8. Thực hiện đăng ký
        boolean isSuccess = userService.register(username, password, fullname, email);

        if (isSuccess) {
            HttpSession session = req.getSession();
            session.setAttribute("verifyEmail", email);
            session.setAttribute("infoMessage", "Mã xác thực OTP đã được gửi về email " + email + ". Vui lòng nhập mã để kích hoạt tài khoản!");
            resp.sendRedirect(req.getContextPath() + "/verify-otp");
        } else {
            req.setAttribute("errorMessage", "Đăng ký không thành công do lỗi hệ thống. Vui lòng thử lại!");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }
}

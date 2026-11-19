package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.utils.CloudinaryUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * Servlet Controller quản lý Hồ sơ cá nhân người dùng (/profile).
 * Hỗ trợ cập nhật thông tin cá nhân (fullname, phone),
 * upload ảnh đại diện (avatar) lên Cloudinary bằng Multipart,
 * và quy trình đổi địa chỉ Email kèm xác thực mã OTP gửi về email mới.
 */
@WebServlet(urlPatterns = {"/profile"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,     // 10MB cho avatar
        maxRequestSize = 1024 * 1024 * 20   // 20MB tổng request
)
public class ProfileServlet extends HttpServlet {
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
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy thông tin mới nhất từ database
        User freshUser = userService.findById(currentUser.getId());
        if (freshUser != null) {
            session.setAttribute("currentUser", freshUser);
            req.setAttribute("user", freshUser);
        } else {
            req.setAttribute("user", currentUser);
        }

        // Đọc flash messages từ session
        if (session != null) {
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                req.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {
                req.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }
            String infoMessage = (String) session.getAttribute("infoMessage");
            if (infoMessage != null) {
                req.setAttribute("infoMessage", infoMessage);
                session.removeAttribute("infoMessage");
            }
        }

        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            action = "update_info";
        }

        switch (action) {
            case "change_email":
                handleChangeEmail(req, resp, currentUser);
                break;
            case "verify_email_otp":
                handleVerifyEmailOtp(req, resp, currentUser);
                break;
            case "update_info":
            default:
                handleUpdateInfo(req, resp, currentUser);
                break;
        }
    }

    /**
     * Cập nhật thông tin cá nhân (fullname, phone) & Upload Avatar lên Cloudinary bằng Multipart.
     */
    private void handleUpdateInfo(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        // 1. Server-side Validation: Họ và tên
        if (fullname == null || fullname.isBlank()) {
            req.setAttribute("errorMessage", "Họ và tên không được để trống!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
            return;
        }

        fullname = fullname.trim();

        // 2. Server-side Validation: Số điện thoại Việt Nam (nếu có nhập)
        if (phone != null && !phone.isBlank()) {
            phone = phone.trim();
            if (!PHONE_VN_PATTERN.matcher(phone).matches()) {
                req.setAttribute("errorMessage", "Số điện thoại không hợp lệ! Vui lòng nhập 10 chữ số bắt đầu bằng 0.");
                req.setAttribute("user", currentUser);
                req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
                return;
            }
        } else {
            phone = null;
        }

        // 3. Upload ảnh đại diện lên Cloudinary (nếu có chọn file ảnh mới)
        String avatarUrl = currentUser.getImages(); // Mặc định giữ nguyên ảnh cũ
        Part imagePart = req.getPart("imageFile");
        if (imagePart != null && imagePart.getSize() > 0) {
            String submittedFileName = imagePart.getSubmittedFileName();
            if (submittedFileName != null && !submittedFileName.isBlank()) {
                String fileNameLower = Paths.get(submittedFileName).getFileName().toString().toLowerCase();
                if (!fileNameLower.endsWith(".jpg") && !fileNameLower.endsWith(".jpeg")
                        && !fileNameLower.endsWith(".png") && !fileNameLower.endsWith(".gif")
                        && !fileNameLower.endsWith(".webp")) {
                    req.setAttribute("errorMessage", "Định dạng ảnh không hợp lệ! Chỉ chấp nhận file JPG, PNG, GIF, WEBP.");
                    req.setAttribute("user", currentUser);
                    req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
                    return;
                }

                String uploadedUrl = CloudinaryUtil.uploadPart(imagePart, req);
                if (uploadedUrl != null && !uploadedUrl.isBlank()) {
                    avatarUrl = uploadedUrl;
                }
            }
        }

        // 4. Lưu vào database và cập nhật lại session
        boolean updated = userService.updateProfile(currentUser.getId(), fullname, phone, avatarUrl);
        if (updated) {
            User fresh = userService.findById(currentUser.getId());
            req.getSession().setAttribute("currentUser", fresh);
            req.getSession().setAttribute("successMessage", "Cập nhật thông tin hồ sơ thành công!");
        } else {
            req.getSession().setAttribute("errorMessage", "Không thể cập nhật hồ sơ do lỗi hệ thống!");
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    /**
     * Xử lý yêu cầu đổi email: lưu pendingEmail, sinh OTP và gửi về email mới.
     */
    private void handleChangeEmail(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        String newEmail = req.getParameter("newEmail");

        if (newEmail == null || newEmail.isBlank()) {
            req.setAttribute("errorMessage", "Vui lòng nhập địa chỉ email mới!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
            return;
        }

        newEmail = newEmail.trim();

        // 1. Kiểm tra định dạng email
        if (!EMAIL_PATTERN.matcher(newEmail).matches()) {
            req.setAttribute("errorMessage", "Địa chỉ email mới không đúng định dạng!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
            return;
        }

        // 2. Không được trùng với email hiện tại
        if (newEmail.equalsIgnoreCase(currentUser.getEmail())) {
            req.setAttribute("errorMessage", "Email mới phải khác với email hiện tại của bạn!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
            return;
        }

        // 3. Kiểm tra xem email mới đã bị tài khoản khác sử dụng chưa
        if (userService.checkExistEmail(newEmail)) {
            req.setAttribute("errorMessage", "Địa chỉ email '" + newEmail + "' đã được sử dụng bởi tài khoản khác!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
            return;
        }

        // 4. Gửi OTP đến email mới và lưu vào pendingEmail
        boolean sent = userService.requestChangeEmail(currentUser.getId(), newEmail);
        if (sent) {
            HttpSession session = req.getSession();
            session.setAttribute("pendingEmail", newEmail);
            session.setAttribute("infoMessage", "Mã xác thực OTP đã được gửi đến email mới: " + newEmail + ". Vui lòng nhập mã OTP để xác nhận!");
            resp.sendRedirect(req.getContextPath() + "/verify-email-otp");
        } else {
            req.setAttribute("errorMessage", "Không thể gửi mã OTP tới email mới. Vui lòng kiểm tra lại cấu hình email!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
        }
    }

    /**
     * Xác thực OTP đổi email trực tiếp tại Profile.
     */
    private void handleVerifyEmailOtp(HttpServletRequest req, HttpServletResponse resp, User currentUser)
            throws ServletException, IOException {
        String otp = req.getParameter("otp");
        if (otp == null || otp.isBlank() || !otp.trim().matches("^[0-9]{6}$")) {
            req.setAttribute("errorMessage", "Mã OTP phải gồm đúng 6 chữ số!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
            return;
        }

        boolean verified = userService.verifyChangeEmailOtp(currentUser.getId(), otp.trim());
        if (verified) {
            User fresh = userService.findById(currentUser.getId());
            req.getSession().setAttribute("currentUser", fresh);
            req.getSession().setAttribute("successMessage", "Thay đổi địa chỉ email thành công! Email mới: " + fresh.getEmail());
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            req.setAttribute("errorMessage", "Mã OTP không chính xác hoặc đã hết hạn!");
            req.setAttribute("user", currentUser);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
        }
    }
}

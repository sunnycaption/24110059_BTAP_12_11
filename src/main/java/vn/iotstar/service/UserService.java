package vn.iotstar.service;

import vn.iotstar.entity.User;

/**
 * Interface tầng Business Logic (Service) xử lý toàn bộ nghiệp vụ người dùng:
 * Xác thực, Đăng ký, Mã hóa BCrypt, Gửi/Xác thực OTP Email, Quên & Đặt lại mật khẩu.
 */
public interface UserService {
    // Đăng nhập người dùng (kiểm tra username/email và mật khẩu BCrypt)
    User login(String usernameOrEmail, String password);

    // Đăng ký tài khoản mới: tạo tài khoản chưa kích hoạt (status = false), sinh OTP và gửi email
    boolean register(String username, String password, String fullname, String email);

    // Xác thực mã OTP để kích hoạt tài khoản
    boolean verifyOtp(String email, String otp);

    // Gửi lại mã OTP qua email
    boolean resendOtp(String email, String purpose);

    // Yêu cầu quên mật khẩu: kiểm tra email và gửi OTP đặt lại mật khẩu
    boolean forgotPassword(String email);

    // Đặt lại mật khẩu mới sau khi xác thực đúng OTP
    boolean resetPassword(String email, String otp, String newPassword);

    // Kiểm tra username đã tồn tại
    boolean checkExistUsername(String username);

    // Kiểm tra email đã tồn tại
    boolean checkExistEmail(String email);

    // Tìm kiếm User theo username
    User findByUsername(String username);

    // Tìm kiếm User theo email
    User findByEmail(String email);

    // Tìm kiếm User theo username hoặc email
    User findByUsernameOrEmail(String identifier);

    // Cập nhật thông tin User
    void update(User user);
}

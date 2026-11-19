package vn.iotstar.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import vn.iotstar.dao.UserDAO;
import vn.iotstar.dao.impl.UserDAOImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.utils.EmailUtil;
import vn.iotstar.utils.OtpUtil;

import java.util.Date;

/**
 * Triển khai nghiệp vụ UserService.
 * Tích hợp mã hóa BCrypt, quản lý trạng thái tài khoản và gửi mã OTP qua Gmail.
 */
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User login(String identifier, String password) {
        if (identifier == null || password == null || identifier.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }

        User user = findByUsernameOrEmail(identifier.trim());
        if (user == null) {
            return null; // Không tìm thấy tài khoản
        }

        // Kiểm tra khớp mật khẩu sử dụng BCrypt
        boolean passwordMatches = false;
        try {
            passwordMatches = BCrypt.checkpw(password, user.getPassword());
        } catch (Exception e) {
            // Trường hợp dữ liệu mật khẩu cũ lưu plain-text chưa hash
            if (password.equals(user.getPassword())) {
                passwordMatches = true;
                // Nâng cấp tự động mật khẩu sang dạng hash BCrypt
                user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));
                userDAO.update(user);
            }
        }

        if (passwordMatches) {
            return user;
        }

        return null; // Sai mật khẩu
    }

    @Override
    public boolean register(String username, String password, String fullname, String email) {
        if (username == null || password == null || fullname == null || email == null) {
            return false;
        }

        username = username.trim();
        email = email.trim();
        fullname = fullname.trim();

        // 1. Kiểm tra trùng lặp
        if (userDAO.checkExistUsername(username) || userDAO.checkExistEmail(email)) {
            return false;
        }

        // 2. Hash mật khẩu bằng BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // 3. Sinh mã OTP 6 số và thời gian hết hạn (5 phút)
        String otp = OtpUtil.generateOtp();
        Date otpExpiry = OtpUtil.generateExpiryTime(OtpUtil.OTP_EXPIRY_MINUTES);

        // 4. Lưu User với status = false (chưa kích hoạt)
        User newUser = new User(username, hashedPassword, fullname, email, false, otp, otpExpiry);
        userDAO.insert(newUser);

        // 5. Gửi email chứa mã OTP kích hoạt tài khoản
        EmailUtil.sendOtpEmail(email, otp, "Kích hoạt tài khoản");

        return true;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        if (email == null || otp == null) {
            return false;
        }

        User user = userDAO.findByEmail(email.trim());
        if (user == null) {
            return false;
        }

        // Kiểm tra mã OTP khớp và chưa hết hạn
        if (otp.trim().equals(user.getCode())) {
            if (!OtpUtil.isExpired(user.getOtpExpiry())) {
                // OTP hợp lệ -> Kích hoạt tài khoản
                user.setStatus(true);
                user.setCode(null);
                user.setOtpExpiry(null);
                userDAO.update(user);
                return true;
            } else {
                System.err.println("❌ [UserService] Mã OTP của email " + email + " đã hết hạn!");
            }
        }
        return false;
    }

    @Override
    public boolean resendOtp(String email, String purpose) {
        if (email == null) return false;

        User user = userDAO.findByEmail(email.trim());
        if (user == null) {
            return false;
        }

        String newOtp = OtpUtil.generateOtp();
        Date newExpiry = OtpUtil.generateExpiryTime(OtpUtil.OTP_EXPIRY_MINUTES);

        user.setCode(newOtp);
        user.setOtpExpiry(newExpiry);
        userDAO.update(user);

        return EmailUtil.sendOtpEmail(user.getEmail(), newOtp, purpose != null ? purpose : "Xác thực tài khoản");
    }

    @Override
    public boolean forgotPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        User user = userDAO.findByEmail(email.trim());
        if (user == null) {
            return false; // Email không tồn tại trong hệ thống
        }

        // Sinh mã OTP mới để đặt lại mật khẩu
        String otp = OtpUtil.generateOtp();
        Date expiry = OtpUtil.generateExpiryTime(OtpUtil.OTP_EXPIRY_MINUTES);

        user.setCode(otp);
        user.setOtpExpiry(expiry);
        userDAO.update(user);

        // Gửi email OTP
        EmailUtil.sendOtpEmail(user.getEmail(), otp, "Đặt lại mật khẩu");
        return true;
    }

    @Override
    public boolean resetPassword(String email, String otp, String newPassword) {
        if (email == null || otp == null || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }

        User user = userDAO.findByEmail(email.trim());
        if (user == null) {
            return false;
        }

        // Xác thực OTP
        if (otp.trim().equals(user.getCode())) {
            if (!OtpUtil.isExpired(user.getOtpExpiry())) {
                // Mã hóa mật khẩu mới bằng BCrypt
                String hashedNewPassword = BCrypt.hashpw(newPassword.trim(), BCrypt.gensalt(12));
                user.setPassword(hashedNewPassword);
                user.setCode(null);
                user.setOtpExpiry(null);
                user.setStatus(true); // Đã xác thực thành công qua email
                userDAO.update(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkExistUsername(String username) {
        if (username == null) return false;
        return userDAO.checkExistUsername(username.trim());
    }

    @Override
    public boolean checkExistEmail(String email) {
        if (email == null) return false;
        return userDAO.checkExistEmail(email.trim());
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) return null;
        return userDAO.findByUsername(username.trim());
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) return null;
        return userDAO.findByEmail(email.trim());
    }

    @Override
    public User findByUsernameOrEmail(String identifier) {
        if (identifier == null) return null;
        identifier = identifier.trim();
        User user = userDAO.findByUsername(identifier);
        if (user == null) {
            user = userDAO.findByEmail(identifier);
        }
        return user;
    }

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public boolean updateProfile(int userId, String fullname, String phone, String images) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }
        if (fullname != null && !fullname.isBlank()) {
            user.setFullname(fullname.trim());
        }
        if (phone != null) {
            user.setPhone(phone.trim());
        }
        if (images != null && !images.isBlank()) {
            user.setImages(images.trim());
        }
        userDAO.update(user);
        return true;
    }

    @Override
    public boolean requestChangeEmail(int userId, String newEmail) {
        if (newEmail == null || newEmail.isBlank()) {
            return false;
        }
        newEmail = newEmail.trim();

        // Kiểm tra xem email mới có bị tài khoản khác sử dụng chưa
        User existingUser = userDAO.findByEmail(newEmail);
        if (existingUser != null && existingUser.getId() != userId) {
            return false;
        }

        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }

        // Sinh mã OTP 6 số
        String otp = OtpUtil.generateOtp();
        Date expiry = OtpUtil.generateExpiryTime(OtpUtil.OTP_EXPIRY_MINUTES);

        user.setPendingEmail(newEmail);
        user.setCode(otp);
        user.setOtpExpiry(expiry);
        userDAO.update(user);

        // Gửi OTP tới địa chỉ email mới
        return EmailUtil.sendOtpEmail(newEmail, otp, "Xác nhận thay đổi địa chỉ Email");
    }

    @Override
    public boolean verifyChangeEmailOtp(int userId, String otp) {
        if (otp == null || otp.isBlank()) {
            return false;
        }
        User user = userDAO.findById(userId);
        if (user == null || user.getPendingEmail() == null) {
            return false;
        }

        if (otp.trim().equals(user.getCode())) {
            if (!OtpUtil.isExpired(user.getOtpExpiry())) {
                user.setEmail(user.getPendingEmail());
                user.setPendingEmail(null);
                user.setCode(null);
                user.setOtpExpiry(null);
                userDAO.update(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(User user) {
        if (user != null) {
            userDAO.update(user);
        }
    }
}

package vn.iotstar.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import vn.iotstar.dao.UserDAO;
import vn.iotstar.dao.impl.UserDAOImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;

/**
 * Triển khai nghiệp vụ UserService.
 * Tích hợp mã hóa bảo mật BCrypt cho mật khẩu.
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
    public User login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }

        // Tìm User theo username
        User user = userDAO.findByUsername(username.trim());
        if (user == null) {
            return null; // Không tìm thấy tài khoản
        }

        // Kiểm tra khớp mật khẩu sử dụng BCrypt
        try {
            if (BCrypt.checkpw(password, user.getPassword())) {
                return user; // Đăng nhập thành công
            }
        } catch (Exception e) {
            // Trường hợp dữ liệu mật khẩu cũ không đúng định dạng BCrypt
            if (password.equals(user.getPassword())) {
                return user;
            }
        }

        return null; // Sai mật khẩu
    }

    @Override
    public boolean register(String username, String password, String fullname, String email) {
        // Kiểm tra dữ liệu đầu vào cơ bản
        if (username == null || password == null || fullname == null || email == null) {
            return false;
        }

        username = username.trim();
        email = email.trim();
        fullname = fullname.trim();

        // 1. Kiểm tra username đã tồn tại
        if (userDAO.checkExistUsername(username)) {
            return false;
        }

        // 2. Kiểm tra email đã tồn tại
        if (userDAO.checkExistEmail(email)) {
            return false;
        }

        // 3. Mã hóa mật khẩu bằng BCrypt (salt factor 12)
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // 4. Tạo đối tượng User và lưu vào cơ sở dữ liệu qua DAO
        User newUser = new User(username, hashedPassword, fullname, email);
        userDAO.insert(newUser);

        return true;
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
}

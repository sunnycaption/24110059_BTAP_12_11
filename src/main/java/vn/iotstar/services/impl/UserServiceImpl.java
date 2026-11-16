package vn.iotstar.services.impl;

import java.sql.Date;

import vn.iotstar.daos.UserDao;
import vn.iotstar.daos.impl.UserDaoImpl;
import vn.iotstar.models.User;
import vn.iotstar.services.UserService;

/**
 * Lớp xử lý nghiệp vụ người dùng (Service Layer) có bổ sung log debug.
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    /**
     * Hàm login:
     * - Lấy user từ UserDao.get.
     * - In log kiểm tra user found.
     * - So sánh mật khẩu bằng .equals() tuyệt đối.
     */
    @Override
    public User login(String username, String password) {
        User user = this.get(username);

        // DEBUG LOG
        System.out.println(">>> [UserServiceImpl] User found in DB: " + (user != null));

        if (user != null) {
            String dbPassword = user.getPassword();
            System.out.println(">>> [UserServiceImpl] So sánh mật khẩu: Nhập vào = [" + password + "] vs Trong DB = [" + dbPassword + "]");

            // So sánh mật khẩu tuyệt đối bằng phương thức .equals()
            if (password != null && password.equals(dbPassword)) {
                System.out.println(">>> [UserServiceImpl] Mật khẩu KHỚP!");
                return user;
            } else {
                System.out.println(">>> [UserServiceImpl] Mật khẩu KHÔNG KHỚP!");
            }
        } else {
            System.out.println(">>> [UserServiceImpl] Không tìm thấy user để so sánh mật khẩu.");
        }

        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (this.checkExistUsername(username)) {
            return false;
        }

        long millis = System.currentTimeMillis();
        Date createdDate = new Date(millis);

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setFullname(fullname);
        user.setPassword(password);
        user.setAvatar(null);
        user.setRoleid(5);
        user.setPhone(phone);
        user.setCreateddate(createdDate);

        userDao.insert(user);
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }
}

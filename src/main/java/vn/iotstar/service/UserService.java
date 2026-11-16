package vn.iotstar.service;

import vn.iotstar.entity.User;

/**
 * Interface tầng Business Logic (Service) xử lý nghiệp vụ người dùng.
 */
public interface UserService {
    // Xử lý nghiệp vụ đăng nhập (kiểm tra username và mật khẩu đã mã hóa BCrypt)
    User login(String username, String password);

    // Xử lý nghiệp vụ đăng ký tài khoản mới (kiểm tra trùng lặp và mã hóa mật khẩu)
    boolean register(String username, String password, String fullname, String email);

    // Kiểm tra username đã tồn tại
    boolean checkExistUsername(String username);

    // Kiểm tra email đã tồn tại
    boolean checkExistEmail(String email);

    // Tìm kiếm User theo username
    User findByUsername(String username);
}

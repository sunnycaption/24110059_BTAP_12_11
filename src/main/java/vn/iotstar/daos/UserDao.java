package vn.iotstar.daos;

import vn.iotstar.models.User;

/**
 * Interface UserDao định nghĩa các phương thức thao tác JDBC với bảng [User].
 */
public interface UserDao {
    /**
     * Lấy thông tin người dùng dựa trên tên đăng nhập.
     * @param username Tên đăng nhập
     * @return Đối tượng User nếu tìm thấy, ngược lại trả về null
     */
    User get(String username);

    /**
     * Thêm mới một người dùng vào cơ sở dữ liệu.
     * @param user Đối tượng User cần thêm
     */
    void insert(User user);

    /**
     * Kiểm tra email đã tồn tại trong database hay chưa.
     * @param email Địa chỉ email cần kiểm tra
     * @return true nếu đã tồn tại, ngược lại false
     */
    boolean checkExistEmail(String email);

    /**
     * Kiểm tra username đã tồn tại trong database hay chưa.
     * @param username Tên đăng nhập cần kiểm tra
     * @return true nếu đã tồn tại, ngược lại false
     */
    boolean checkExistUsername(String username);
}

package vn.iotstar.daos.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import vn.iotstar.configs.DBConnection;
import vn.iotstar.daos.UserDao;
import vn.iotstar.models.User;

/**
 * Lớp triển khai UserDao sử dụng JDBC thuần kết nối SQL Server.
 * Đã bổ sung cơ chế bắt lỗi an toàn chống văng Exception khi cột bị NULL.
 */
public class UserDaoImpl implements UserDao {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Lấy thông tin người dùng theo username.
     * Dùng: SELECT * FROM [User] WHERE username = ?
     */
    @Override
    public User get(String username) {
        String sql = "SELECT * FROM [User] WHERE username = ?";
        System.out.println(">>> [UserDaoImpl] Executing SQL for user: " + username);

        try {
            conn = new DBConnection().getConnection();
            if (conn == null) {
                System.err.println("❌ [UserDaoImpl] Lỗi nghiêm trọng: Connection lấy từ DBConnection bị NULL! Vui lòng kiểm tra lại kết nối SQL Server.");
                return null;
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();

                // Map an toàn chống lỗi NullPointerException khi cột NULL
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setFullname(rs.getString("fullname"));
                user.setPassword(rs.getString("password"));

                // Xử lý an toàn cột avatar (cho phép null)
                String avatar = rs.getString("avatar");
                user.setAvatar(avatar != null ? avatar : "");

                // Xử lý an toàn cột roleid (mặc định là 5 nếu null)
                int roleid = rs.getInt("roleid");
                if (rs.wasNull()) {
                    roleid = 5;
                }
                user.setRoleid(roleid);

                // Xử lý an toàn cột phone (cho phép null)
                String phone = rs.getString("phone");
                user.setPhone(phone != null ? phone : "");

                // Xử lý an toàn cột createddate (cho phép null)
                Date createdDate = rs.getDate("createddate");
                user.setCreateddate(createdDate);

                System.out.println(">>> [UserDaoImpl] Đã tìm thấy User trong Database: " + user.getUsername() + " | Password trong DB: " + user.getPassword());
                return user;
            } else {
                System.out.println(">>> [UserDaoImpl] Không tìm thấy bản ghi nào với username: [" + username + "]");
            }
        } catch (Exception e) {
            System.err.println("❌ [UserDaoImpl] Lỗi DAO get User: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    /**
     * Chèn mới User vào database.
     */
    @Override
    public void insert(User user) {
        String sql = "INSERT INTO [User](email, username, fullname, password, avatar, roleid, phone, createddate) VALUES (?,?,?,?,?,?,?,?)";
        try {
            conn = new DBConnection().getConnection();
            if (conn == null) {
                System.err.println("❌ [UserDaoImpl] Lỗi: Connection bị NULL khi insert!");
                return;
            }
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getAvatar());
            ps.setInt(6, user.getRoleid());
            ps.setString(7, user.getPhone());
            ps.setDate(8, user.getCreateddate());
            ps.executeUpdate();
            System.out.println(">>> [UserDaoImpl] Insert thành công user: " + user.getUsername());
        } catch (Exception e) {
            System.err.println("❌ [UserDaoImpl] Lỗi DAO insert: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    /**
     * Kiểm tra trùng email.
     */
    @Override
    public boolean checkExistEmail(String email) {
        String sql = "SELECT * FROM [User] WHERE email = ?";
        try {
            conn = new DBConnection().getConnection();
            if (conn == null) return false;
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ [UserDaoImpl] Lỗi DAO checkExistEmail: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    /**
     * Kiểm tra trùng username.
     */
    @Override
    public boolean checkExistUsername(String username) {
        String sql = "SELECT * FROM [User] WHERE username = ?";
        try {
            conn = new DBConnection().getConnection();
            if (conn == null) return false;
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.err.println("❌ [UserDaoImpl] Lỗi DAO checkExistUsername: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

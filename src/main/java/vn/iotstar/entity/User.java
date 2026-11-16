package vn.iotstar.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity User ánh xạ tới bảng Users trong database JavaWebMVC.
 * Sử dụng Jakarta Persistence API.
 */
@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "fullname", nullable = false, length = 255)
    private String fullname;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "status")
    private boolean status = false; // 0 = Chưa kích hoạt, 1 = Đã kích hoạt

    @Column(name = "code", length = 10)
    private String code; // Mã OTP 6 số

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "otpExpiry")
    private Date otpExpiry; // Thời gian hết hạn OTP

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", insertable = false, updatable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Video> videos = new ArrayList<>();

    // Default constructor
    public User() {
    }

    // Constructor phục vụ đăng ký tài khoản mới
    public User(String username, String password, String fullname, String email) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.status = false;
        this.createdAt = new Date();
    }

    // Constructor có status và OTP
    public User(String username, String password, String fullname, String email, boolean status, String code, Date otpExpiry) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.status = status;
        this.code = code;
        this.otpExpiry = otpExpiry;
        this.createdAt = new Date();
    }

    // Full constructor
    public User(int id, String username, String password, String fullname, String email, boolean status, String code, Date otpExpiry, Date createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.status = status;
        this.code = code;
        this.otpExpiry = otpExpiry;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(Date otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", otpExpiry=" + otpExpiry +
                ", createdAt=" + createdAt +
                '}';
    }
}

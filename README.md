# Dự Án Java Web MVC JPA - Xác Thực OTP & Quản Lý Video

**Họ và tên: Lê Thị Thanh Thúy
**Mã số SV:** 24110059  
**Repository GitHub:** https://github.com/sunnycaption/24110059_BTAP_12_11  

---

## 1. Công Nghệ & Kiến Trúc Sử Dụng

- **Ngôn ngữ & Nền tảng:** Java 21, Jakarta EE 10 / Servlet 6.0, JSP 3.1, JSTL 3.0.
- **ORM / Persistence:** Hibernate 7.4.6.Final (JPA 3.0) kết nối Microsoft SQL Server.
- **Bảo mật:** Mã hóa mật khẩu an toàn với thư viện **jBCrypt**.
- **Gửi Email:** Jakarta Mail API (`jakarta.mail-api:2.1.3`) & Angus Mail (`angus-mail:2.0.3`) qua SMTP Gmail.
- **Upload File:** Chuẩn **Jakarta Servlet Multipart** (`@MultipartConfig`).
- **Quản lý dependencies:** Apache Maven.

---

## 2. Cài Đặt Cơ Sở Dữ Liệu (SQL Server)

1. Mở **SQL Server Management Studio (SSMS)**.
2. Mở file `database.sql` trong thư mục gốc dự án và bấm **Execute**.
3. Database `JavaWebMVC` sẽ được tạo kèm các bảng:
   - `Users`: Quản lý tài khoản, mật khẩu hash BCrypt, trạng thái `status` (0: Chưa kích hoạt, 1: Đã kích hoạt), mã `code` (OTP 6 số) và thời hạn `otpExpiry`.
   - `Videos`: Quản lý video với khóa chính tự tăng `videoId INT IDENTITY(1,1)`, `title`, `poster` (ảnh bìa), `videoFile` (file video .mp4), `views` (lượt xem), `description`, `active`, `createdDate` (ngày tạo), và khóa ngoại `userId` tham chiếu `Users(id)`.
4. Tài khoản mẫu có sẵn (mật khẩu mặc định: `123456`):
   - `admin` / `123456` (Email: `admin@iotstar.vn`, Status: 1 - Đã kích hoạt)
   - `user01` / `123456` (Email: `user01@iotstar.vn`, Status: 1 - Đã kích hoạt)
   - `user02` / `123456` (Email: `user02@iotstar.vn`, Status: 0 - Chưa kích hoạt OTP)

---

## 3. Các Chức Năng Chính Đã Hoàn Thành

### PHẦN 1: Xác Thực & Bảo Mật (Đã hoàn thành)
- **Đăng ký tài khoản (`/register`)**: Lưu tài khoản ở trạng thái chưa kích hoạt (`status = 0`), sinh ngẫu nhiên mã OTP 6 số (hạn dùng 5 phút), gửi OTP về email và chuyển sang trang `/verify-otp`.
- **Kích hoạt OTP (`/verify-otp`)**: Nhập đúng mã OTP để kích hoạt tài khoản (`status = 1`), hỗ trợ nút gửi lại mã OTP mới.
- **Đăng nhập (`/login`)**: Kiểm tra mật khẩu mã hóa **BCrypt** và kiểm tra trạng thái kích hoạt tài khoản. Nếu chưa kích hoạt, cảnh báo và điều hướng sang trang xác thực OTP.
- **Đăng xuất (`/logout`)**: Hủy `HttpSession`, xóa sạch cookie Remember me và đưa về trang đăng nhập.
- **Quên mật khẩu (`/forgot-password`) & Đặt lại mật khẩu (`/reset-password`)**: Nhập email $\rightarrow$ Gửi mã OTP $\rightarrow$ Xác thực OTP & nhập mật khẩu mới $\rightarrow$ Hash BCrypt và cập nhật database.

### PHẦN 2: Quản Lý Video & Upload Multipart (Yêu Cầu 4 - Đã hoàn thành 100%)
1. **Entity `Video` (Quan hệ 1-N với `User`)**:
   - Khóa chính tự tăng `videoId` (`@GeneratedValue(strategy = GenerationType.IDENTITY)`).
   - Đầy đủ các trường: `title`, `poster`, `videoFile`, `views` (mặc định 0), `description`, `active` (mặc định true), `createdDate` (ngày tạo), và `@ManyToOne` tới `User` (`userId`).
   - Phía `User.java` có `@OneToMany(mappedBy = "user") List<Video> videos`.

2. **CRUD Videos & Upload Jakarta Servlet Multipart**:
   - Sử dụng `@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 100)`.
   - Form JSP sử dụng `enctype="multipart/form-data"` với các ô chọn file:
     - `<input type="file" name="posterFile" accept="image/*">`
     - `<input type="file" name="videoFile" accept="video/*">`
   - Servlet xử lý `request.getPart("posterFile")` và `request.getPart("videoFile")`, lưu file vật lý vào thư mục `uploads/` trong `webapp` với tên file thêm tiền tố timestamp, đồng thời lưu đường dẫn tương đối `uploads/<tên_file>` vào cơ sở dữ liệu.
   - Khi chỉnh sửa (Edit): nếu người dùng không chọn file mới thì giữ nguyên `poster` và `videoFile` cũ trong database.

3. **Hiển thị 10 video mới nhất lên trang chủ (`/home`)**:
   - Truy vấn JPA `findTop10Latest` sắp xếp theo ngày tạo mới nhất (`ORDER BY v.createdDate DESC, v.videoId DESC`, `setMaxResults(10)`).
   - Hiển thị danh sách 10 video ra giao diện dạng card với ảnh bìa `poster`, tiêu đề (click để xem video), lượt xem `views` và người đăng.

4. **Hiển thị video của user có phân trang 6 sản phẩm/trang tại `/video`**:
   - Mapping URL `/video` lấy video theo `userId` của user đang đăng nhập.
   - Phân trang JPA chuẩn: `setFirstResult((page - 1) * 6)` và `setMaxResults(6)` cố định đúng **6 video / trang**.
   - Giao diện có thanh điều hướng phân trang (Trang trước, số trang `1, 2, 3...`, Trang sau) và các nút thao tác Thêm / Sửa / Xóa.

5. **Hiển thị chi tiết & Phát video HTML5 Player**:
   - Bấm vào tiêu đề bất kỳ video nào trên `/home` hoặc `/video` sẽ chuyển đến trang `/video/detail?id=...`.
   - Tự động tăng lượt xem `views + 1` và cập nhật xuống database.
   - Phát video trực tiếp bằng trình phát HTML5:
     ```html
     <video width="100%" controls autoplay>
         <source src="${pageContext.request.contextPath}/${video.videoFile}" type="video/mp4">
     </video>
     ```

---

## 4. Hướng Dẫn Khởi Chạy Kiểm Thử Nhanh

1. Mở file [database.sql](database.sql) trong SSMS và chạy **Execute**.
2. Nhấp đúp vào file [deploy_and_start.bat](deploy_and_start.bat) trong thư mục dự án để tự động đóng gói WAR và khởi chạy Tomcat 10.1.44.
3. Trình duyệt sẽ tự động mở trang web tại địa chỉ: `http://localhost:8080/UserAuthProject/login`

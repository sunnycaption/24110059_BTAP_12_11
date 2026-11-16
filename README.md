# Dự Án Java Web MVC JPA - Xác Thực OTP & Quản Lý Video

**Họ và tên:** Sinh viên HCMUTE  
**Mã số SV:** 24110059  
**Repository GitHub:** https://github.com/sunnycaption/24110059_BTAP_12_11  

---

## 1. Công Nghệ & Kiến Trúc Sử Dụng

- **Ngôn ngữ & Nền tảng:** Java 21, Jakarta EE 10 / Servlet 6.0, JSP 3.1, JSTL 3.0.
- **ORM / Persistence:** Hibernate 7.4.6.Final (JPA 3.0) kết nối Microsoft SQL Server.
- **Bảo mật:** Mã hóa mật khẩu an toàn với thư viện **jBCrypt**.
- **Gửi Email:** Jakarta Mail API (`jakarta.mail-api:2.1.3`) & Angus Mail (`angus-mail:2.0.3`) qua SMTP Gmail.
- **Quản lý dependencies:** Apache Maven.

---

## 2. Hướng Dẫn Cấu Hình Email SMTP (Gmail App Password)

Để gửi mã OTP thực tế về hòm thư Gmail của người dùng:
1. Mở file: `src/main/resources/email.properties`
2. Cập nhật các thông số:
   ```properties
   mail.smtp.host=smtp.gmail.com
   mail.smtp.port=587
   mail.smtp.auth=true
   mail.smtp.starttls.enable=true
   mail.smtp.ssl.protocols=TLSv1.2

   # Điền tài khoản Gmail của bạn
   mail.username=your_email@gmail.com

   # Điền mật khẩu ứng dụng 16 ký tự (Google App Password)
   mail.password=xxxx xxxx xxxx xxxx

   mail.from.name=Hệ thống Xác thực Java Web - 24110059
   ```
> **Lưu ý hỗ trợ dev/test local:** Khi chạy test nội bộ trên máy chưa cấu hình App Password, hệ thống được tích hợp cơ chế tự động in mã OTP ra Terminal/Console (`⚡ [EmailUtil] MÃ OTP TRONG EMAIL LÀ: >>> 123456 <<<`), giúp bạn có thể nhập mã xác thực ngay lập tức mà không bị gián đoạn.

---

## 3. Cài Đặt Cơ Sở Dữ Liệu (SQL Server)

1. Mở **SQL Server Management Studio (SSMS)**.
2. Mở file `database.sql` trong thư mục gốc dự án và bấm **Execute**.
3. Database `JavaWebMVC` sẽ được tạo kèm các bảng:
   - `Users`: Quản lý tài khoản, mật khẩu hash BCrypt, trạng thái `status` (0: Chưa kích hoạt, 1: Đã kích hoạt), mã `code` (OTP) và thời hạn `otpExpiry`.
   - `Videos`: Quản lý danh sách video, poster, videoPath, views, active, khóa ngoại `userId` tham chiếu `Users(id)`.
4. Tài khoản mẫu có sẵn (mật khẩu mặc định: `123456`):
   - `admin` / `123456` (Email: `admin@iotstar.vn`, Status: 1 - Đã kích hoạt)
   - `user01` / `123456` (Email: `user01@iotstar.vn`, Status: 1 - Đã kích hoạt)
   - `user02` / `123456` (Email: `user02@iotstar.vn`, Status: 0 - Chưa kích hoạt OTP)

---

## 4. Các Chức Năng Chính

### PHẦN 1: Xác Thực & Bảo Mật
- **Đăng ký tài khoản (`/register`)**: Lưu tài khoản ở trạng thái chưa kích hoạt (`status = 0`), sinh ngẫu nhiên mã OTP 6 số (hạn dùng 5 phút), gửi OTP về email và chuyển sang trang `/verify-otp`.
- **Kích hoạt OTP (`/verify-otp`)**: Nhập đúng mã OTP để kích hoạt tài khoản (`status = 1`), hỗ trợ nút gửi lại mã OTP mới.
- **Đăng nhập (`/login`)**: Kiểm tra mật khẩu mã hóa **BCrypt** và kiểm tra trạng thái kích hoạt tài khoản. Nếu chưa kích hoạt, cảnh báo và điều hướng sang trang xác thực OTP.
- **Đăng xuất (`/logout`)**: Hủy `HttpSession`, xóa sạch cookie Remember me và đưa về trang đăng nhập.
- **Quên mật khẩu (`/forgot-password`) & Đặt lại mật khẩu (`/reset-password`)**: Nhập email $\rightarrow$ Gửi mã OTP $\rightarrow$ Xác thực OTP & nhập mật khẩu mới $\rightarrow$ Hash BCrypt và cập nhật database.

### PHẦN 2: Quản Lý Video & Upload Multipart
- **Entity `Video` (Quan hệ 1-N với `User`)**: Đầy đủ thuộc tính `videoId`, `title`, `poster`, `videoPath`, `views`, `description`, `active`, `userId`.
- **CRUD Video**: Thêm mới, chỉnh sửa, xóa và hiển thị danh sách video theo từng user.
- **Upload Multipart Servlet**: Sử dụng `@MultipartConfig` để tải lên file ảnh đại diện (`poster`) và file `video` vào thư mục `uploads/`.
- **Top 10 Video mới nhất (`/home`)**: Hiển thị 10 video mới nhất trên trang chủ.
- **Phân trang Video của user (`/video`)**: Hiển thị danh sách video của user với phân trang chuẩn **6 sản phẩm/trang**.
- **Xem chi tiết & Player HTML5 (`/video/detail` hoặc `/watch`)**: Phát video bằng thẻ `<video controls>`, tự động tăng lượt xem (`views + 1`).

-- =======================================================
-- SCRIPT TẠO DATABASE VÀ CÁC BẢNG CHO DỰ ÁN JAVA WEB MVC (JPA)
-- Hỗ trợ SQL Server 2017+ / 2019 / 2022
-- =======================================================

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'JavaWebMVC')
BEGIN
    CREATE DATABASE [JavaWebMVC];
END
GO

USE [JavaWebMVC];
GO

-- 1. Bảng Users (Xác thực tài khoản, OTP kích hoạt & bảo mật)
IF OBJECT_ID('dbo.Videos', 'U') IS NOT NULL DROP TABLE dbo.Videos;
IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL DROP TABLE dbo.Users;
GO

CREATE TABLE [Users] (
    [id] INT IDENTITY(1,1) PRIMARY KEY,
    [username] VARCHAR(100) NOT NULL UNIQUE,
    [password] VARCHAR(255) NOT NULL,
    [fullname] NVARCHAR(255) NOT NULL,
    [email] VARCHAR(255) NOT NULL UNIQUE,
    [status] BIT NOT NULL DEFAULT 0, -- 0: Chưa kích hoạt, 1: Đã kích hoạt OTP
    [code] VARCHAR(10) NULL,         -- Mã OTP 6 số
    [otpExpiry] DATETIME NULL,       -- Thời hạn hết hạn OTP
    [createdAt] DATETIME NOT NULL DEFAULT GETDATE()
);
GO

-- 2. Bảng Videos (Quản lý Video, Upload Multipart, Quan hệ 1-N với Users)
CREATE TABLE [Videos] (
    [videoId] VARCHAR(50) PRIMARY KEY,
    [title] NVARCHAR(255) NOT NULL,
    [poster] NVARCHAR(255) NULL,
    [videoPath] NVARCHAR(500) NULL,
    [views] INT NOT NULL DEFAULT 0,
    [description] NVARCHAR(MAX) NULL,
    [active] BIT NOT NULL DEFAULT 1,
    [userId] INT NOT NULL,
    [categoryId] INT NULL,
    CONSTRAINT FK_Videos_Users FOREIGN KEY ([userId]) REFERENCES [Users]([id]) ON DELETE CASCADE
);
GO

-- 3. Chèn dữ liệu mẫu Users
-- Mật khẩu mã hóa BCrypt cho "123456" là: $2a$12$K1dYh8K6lWfR9v.8vUa1CeW0V2zM7XwQd1Q3n2L7K9E8P4j3X5fOy
INSERT INTO [Users] ([username], [password], [fullname], [email], [status], [code], [otpExpiry], [createdAt])
VALUES 
('admin', '$2a$12$K1dYh8K6lWfR9v.8vUa1CeW0V2zM7XwQd1Q3n2L7K9E8P4j3X5fOy', N'Quản Trị Viên (Admin)', 'admin@iotstar.vn', 1, NULL, NULL, GETDATE()),
('user01', '$2a$12$K1dYh8K6lWfR9v.8vUa1CeW0V2zM7XwQd1Q3n2L7K9E8P4j3X5fOy', N'Nguyễn Văn A', 'user01@iotstar.vn', 1, NULL, NULL, GETDATE()),
('user02', '$2a$12$K1dYh8K6lWfR9v.8vUa1CeW0V2zM7XwQd1Q3n2L7K9E8P4j3X5fOy', N'Trần Thị B', 'user02@iotstar.vn', 0, '123456', DATEADD(MINUTE, 30, GETDATE()), GETDATE());
GO

-- 4. Chèn dữ liệu mẫu Videos
INSERT INTO [Videos] ([videoId], [title], [poster], [videoPath], [views], [description], [active], [userId])
VALUES
('VID001', N'Hướng dẫn lập trình Java Web với Servlet & JSP', 'sample_poster1.jpg', 'sample_video1.mp4', 150, N'Khóa học Java Web cơ bản và nâng cao từ A đến Z dành cho sinh viên CNTT.', 1, 1),
('VID002', N'Xây dựng ứng dụng MVC 3 tầng với Hibernate JPA', 'sample_poster2.jpg', 'sample_video2.mp4', 320, N'Hướng dẫn chi tiết tích hợp JPA/Hibernate vào dự án Maven.', 1, 1),
('VID003', N'Kích hoạt tài khoản bằng OTP qua Gmail SMTP', 'sample_poster3.jpg', 'sample_video3.mp4', 85, N'Bảo mật ứng dụng web với mã xác nhận OTP qua email.', 1, 1),
('VID004', N'Mã hóa mật khẩu an toàn với BCrypt trong Java', 'sample_poster1.jpg', 'sample_video1.mp4', 210, N'Cách sử dụng thư viện jBCrypt để hash mật khẩu an toàn.', 1, 1),
('VID005', N'Upload file bằng Multipart Servlet trong Jakarta EE', 'sample_poster2.jpg', 'sample_video2.mp4', 95, N'Sử dụng annotation @MultipartConfig để tải lên file video và poster.', 1, 1),
('VID006', N'Phân trang dữ liệu Pagination 6 items/trang với JPA', 'sample_poster3.jpg', 'sample_video3.mp4', 180, N'Kỹ thuật phân trang chuẩn bằng setFirstResult và setMaxResults.', 1, 1),
('VID007', N'Thiết kế giao diện Bootstrap 5 hiện đại cho Web App', 'sample_poster1.jpg', 'sample_video1.mp4', 400, N'Hướng dẫn xây dựng UI đẹp mắt, responsive trên thiết bị di động.', 1, 1),
('VID008', N'Lập trình RESTful API với Jakarta EE', 'sample_poster2.jpg', 'sample_video2.mp4', 120, N'Tạo API chuẩn REST và trả về JSON.', 1, 2),
('VID009', N'Tối ưu hóa truy vấn SQL Server cho Website lớn', 'sample_poster3.jpg', 'sample_video3.mp4', 250, N'Các kỹ thuật đánh index và tinh chỉnh query.', 1, 2),
('VID010', N'Deploy ứng dụng Java Web lên Apache Tomcat 10', 'sample_poster1.jpg', 'sample_video1.mp4', 310, N'Hướng dẫn đóng gói WAR và triển khai trên Tomcat Server.', 1, 2),
('VID011', N'Video bổ sung kiểm tra phân trang trang 2', 'sample_poster2.jpg', 'sample_video2.mp4', 45, N'Video kiểm tra phân trang tự động vượt quá 6 video.', 1, 1),
('VID012', N'Video số 12 kiểm thử phân trang đầy đủ', 'sample_poster3.jpg', 'sample_video3.mp4', 60, N'Video số 12 phục vụ kiểm thử phân trang 6 sản phẩm/trang.', 1, 1);
GO

-- Kiểm tra kết quả
SELECT * FROM [Users];
SELECT * FROM [Videos];
GO

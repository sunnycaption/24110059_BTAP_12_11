<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản - Java Web MVC JPA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="header-title">
            <h2>Đăng Ký Tài Khoản</h2>
            <p>Hệ thống xác thực người dùng MVC 3 tầng (JPA + Hibernate)</p>
        </div>

        <%-- Hiển thị thông báo lỗi --%>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập:</label>
                <input type="text" id="username" name="username" class="form-control" 
                       value="${username != null ? username : ''}" 
                       placeholder="Ví dụ: nguyenvana" required autofocus>
            </div>

            <div class="form-group">
                <label for="fullname">Họ và tên:</label>
                <input type="text" id="fullname" name="fullname" class="form-control" 
                       value="${fullname != null ? fullname : ''}" 
                       placeholder="Ví dụ: Nguyễn Văn A" required>
            </div>

            <div class="form-group">
                <label for="email">Địa chỉ Email:</label>
                <input type="email" id="email" name="email" class="form-control" 
                       value="${email != null ? email : ''}" 
                       placeholder="Ví dụ: nguyenvana@example.com" required>
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" class="form-control" 
                       placeholder="Tối thiểu 6 ký tự" required>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Xác nhận mật khẩu:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" 
                       placeholder="Nhập lại mật khẩu" required>
            </div>

            <button type="submit" class="btn btn-primary">Đăng Ký</button>
        </form>

        <div class="auth-footer">
            Đã có tài khoản? 
            <a href="${pageContext.request.contextPath}/login">Đăng nhập tại đây</a>
        </div>
    </div>
</body>
</html>

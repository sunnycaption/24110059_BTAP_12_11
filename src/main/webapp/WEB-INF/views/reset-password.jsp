<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - Java Web MVC JPA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="header-title">
            <h2>Đặt Lại Mật Khẩu</h2>
            <p>Nhập mã xác thực OTP từ Email và thiết lập mật khẩu mới</p>
        </div>

        <c:if test="${not empty infoMessage}">
            <div class="alert alert-info">
                ${infoMessage}
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/reset-password" method="post">
            <div class="form-group">
                <label for="email">Địa chỉ Email:</label>
                <input type="email" id="email" name="email" class="form-control" 
                       value="${email != null ? email : ''}" 
                       placeholder="Nhập email tài khoản" required>
            </div>

            <div class="form-group">
                <label for="otp">Mã xác thực OTP (6 chữ số):</label>
                <input type="text" id="otp" name="otp" class="form-control" 
                       placeholder="Ví dụ: 123456" maxlength="6" 
                       style="text-align: center; font-size: 22px; letter-spacing: 5px; font-weight: bold; color: #667eea;" required autofocus>
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu mới:</label>
                <input type="password" id="password" name="password" class="form-control" 
                       placeholder="Tối thiểu 6 ký tự" required>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Xác nhận mật khẩu mới:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" 
                       placeholder="Nhập lại mật khẩu mới" required>
            </div>

            <button type="submit" class="btn btn-primary">Xác Nhận & Đổi Mật Khẩu</button>
        </form>

        <form action="${pageContext.request.contextPath}/reset-password" method="post" style="margin-top: 12px;">
            <input type="hidden" name="action" value="resend">
            <input type="hidden" name="email" value="${email}">
            <button type="submit" class="btn btn-secondary">Gửi lại mã OTP qua Email</button>
        </form>

        <div class="auth-footer">
            <a href="${pageContext.request.contextPath}/login">&larr; Quay lại trang Đăng nhập</a>
        </div>
    </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực mã OTP - Java Web MVC JPA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="header-title">
            <h2>Xác Thực Mã OTP</h2>
            <p>Nhập mã 6 chữ số đã được gửi qua email của bạn</p>
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

        <form action="${pageContext.request.contextPath}/verify-otp" method="post">
            <div class="form-group">
                <label for="email">Email tài khoản:</label>
                <input type="email" id="email" name="email" class="form-control" 
                       value="${email != null ? email : ''}" 
                       placeholder="Nhập email tài khoản" required>
            </div>

            <div class="form-group">
                <label for="otp">Mã xác thực OTP (6 chữ số):</label>
                <input type="text" id="otp" name="otp" class="form-control" 
                       placeholder="Ví dụ: 123456" maxlength="6" 
                       style="text-align: center; font-size: 24px; letter-spacing: 6px; font-weight: bold; color: #667eea;" required autofocus>
            </div>

            <div style="font-size: 13px; color: #718096; margin-bottom: 20px; text-align: center;">
                ⏱️ Mã OTP có hiệu lực trong vòng <strong>5 phút</strong>.
            </div>

            <button type="submit" class="btn btn-primary">Xác Thực & Kích Hoạt</button>
        </form>

        <form action="${pageContext.request.contextPath}/verify-otp" method="post" style="margin-top: 12px;">
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

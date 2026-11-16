<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Java Web MVC JPA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="auth-container">
        <div class="header-title">
            <h2>Đăng Nhập</h2>
            <p>Hệ thống xác thực người dùng & Quản lý Video (24110059)</p>
        </div>

        <%-- Hiển thị thông báo thành công --%>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
            </div>
        </c:if>

        <%-- Hiển thị thông báo lỗi --%>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">
                ${errorMessage}
            </div>
        </c:if>

        <%-- Cảnh báo tài khoản chưa kích hoạt OTP --%>
        <c:if test="${notActivated}">
            <div class="alert alert-warning" style="text-align: center;">
                <p style="margin-bottom: 8px;">Tài khoản chưa được kích hoạt mã OTP!</p>
                <a href="${pageContext.request.contextPath}/verify-otp?email=${userEmail}" class="btn btn-primary btn-sm" style="display: inline-block;">
                    Nhập mã OTP kích hoạt ngay &rarr;
                </a>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập hoặc Email:</label>
                <input type="text" id="username" name="username" class="form-control" 
                       value="${username != null ? username : ''}" 
                       placeholder="Nhập tên đăng nhập hoặc email" required autofocus>
            </div>

            <div class="form-group">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;">
                    <label for="password" style="margin-bottom: 0;">Mật khẩu:</label>
                    <a href="${pageContext.request.contextPath}/forgot-password" style="font-size: 13px; color: #667eea; text-decoration: none;">Quên mật khẩu?</a>
                </div>
                <input type="password" id="password" name="password" class="form-control" 
                       placeholder="Nhập mật khẩu" required>
            </div>

            <button type="submit" class="btn btn-primary" style="margin-top: 10px;">Đăng Nhập</button>
        </form>

        <div class="auth-footer">
            Chưa có tài khoản? 
            <a href="${pageContext.request.contextPath}/register">Đăng ký tài khoản mới</a>
        </div>
    </div>
</body>
</html>

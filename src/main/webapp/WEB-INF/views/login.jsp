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
            <p>Hệ thống xác thực người dùng MVC 3 tầng (JPA + Hibernate)</p>
        </div>

        <%-- Hiển thị thông báo lỗi nếu có --%>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <%-- Hiển thị thông báo thành công sau khi đăng ký --%>
        <% if (request.getAttribute("successMessage") != null) { %>
            <div class="alert alert-success">
                <%= request.getAttribute("successMessage") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="username">Tên đăng nhập:</label>
                <input type="text" id="username" name="username" class="form-control" 
                       value="${username != null ? username : ''}" 
                       placeholder="Nhập tên đăng nhập" required autofocus>
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" class="form-control" 
                       placeholder="Nhập mật khẩu" required>
            </div>

            <button type="submit" class="btn btn-primary">Đăng Nhập</button>
        </form>

        <div class="auth-footer">
            Chưa có tài khoản? 
            <a href="${pageContext.request.contextPath}/register">Đăng ký tài khoản mới</a>
        </div>
    </div>
</body>
</html>

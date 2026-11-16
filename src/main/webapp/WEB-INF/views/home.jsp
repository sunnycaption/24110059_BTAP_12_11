<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="vn.iotstar.entity.User" %>
<%
    User currentUser = (User) session.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ - Dashboard Người Dùng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
    <div class="dashboard-container">
        <div class="header-title">
            <h2>Xin Chào, <%= (currentUser != null) ? currentUser.getFullname() : "Người dùng" %>! 👋</h2>
            <p>Chào mừng bạn đến với ứng dụng Java Web MVC 3 Tầng sử dụng JPA và SQL Server</p>
        </div>

        <div class="user-card">
            <h3 style="margin-bottom: 16px; color: #2d3748; font-size: 18px;">Thông Tin Tài Khoản</h3>
            
            <div class="user-info-row">
                <span class="user-info-label">ID Người dùng:</span>
                <span class="user-info-value"><%= (currentUser != null) ? currentUser.getId() : "" %></span>
            </div>

            <div class="user-info-row">
                <span class="user-info-label">Tên đăng nhập:</span>
                <span class="user-info-value"><%= (currentUser != null) ? currentUser.getUsername() : "" %></span>
            </div>

            <div class="user-info-row">
                <span class="user-info-label">Họ và tên:</span>
                <span class="user-info-value"><%= (currentUser != null) ? currentUser.getFullname() : "" %></span>
            </div>

            <div class="user-info-row">
                <span class="user-info-label">Email:</span>
                <span class="user-info-value"><%= (currentUser != null) ? currentUser.getEmail() : "" %></span>
            </div>

            <div class="user-info-row">
                <span class="user-info-label">Ngày tạo tài khoản:</span>
                <span class="user-info-value"><%= (currentUser != null && currentUser.getCreatedAt() != null) ? currentUser.getCreatedAt().toString() : "Mới tạo" %></span>
            </div>
        </div>

        <div style="display: flex; gap: 12px;">
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Đăng Xuất</a>
        </div>
    </div>
</body>
</html>

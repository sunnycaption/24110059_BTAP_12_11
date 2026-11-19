<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Đăng nhập - Java Web MVC JPA</title>
</head>
<body>
    <div class="auth-brand">
        <h3><i class="bi bi-shield-lock"></i> Đăng Nhập</h3>
        <p>Hệ thống xác thực người dùng & Quản lý Video (24110059)</p>
    </div>

    <%-- Hiển thị thông báo thành công --%>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle"></i> ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <%-- Hiển thị thông báo lỗi --%>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle"></i> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <%-- Cảnh báo tài khoản chưa kích hoạt OTP --%>
    <c:if test="${notActivated}">
        <div class="alert alert-warning text-center">
            <p class="mb-2">Tài khoản chưa được kích hoạt mã OTP!</p>
            <a href="${pageContext.request.contextPath}/verify-otp?email=${userEmail}" class="btn btn-sm btn-warning">
                Nhập mã OTP kích hoạt ngay &rarr;
            </a>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="username" class="form-label">Tên đăng nhập hoặc Email</label>
            <input type="text" id="username" name="username" class="form-control"
                   value="${username != null ? username : ''}"
                   placeholder="Nhập tên đăng nhập hoặc email" required autofocus>
            <div class="invalid-feedback">Vui lòng nhập tên đăng nhập hoặc email.</div>
        </div>

        <div class="mb-3">
            <div class="d-flex justify-content-between align-items-center mb-1">
                <label for="password" class="form-label mb-0">Mật khẩu</label>
                <a href="${pageContext.request.contextPath}/forgot-password" class="text-decoration-none" style="font-size: 13px;">Quên mật khẩu?</a>
            </div>
            <input type="password" id="password" name="password" class="form-control"
                   placeholder="Nhập mật khẩu" required minlength="6">
            <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự.</div>
        </div>

        <button type="submit" class="btn btn-primary w-100 mt-2">
            <i class="bi bi-box-arrow-in-right"></i> Đăng Nhập
        </button>
    </form>

    <div class="auth-footer-text">
        Chưa có tài khoản?
        <a href="${pageContext.request.contextPath}/register">Đăng ký tài khoản mới</a>
    </div>
</body>
</html>

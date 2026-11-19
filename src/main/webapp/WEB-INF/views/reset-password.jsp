<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Đặt lại mật khẩu - Java Web MVC JPA</title>
</head>
<body>
    <div class="auth-brand">
        <h3><i class="bi bi-lock-fill"></i> Đặt Lại Mật Khẩu</h3>
        <p>Nhập mã OTP và mật khẩu mới của bạn</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle"></i> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty infoMessage}">
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            <i class="bi bi-info-circle"></i> ${infoMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/reset-password" method="post" class="needs-validation" novalidate>
        <input type="hidden" name="email" value="${email != null ? email : sessionScope.resetEmail}">

        <div class="mb-3">
            <label for="otp" class="form-label">Mã OTP (6 chữ số) <span class="text-danger">*</span></label>
            <input type="text" id="otp" name="otp" class="form-control text-center"
                   placeholder="Nhập mã OTP" required pattern="[0-9]{6}" maxlength="6"
                   style="letter-spacing: 8px; font-size: 20px; font-weight: bold;">
            <div class="invalid-feedback">Vui lòng nhập đúng 6 chữ số OTP.</div>
        </div>

        <div class="mb-3">
            <label for="newPassword" class="form-label">Mật khẩu mới <span class="text-danger">*</span></label>
            <input type="password" id="newPassword" name="newPassword" class="form-control"
                   placeholder="Tối thiểu 6 ký tự" required minlength="6">
            <div class="invalid-feedback">Mật khẩu mới phải có ít nhất 6 ký tự.</div>
        </div>

        <div class="mb-3">
            <label for="confirmPassword" class="form-label">Xác nhận mật khẩu mới <span class="text-danger">*</span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
                   placeholder="Nhập lại mật khẩu mới" required minlength="6">
            <div class="invalid-feedback">Vui lòng xác nhận mật khẩu mới.</div>
        </div>

        <button type="submit" class="btn btn-primary w-100">
            <i class="bi bi-check-circle"></i> Đặt Lại Mật Khẩu
        </button>
    </form>

    <div class="auth-footer-text">
        <a href="${pageContext.request.contextPath}/login"><i class="bi bi-arrow-left"></i> Quay lại đăng nhập</a>
    </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Quên mật khẩu - Java Web MVC JPA</title>
</head>
<body>
    <div class="auth-brand">
        <h3><i class="bi bi-key"></i> Quên Mật Khẩu</h3>
        <p>Nhập email đã đăng ký để nhận mã OTP đặt lại mật khẩu</p>
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

    <form action="${pageContext.request.contextPath}/forgot-password" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="email" class="form-label">Địa chỉ Email đã đăng ký <span class="text-danger">*</span></label>
            <input type="email" id="email" name="email" class="form-control"
                   value="${email != null ? email : ''}"
                   placeholder="Nhập email đã đăng ký" required>
            <div class="invalid-feedback">Vui lòng nhập đúng định dạng Email.</div>
        </div>

        <button type="submit" class="btn btn-primary w-100">
            <i class="bi bi-send"></i> Gửi Mã OTP
        </button>
    </form>

    <div class="auth-footer-text">
        <a href="${pageContext.request.contextPath}/login"><i class="bi bi-arrow-left"></i> Quay lại đăng nhập</a>
    </div>
</body>
</html>

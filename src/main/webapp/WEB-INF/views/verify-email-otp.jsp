<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Xác Thực OTP Đổi Email - Java Web MVC JPA</title>
</head>
<body>
    <div class="auth-brand">
        <h3><i class="bi bi-envelope-check"></i> Xác Thực Đổi Email</h3>
        <p>Nhập mã OTP gồm 6 chữ số đã gửi đến email mới của bạn</p>
    </div>

    <%-- Thông báo phản hồi --%>
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

    <div class="alert alert-light border text-center mb-3">
        <small class="text-muted">Địa chỉ email mới đang chờ xác thực:</small><br>
        <strong class="text-primary" style="font-size: 16px;">${pendingEmail}</strong>
    </div>

    <form action="${pageContext.request.contextPath}/verify-email-otp" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="otp" class="form-label">Mã OTP (6 chữ số) <span class="text-danger">*</span></label>
            <input type="text" id="otp" name="otp" class="form-control form-control-lg text-center"
                   placeholder="______" required pattern="[0-9]{6}" maxlength="6"
                   style="letter-spacing: 10px; font-size: 24px; font-weight: bold;" autofocus>
            <div class="invalid-feedback">Vui lòng nhập đúng 6 chữ số mã OTP.</div>
        </div>

        <button type="submit" class="btn btn-success w-100">
            <i class="bi bi-check-circle"></i> Xác Nhận Đổi Email
        </button>
    </form>

    <div class="auth-footer-text mt-3">
        <form action="${pageContext.request.contextPath}/verify-email-otp" method="post" style="display:inline;">
            <input type="hidden" name="action" value="resend">
            <button type="submit" class="btn btn-link btn-sm text-decoration-none">
                <i class="bi bi-arrow-repeat"></i> Gửi lại mã OTP
            </button>
        </form>
        <br>
        <a href="${pageContext.request.contextPath}/profile"><i class="bi bi-arrow-left"></i> Quay lại trang Hồ Sơ</a>
    </div>
</body>
</html>

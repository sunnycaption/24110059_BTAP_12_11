<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Đăng ký tài khoản - Java Web MVC JPA</title>
</head>
<body>
    <div class="auth-brand">
        <h3><i class="bi bi-person-plus"></i> Đăng Ký Tài Khoản</h3>
        <p>Hệ thống xác thực & gửi mã OTP kích hoạt qua Email (24110059)</p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle"></i> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="username" class="form-label">Tên đăng nhập <span class="text-danger">*</span></label>
            <input type="text" id="username" name="username" class="form-control"
                   value="${username != null ? username : ''}"
                   placeholder="Ví dụ: nguyenvana" required minlength="3" pattern="[a-zA-Z0-9_]+">
            <div class="invalid-feedback">Tên đăng nhập phải có ít nhất 3 ký tự (chữ, số, gạch dưới).</div>
        </div>

        <div class="mb-3">
            <label for="fullname" class="form-label">Họ và tên <span class="text-danger">*</span></label>
            <input type="text" id="fullname" name="fullname" class="form-control"
                   value="${fullname != null ? fullname : ''}"
                   placeholder="Ví dụ: Nguyễn Văn A" required>
            <div class="invalid-feedback">Vui lòng nhập Họ và tên.</div>
        </div>

        <div class="mb-3">
            <label for="email" class="form-label">Địa chỉ Email (Nhận mã OTP) <span class="text-danger">*</span></label>
            <input type="email" id="email" name="email" class="form-control"
                   value="${email != null ? email : ''}"
                   placeholder="Ví dụ: your_email@gmail.com" required>
            <div class="invalid-feedback">Vui lòng nhập đúng định dạng Email.</div>
        </div>

        <div class="mb-3">
            <label for="phone" class="form-label">Số điện thoại</label>
            <input type="tel" id="phone" name="phone" class="form-control"
                   value="${phone != null ? phone : ''}"
                   placeholder="Ví dụ: 0901234567" pattern="0[0-9]{9}">
            <div class="invalid-feedback">Số điện thoại phải bắt đầu bằng 0 và gồm 10 chữ số.</div>
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">Mật khẩu <span class="text-danger">*</span></label>
            <input type="password" id="password" name="password" class="form-control"
                   placeholder="Tối thiểu 6 ký tự" required minlength="6">
            <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự.</div>
        </div>

        <div class="mb-3">
            <label for="confirmPassword" class="form-label">Xác nhận mật khẩu <span class="text-danger">*</span></label>
            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
                   placeholder="Nhập lại mật khẩu" required minlength="6">
            <div class="invalid-feedback">Vui lòng xác nhận mật khẩu.</div>
        </div>

        <div class="mb-3">
            <small class="text-muted">
                <i class="bi bi-info-circle"></i> Sau khi bấm Đăng Ký, mã OTP gồm 6 chữ số sẽ được gửi về email của bạn để kích hoạt tài khoản.
            </small>
        </div>

        <button type="submit" class="btn btn-primary w-100">
            <i class="bi bi-person-plus"></i> Đăng Ký Tài Khoản
        </button>
    </form>

    <div class="auth-footer-text">
        Đã có tài khoản?
        <a href="${pageContext.request.contextPath}/login">Đăng nhập tại đây</a>
    </div>
</body>
</html>

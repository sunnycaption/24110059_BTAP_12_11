<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Hồ Sơ Cá Nhân - Java Web MVC JPA</title>
    <style>
        .profile-avatar-wrapper {
            position: relative;
            width: 140px;
            height: 140px;
            margin: 0 auto 15px;
        }
        .profile-avatar {
            width: 140px;
            height: 140px;
            border-radius: 50%;
            object-fit: cover;
            border: 4px solid #fff;
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        .profile-card {
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.06);
        }
    </style>
</head>
<body>
    <div class="container py-3">
        <div class="row mb-3">
            <div class="col">
                <h4><i class="bi bi-person-badge"></i> Hồ Sơ Cá Nhân</h4>
                <p class="text-muted" style="font-size: 14px;">Quản lý thông tin tài khoản, ảnh đại diện và địa chỉ email của bạn</p>
            </div>
        </div>

        <%-- Thông báo phản hồi --%>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="bi bi-check-circle-fill"></i> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle-fill"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        <c:if test="${not empty infoMessage}">
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <i class="bi bi-info-circle-fill"></i> ${infoMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <%-- Cảnh báo nếu đang có yêu cầu đổi email chờ xác thực --%>
        <c:if test="${not empty user.pendingEmail}">
            <div class="alert alert-warning d-flex justify-content-between align-items-center" role="alert">
                <div>
                    <i class="bi bi-clock-history"></i> Bạn đang có yêu cầu đổi sang email mới: <strong>${user.pendingEmail}</strong> đang chờ xác thực mã OTP.
                </div>
                <a href="${pageContext.request.contextPath}/verify-email-otp" class="btn btn-warning btn-sm">
                    <i class="bi bi-shield-check"></i> Nhập mã OTP ngay
                </a>
            </div>
        </c:if>

        <div class="row g-4">
            <!-- Cột trái: Thông tin tổng quan & Avatar -->
            <div class="col-lg-4">
                <div class="card profile-card text-center p-4">
                    <div class="profile-avatar-wrapper">
                        <c:choose>
                            <c:when test="${not empty user.images && (user.images.startsWith('http://') || user.images.startsWith('https://'))}">
                                <img src="${user.images}" alt="Avatar" class="profile-avatar" id="avatarPreview"
                                     onerror="this.src='https://placehold.co/140x140/667eea/fff?text=Avatar';">
                            </c:when>
                            <c:when test="${not empty user.images}">
                                <img src="${pageContext.request.contextPath}/${user.images.startsWith('uploads/') ? user.images : 'uploads/'.concat(user.images)}"
                                     alt="Avatar" class="profile-avatar" id="avatarPreview"
                                     onerror="this.src='https://placehold.co/140x140/667eea/fff?text=Avatar';">
                            </c:when>
                            <c:otherwise>
                                <img src="https://placehold.co/140x140/667eea/fff?text=User" alt="Avatar" class="profile-avatar" id="avatarPreview">
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <h5 class="mb-1">${user.fullname}</h5>
                    <p class="text-muted mb-2">@${user.username}</p>
                    <div>
                        <span class="badge bg-success"><i class="bi bi-check-circle"></i> Tài khoản đã kích hoạt</span>
                    </div>

                    <hr class="my-3">

                    <div class="text-start" style="font-size: 14px;">
                        <p class="mb-2"><strong><i class="bi bi-envelope"></i> Email:</strong> <br><span class="text-muted">${user.email}</span></p>
                        <p class="mb-2"><strong><i class="bi bi-telephone"></i> SĐT:</strong> <br><span class="text-muted">${not empty user.phone ? user.phone : 'Chưa cập nhật'}</span></p>
                        <p class="mb-0"><strong><i class="bi bi-calendar"></i> Tham gia:</strong> <br><span class="text-muted">${user.createdAt}</span></p>
                    </div>
                </div>
            </div>

            <!-- Cột phải: Form cập nhật thông tin & Đổi Email -->
            <div class="col-lg-8">
                <!-- Card 1: Cập nhật thông tin & Avatar Cloudinary -->
                <div class="card profile-card p-4 mb-4">
                    <h5 class="card-title mb-3"><i class="bi bi-pencil-square text-primary"></i> Thông Tin Cá Nhân & Upload Avatar</h5>
                    <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
                        <input type="hidden" name="action" value="update_info">

                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="username" class="form-label">Tên đăng nhập</label>
                                <input type="text" id="username" class="form-control" value="${user.username}" readonly disabled>
                                <small class="text-muted">Tên đăng nhập không thể thay đổi.</small>
                            </div>
                            <div class="col-md-6">
                                <label for="fullname" class="form-label">Họ và tên <span class="text-danger">*</span></label>
                                <input type="text" id="fullname" name="fullname" class="form-control"
                                       value="${user.fullname}" required>
                                <div class="invalid-feedback">Vui lòng nhập Họ và tên.</div>
                            </div>
                        </div>

                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label for="phone" class="form-label">Số điện thoại (Việt Nam)</label>
                                <input type="tel" id="phone" name="phone" class="form-control"
                                       value="${user.phone}" placeholder="Ví dụ: 0901234567"
                                       pattern="0[0-9]{9}">
                                <div class="invalid-feedback">Số điện thoại phải bắt đầu bằng số 0 và có đúng 10 chữ số.</div>
                            </div>
                            <div class="col-md-6">
                                <label for="imageFile" class="form-label">Chọn ảnh đại diện mới (Cloudinary)</label>
                                <input type="file" id="imageFile" name="imageFile" class="form-control" accept="image/*"
                                       onchange="previewImage(this)">
                                <small class="text-muted">Chấp nhận JPG, PNG, GIF (tối đa 10MB). Upload lên Cloudinary.</small>
                            </div>
                        </div>

                        <div class="d-flex justify-content-end">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-cloud-arrow-up"></i> Lưu Thông Tin & Upload Avatar
                            </button>
                        </div>
                    </form>
                </div>

                <!-- Card 2: Đổi Địa Chỉ Email (Xác Thực OTP) -->
                <div class="card profile-card p-4">
                    <h5 class="card-title mb-3"><i class="bi bi-envelope-at text-warning"></i> Thay Đổi Địa Chỉ Email</h5>
                    <p class="text-muted" style="font-size: 14px;">
                        Khi bạn yêu cầu đổi email, hệ thống sẽ gửi mã xác thực OTP 6 số đến <strong>địa chỉ email mới</strong>. 
                        Sau khi bạn nhập đúng mã OTP, email của tài khoản mới chính thức được cập nhật.
                    </p>

                    <form action="${pageContext.request.contextPath}/profile" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="action" value="change_email">

                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Email hiện tại</label>
                                <input type="email" class="form-control" value="${user.email}" readonly disabled>
                            </div>
                            <div class="col-md-6">
                                <label for="newEmail" class="form-label">Email mới cần đổi <span class="text-danger">*</span></label>
                                <input type="email" id="newEmail" name="newEmail" class="form-control"
                                       placeholder="Ví dụ: new_email@gmail.com" required>
                                <div class="invalid-feedback">Vui lòng nhập đúng định dạng Email mới.</div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-end">
                            <button type="submit" class="btn btn-warning text-dark">
                                <i class="bi bi-send-check"></i> Gửi Mã OTP Đến Email Mới
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
    function previewImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                var preview = document.getElementById('avatarPreview');
                if (preview) {
                    preview.src = e.target.result;
                }
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
    </script>
</body>
</html>

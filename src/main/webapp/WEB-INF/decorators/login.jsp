<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title">Xác Thực - Java Web MVC JPA</sitemesh:write></title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 24px 16px;
        }
        .auth-card {
            background: #ffffff;
            border-radius: 14px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            padding: 36px;
            max-width: 480px;
            width: 100%;
        }
        .auth-footer-text {
            text-align: center;
            margin-top: 20px;
            font-size: 14px;
            color: rgba(255, 255, 255, 0.75);
        }
        .auth-footer-text a {
            color: #ffffff;
            font-weight: 600;
            text-decoration: underline;
        }
    </style>
    <sitemesh:write property="head"/>
</head>
<body>
    <div class="auth-card">
        <sitemesh:write property="body"/>
    </div>
    <div class="auth-footer-text">
        <small>&copy; 2026 Java Web MVC JPA | MSSV: 24110059 - Lê Thị Thanh Thúy</small>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Bootstrap 5 Client-side Form Validation Script -->
    <script>
    (function () {
        'use strict';
        var forms = document.querySelectorAll('.needs-validation');
        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();
    </script>
</body>
</html>

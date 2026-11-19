<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title">Quản Trị Hệ Thống - Java Web MVC</sitemesh:write></title>
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 56px;
            background-color: #f1f3f5;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .content-wrapper {
            flex: 1 0 auto;
            padding: 24px 0;
        }
        .footer {
            flex-shrink: 0;
            background: #212529;
            color: #adb5bd;
            font-size: 14px;
        }
    </style>
    <sitemesh:write property="head"/>
</head>
<body>
    <!-- Admin Header -->
    <c:import url="/common/admin/header.jsp"/>

    <!-- Main Container with Sidebar + Body -->
    <div class="content-wrapper">
        <div class="container-fluid px-4">
            <div class="row">
                <div class="col-lg-3 col-md-4 mb-4">
                    <c:import url="/common/admin/left.jsp"/>
                </div>
                <div class="col-lg-9 col-md-8">
                    <sitemesh:write property="body"/>
                </div>
            </div>
        </div>
    </div>

    <!-- Admin Footer -->
    <c:import url="/common/admin/footer.jsp"/>

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

package vn.iotstar.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Tiện ích gửi Email sử dụng Jakarta Mail và SMTP Gmail.
 * Tự động đọc cấu hình từ file email.properties.
 */
public class EmailUtil {
    private static Properties mailProps = new Properties();
    private static String username;
    private static String password;
    private static String fromName;

    static {
        try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (is != null) {
                mailProps.load(is);
                username = mailProps.getProperty("mail.username");
                password = mailProps.getProperty("mail.password");
                fromName = mailProps.getProperty("mail.from.name", "Java Web OTP Verification");
            } else {
                System.err.println("⚠️ [EmailUtil] Không tìm thấy file email.properties! Sử dụng cấu hình mặc định.");
            }
        } catch (Exception e) {
            System.err.println("❌ [EmailUtil] Lỗi khi đọc file email.properties: " + e.getMessage());
        }
    }

    /**
     * Gửi email OTP kích hoạt tài khoản hoặc đặt lại mật khẩu.
     * @param toEmail Email người nhận
     * @param otp Mã OTP 6 chữ số
     * @param purpose Mục đích gửi (ví dụ: "Kích hoạt tài khoản", "Đặt lại mật khẩu")
     * @return true nếu gửi thành công, false nếu thất bại
     */
    public static boolean sendOtpEmail(String toEmail, String otp, String purpose) {
        String subject = "[Java Web] Mã OTP " + purpose + " của bạn: " + otp;
        String htmlContent = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #fdfdfd;\">"
                + "<h2 style=\"color: #0d6efd; text-align: center; border-bottom: 2px solid #0d6efd; padding-bottom: 10px;\">XÁC THỰC BẢO MẬT TÀI KHOẢN</h2>"
                + "<p>Xin chào,</p>"
                + "<p>Bạn vừa thực hiện yêu cầu <strong>" + purpose + "</strong> trên hệ thống của chúng tôi.</p>"
                + "<p>Mã xác thực OTP của bạn là:</p>"
                + "<div style=\"text-align: center; margin: 25px 0;\">"
                + "<span style=\"display: inline-block; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #dc3545; background: #fff3cd; padding: 12px 25px; border-radius: 6px; border: 1px dashed #ffc107;\">"
                + otp
                + "</span>"
                + "</div>"
                + "<p style=\"color: #6c757d; font-size: 14px;\">Mã OTP này có hiệu lực trong vòng <strong>" + OtpUtil.OTP_EXPIRY_MINUTES + " phút</strong>. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                + "<hr style=\"border: none; border-top: 1px solid #e9ecef; margin: 20px 0;\">"
                + "<p style=\"font-size: 12px; color: #adb5bd; text-align: center;\">Đây là email tự động từ hệ thống Java Web MVC (Mã SV: 24110059). Vui lòng không trả lời email này.</p>"
                + "</div>";

        return sendHtmlEmail(toEmail, subject, htmlContent, otp);
    }

    /**
     * Gửi email dạng HTML qua SMTP Gmail.
     */
    public static boolean sendHtmlEmail(String toEmail, String subject, String htmlContent, String fallbackOtp) {
        // Luôn in mã OTP ra console để hỗ trợ dev/test local ngay cả khi chưa có App Password Gmail thực tế
        System.out.println("==================================================================");
        System.out.println("📧 [EmailUtil] GỬI EMAIL TỚI: " + toEmail);
        System.out.println("🔑 [EmailUtil] TIÊU ĐỀ: " + subject);
        if (fallbackOtp != null) {
            System.out.println("⚡ [EmailUtil] MÃ OTP TRONG EMAIL LÀ: >>> " + fallbackOtp + " <<<");
        }
        System.out.println("==================================================================");

        try {
            Session session = Session.getInstance(mailProps, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, fromName, StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("✅ [EmailUtil] Đã gửi email thành công tới: " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("⚠️ [EmailUtil] Không thể gửi email qua SMTP: " + e.getMessage());
            System.err.println("💡 Ghi chú: Nếu chưa cấu hình App Password Gmail trong email.properties, bạn vẫn có thể lấy mã OTP đã in ở trên console để xác thực test!");
            return false;
        }
    }
}

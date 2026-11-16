package vn.iotstar.utils;

import java.security.SecureRandom;
import java.util.Date;

/**
 * Tiện ích sinh mã OTP và quản lý thời hạn hiệu lực của OTP.
 */
public class OtpUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final int OTP_EXPIRY_MINUTES = 5; // Mặc định thời hạn OTP là 5 phút

    /**
     * Sinh mã OTP 6 chữ số ngẫu nhiên (từ 000000 đến 999999).
     */
    public static String generateOtp() {
        int number = RANDOM.nextInt(1000000);
        return String.format("%06d", number);
    }

    /**
     * Tính thời gian hết hạn của OTP (tính từ thời điểm hiện tại).
     * @param minutes Số phút hiệu lực
     */
    public static Date generateExpiryTime(int minutes) {
        long nowMillis = System.currentTimeMillis();
        long expiryMillis = nowMillis + ((long) minutes * 60 * 1000);
        return new Date(expiryMillis);
    }

    /**
     * Kiểm tra mã OTP đã hết hạn hay chưa.
     */
    public static boolean isExpired(Date expiryTime) {
        if (expiryTime == null) {
            return true;
        }
        return new Date().after(expiryTime);
    }
}

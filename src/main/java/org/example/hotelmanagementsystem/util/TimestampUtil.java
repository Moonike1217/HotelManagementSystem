package org.example.hotelmanagementsystem.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 时间戳工具类
 */
public class TimestampUtil {
    
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * 获取当前时间的秒级时间戳
     * @return 当前时间的秒级时间戳
     */
    public static Long getCurrentTimestamp() {
        return LocalDateTime.now().atZone(DEFAULT_ZONE).toEpochSecond();
    }
    
    /**
     * 将 LocalDateTime 转换为秒级时间戳
     * @param localDateTime 日期时间
     * @return 秒级时间戳
     */
    public static Long toTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(DEFAULT_ZONE).toEpochSecond();
    }
    
    /**
     * 将秒级时间戳转换为 LocalDateTime
     * @param timestamp 秒级时间戳
     * @return 日期时间
     */
    public static LocalDateTime fromTimestamp(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), DEFAULT_ZONE);
    }
    
    /**
     * 将秒级时间戳转换为格式化字符串
     * @param timestamp 秒级时间戳
     * @return 格式化日期时间字符串
     */
    public static String formatTimestamp(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        LocalDateTime localDateTime = fromTimestamp(timestamp);
        return localDateTime.format(DEFAULT_FORMATTER);
    }
    
    /**
     * 将格式化字符串转换为秒级时间戳
     * @param dateTimeStr 格式化日期时间字符串
     * @return 秒级时间戳
     */
    public static Long parseTimestamp(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, DEFAULT_FORMATTER);
        return toTimestamp(localDateTime);
    }
}
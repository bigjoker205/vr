package com.vrvideo.player.utils;

/**
 * 时间格式化工具类
 */
public class TimeFormatter {

    /**
     * 将毫秒转换为 HH:MM:SS 格式
     */
    public static String formatMilliseconds(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * 将毫秒转换为 MM:SS 格式
     */
    public static String formatMillisecondsShort(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 将秒数转换为 HH:MM:SS 格式
     */
    public static String formatSeconds(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * 将秒数转换为 MM:SS 格式
     */
    public static String formatSecondsShort(long seconds) {
        long minutes = seconds / 60;
        long secs = seconds % 60;
        
        return String.format("%02d:%02d", minutes, secs);
    }

    /**
     * 获取总播放时长的显示格式
     */
    public static String getDurationFormat(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        if (totalSeconds < 3600) {
            return formatMillisecondsShort(milliseconds);
        } else {
            return formatMilliseconds(milliseconds);
        }
    }
}

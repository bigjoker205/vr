package com.vrvideo.player.utils;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypeInfo;

/**
 * 视频URL工具类 - 处理视频链接解析和验证
 */
public class VideoUrlUtils {

    /**
     * 验证URL格式
     */
    public static boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return url.startsWith("http://") || url.startsWith("https://");
    }

    /**
     * 获取视频文件扩展名
     */
    public static String getVideoExtension(String url) {
        if (url == null) return "";
        int lastDot = url.lastIndexOf('.');
        if (lastDot > 0 && lastDot < url.length() - 1) {
            return url.substring(lastDot + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 判断是否为360度全景视频
     */
    public static boolean is360Video(String url) {
        // 可以根据文件名或其他特征判断
        String lowercaseUrl = url.toLowerCase();
        return lowercaseUrl.contains("360") || 
               lowercaseUrl.contains("panorama") ||
               lowercaseUrl.contains("spherical");
    }

    /**
     * 创建MediaItem
     */
    public static MediaItem createMediaItem(String url) {
        return MediaItem.fromUri(Uri.parse(url));
    }

    /**
     * 获取MIME类型
     */
    public static String getMimeType(String url) {
        String extension = getVideoExtension(url);
        switch (extension) {
            case "mp4":
                return "video/mp4";
            case "m3u8":
                return "application/x-mpegURL";
            case "mpd":
                return "application/dash+xml";
            case "mkv":
                return "video/x-matroska";
            case "webm":
                return "video/webm";
            default:
                return "video/*";
        }
    }

    /**
     * 格式化URL（添加协议如果缺少）
     */
    public static String formatUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "";
        }
        url = url.trim();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        return url;
    }
}

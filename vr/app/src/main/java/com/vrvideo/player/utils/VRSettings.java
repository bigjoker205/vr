package com.vrvideo.player.utils;

/**
 * VR设置工具类 - 管理VR相关的参数和偏好设置
 */
public class VRSettings {

    // 瞳距范围 (单位: 米)
    public static final float MIN_PUPIL_DISTANCE = 0.050f; // 5cm
    public static final float MAX_PUPIL_DISTANCE = 0.080f; // 8cm
    public static final float DEFAULT_PUPIL_DISTANCE = 0.065f; // 6.5cm

    // 屏幕大小范围 (单位: 英寸)
    public static final float MIN_SCREEN_SIZE = 3.0f;
    public static final float MAX_SCREEN_SIZE = 7.0f;
    public static final float DEFAULT_SCREEN_SIZE = 5.0f;

    // 播放速度选项
    public static final float[] PLAY_SPEEDS = {0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};

    // 快进/快退步长 (单位: 毫秒)
    public static final int FAST_FORWARD_DURATION = 10000; // 10秒
    public static final int FAST_BACKWARD_DURATION = 10000; // 10秒

    /**
     * 验证瞳距值
     */
    public static boolean isValidPupilDistance(float distance) {
        return distance >= MIN_PUPIL_DISTANCE && distance <= MAX_PUPIL_DISTANCE;
    }

    /**
     * 验证屏幕大小值
     */
    public static boolean isValidScreenSize(float size) {
        return size >= MIN_SCREEN_SIZE && size <= MAX_SCREEN_SIZE;
    }

    /**
     * 验证播放速度值
     */
    public static boolean isValidPlaySpeed(float speed) {
        return speed > 0 && speed <= 2.0f;
    }

    /**
     * 获取最接近的播放速度
     */
    public static float getNearestPlaySpeed(float speed) {
        if (!isValidPlaySpeed(speed)) {
            return 1.0f;
        }
        
        float nearest = PLAY_SPEEDS[0];
        float minDiff = Math.abs(speed - nearest);
        
        for (float s : PLAY_SPEEDS) {
            float diff = Math.abs(speed - s);
            if (diff < minDiff) {
                nearest = s;
                minDiff = diff;
            }
        }
        return nearest;
    }

    /**
     * 限制瞳距在有效范围内
     */
    public static float clampPupilDistance(float distance) {
        return Math.max(MIN_PUPIL_DISTANCE, 
                       Math.min(MAX_PUPIL_DISTANCE, distance));
    }

    /**
     * 限制屏幕大小在有效范围内
     */
    public static float clampScreenSize(float size) {
        return Math.max(MIN_SCREEN_SIZE, 
                       Math.min(MAX_SCREEN_SIZE, size));
    }

    /**
     * 转换瞳距 - 从毫米转换为米
     */
    public static float pupilDistanceFromMM(float mm) {
        return mm / 1000.0f;
    }

    /**
     * 转换瞳距 - 从米转换为毫米
     */
    public static float pupilDistanceToMM(float meters) {
        return meters * 1000.0f;
    }
}

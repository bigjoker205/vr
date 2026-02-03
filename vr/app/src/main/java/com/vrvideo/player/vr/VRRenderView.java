package com.vrvideo.player.vr;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

/**
 * VR渲染视图 - 处理360度全景视频渲染和陀螺仪控制
 */
public class VRRenderView extends GLSurfaceView implements SensorEventListener {
    
    private VRRenderer renderer;
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private float[] rotationMatrix = new float[16];
    private float[] orientationAngles = new float[3];

    public VRRenderView(Context context) {
        super(context);
        initialize(context);
    }

    public VRRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        setEGLContextClientVersion(2);
        
        renderer = new VRRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        
        // 初始化陀螺仪传感器
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        }
    }

    /**
     * 启用陀螺仪控制
     */
    public void enableGyroControl() {
        if (sensorManager != null && gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    /**
     * 禁用陀螺仪控制
     */
    public void disableGyroControl() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // 转换旋转向量为旋转矩阵
            float[] rotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            
            // 更新渲染器的旋转信息
            if (renderer != null) {
                renderer.setRotationMatrix(rotationMatrix);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    /**
     * 设置VR参数
     */
    public void setVRParameters(float pupilDistance, float screenSize) {
        if (renderer != null) {
            renderer.setPupilDistance(pupilDistance);
            renderer.setScreenSize(screenSize);
        }
    }

    /**
     * 获取VR渲染器
     */
    public VRRenderer getRenderer() {
        return renderer;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        disableGyroControl();
    }
}

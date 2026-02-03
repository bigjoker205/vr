package com.vrvideo.player.vr;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLES20;
import android.graphics.Bitmap;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * VR渲染器 - 负责OpenGL渲染和VR效果
 */
public class VRRenderer implements GLSurfaceView.Renderer {
    
    private Context context;
    private int textureID = -1;
    private Bitmap videoBitmap;
    private float pupilDistance = 0.065f; // 默认瞳距 6.5cm
    private float screenSize = 5.0f; // 默认屏幕大小 5英寸
    private float[] rotationMatrix = new float[9];
    private int program;
    
    // 球体顶点数据
    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private short[] indices;
    private int indexCount;

    public VRRenderer(Context context) {
        this.context = context;
        initializeSphere();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置背景颜色
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        // 启用深度测试
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        
        // 创建着色器程序
        program = createShaderProgram();
        
        // 创建纹理
        textureID = createTexture();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        
        GLES20.glUseProgram(program);
        
        // 绘制左眼视图
        GLES20.glViewport(0, 0, 540, 1080);
        drawEye(-pupilDistance / 2.0f);
        
        // 绘制右眼视图
        GLES20.glViewport(540, 0, 540, 1080);
        drawEye(pupilDistance / 2.0f);
    }

    /**
     * 绘制单个眼睛的视图
     */
    private void drawEye(float eyeOffset) {
        // 构建投影矩阵和视图矩阵
        float[] projectionMatrix = new float[16];
        float[] viewMatrix = new float[16];
        
        // 应用陀螺仪旋转
        applyRotation(viewMatrix);
        
        // 设置顶点属性
        int positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
        
        // 设置纹理坐标
        int texCoordHandle = GLES20.glGetAttribLocation(program, "vTexCoord");
        GLES20.glEnableVertexAttribArray(texCoordHandle);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 8, texCoordBuffer);
        
        // 绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
        int textureHandle = GLES20.glGetUniformLocation(program, "uTexture");
        GLES20.glUniform1i(textureHandle, 0);
        
        // 绘制球体
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, ByteBuffer.wrap(new byte[0]));
        
        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);
    }

    /**
     * 应用陀螺仪旋转
     */
    private void applyRotation(float[] viewMatrix) {
        // TODO: 应用旋转矩阵到视图
    }

    /**
     * 初始化球体网格
     */
    private void initializeSphere() {
        int latitudes = 32;
        int longitudes = 64;
        
        float[] vertices = new float[(latitudes + 1) * (longitudes + 1) * 3];
        float[] texCoords = new float[(latitudes + 1) * (longitudes + 1) * 2];
        
        int vertexIndex = 0;
        int texCoordIndex = 0;
        float radius = 1.0f;
        
        for (int lat = 0; lat <= latitudes; lat++) {
            float theta = (float) (Math.PI * lat / latitudes);
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);
            
            for (int lon = 0; lon <= longitudes; lon++) {
                float phi = (float) (2.0f * Math.PI * lon / longitudes);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);
                
                vertices[vertexIndex++] = radius * sinTheta * cosPhi;
                vertices[vertexIndex++] = radius * cosTheta;
                vertices[vertexIndex++] = radius * sinTheta * sinPhi;
                
                texCoords[texCoordIndex++] = (float) lon / longitudes;
                texCoords[texCoordIndex++] = (float) lat / latitudes;
            }
        }
        
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertices);
        vertexBuffer.position(0);
        
        texCoordBuffer = ByteBuffer.allocateDirect(texCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texCoords);
        texCoordBuffer.position(0);
    }

    /**
     * 创建着色器程序
     */
    private int createShaderProgram() {
        String vertexShaderCode = "attribute vec3 vPosition;" +
                "attribute vec2 vTexCoord;" +
                "varying vec2 fragTexCoord;" +
                "uniform mat4 uMVPMatrix;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vec4(vPosition, 1.0);" +
                "  fragTexCoord = vTexCoord;" +
                "}";
        
        String fragmentShaderCode = "precision mediump float;" +
                "varying vec2 fragTexCoord;" +
                "uniform sampler2D uTexture;" +
                "void main() {" +
                "  gl_FragColor = texture2D(uTexture, fragTexCoord);" +
                "}";
        
        int vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        
        return program;
    }

    /**
     * 编译着色器
     */
    private int compileShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        return shader;
    }

    /**
     * 创建纹理
     */
    private int createTexture() {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int textureID = textures[0];
        
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        
        return textureID;
    }

    /**
     * 更新视频纹理
     */
    public void updateTexture(Bitmap bitmap) {
        this.videoBitmap = bitmap;
        if (textureID != -1 && bitmap != null) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        }
    }

    /**
     * 设置瞳距
     */
    public void setPupilDistance(float distance) {
        this.pupilDistance = distance;
    }

    /**
     * 设置屏幕大小
     */
    public void setScreenSize(float size) {
        this.screenSize = size;
    }

    /**
     * 设置旋转矩阵
     */
    public void setRotationMatrix(float[] matrix) {
        System.arraycopy(matrix, 0, this.rotationMatrix, 0, Math.min(matrix.length, rotationMatrix.length));
    }
}

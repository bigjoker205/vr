# VR Video Player - 使用和开发指南

## 项目概述

VR Video Player是一款功能完整的Android VR视频播放器，可以通过输入视频URL来播放视频，支持VR分屏显示和陀螺仪控制视角。

## 快速开始

### 1. 编译项目

```bash
# 进入项目目录
cd vr

# 编译Debug版本
./gradlew build

# 如果编译出现问题，清理后重新编译
./gradlew clean build
```

### 2. 安装到设备

```bash
# 安装Debug APK
./gradlew installDebug

# 或者手动安装已生成的APK文件
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 3. 运行应用

在设备上点击应用图标，或使用以下命令：

```bash
./gradlew run
```

## 使用说明

### 主界面操作

1. **输入视频URL** - 在输入框中粘贴或输入视频链接
   - 支持格式：`https://example.com/video.mp4`
   - 应用会自动验证URL格式

2. **选择播放模式**
   - **VR模式** - 用Google Cardboard等VR设备观看
   - **普通模式** - 标准手机屏幕播放

### 播放器界面

#### 控制面板按钮

1. **播放/暂停** - 控制视频播放状态
2. **播放速度** - 选择播放速度（0.5x - 2.0x）
3. **瞳距** - 调整眼镜光学中心距（适合VR模式）
4. **屏幕大小** - 调整虚拟屏幕尺寸（适合VR模式）
5. **模式切换** - 在VR和普通模式间切换

#### 进度条操作

- **拖动进度条** - 跳转到指定时间
- **左侧数字** - 当前播放时间
- **右侧数字** - 视频总时长

### VR模式特殊操作

1. **放入VR眼镜** - 将手机放入Google Cardboard或类似设备
2. **移动头部** - 改变视角方向
3. **调整参数** - 根据个人舒适度调整瞳距和屏幕大小

## 开发指南

### 项目结构

```
app/src/main/java/com/vrvideo/player/
├── MainActivity.java              # 主界面入口
├── ui/
│   └── VRPlayerActivity.java      # 播放器界面
├── vr/
│   ├── VRRenderView.java          # VR视图容器
│   └── VRRenderer.java            # OpenGL渲染器
└── utils/
    ├── VideoUrlUtils.java         # URL工具
    ├── VRSettings.java            # VR配置
    └── TimeFormatter.java         # 时间格式化
```

### 主要类说明

#### MainActivity
入口Activity，处理URL输入和模式选择。

```java
// 启动VR播放
Intent intent = new Intent(this, VRPlayerActivity.class);
intent.putExtra("video_url", url);
intent.putExtra("vr_mode", true);
startActivity(intent);
```

#### VRPlayerActivity
播放器界面，管理ExoPlayer和UI控制。

关键方法：
- `setupPlayer()` - 初始化播放器
- `togglePlayPause()` - 暂停/播放
- `setPlaySpeed()` - 设置播放速度
- `toggleVRMode()` - 切换模式

#### VRRenderView
GLSurfaceView的扩展，处理陀螺仪和VR渲染。

关键方法：
- `enableGyroControl()` - 启用陀螺仪
- `setVRParameters()` - 设置VR参数
- `onSensorChanged()` - 处理陀螺仪事件

#### VRRenderer
OpenGL渲染器，进行立体渲染。

关键方法：
- `onSurfaceCreated()` - 初始化OpenGL
- `onDrawFrame()` - 渲染单帧
- `drawEye()` - 绘制单个眼睛视图

### 扩展功能

#### 添加新的播放速度

修改 `app/src/main/java/com/vrvideo/player/utils/VRSettings.java`：

```java
public static final float[] PLAY_SPEEDS = {0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
// 添加新的速度值到数组中
```

#### 自定义VR参数范围

修改 `VRSettings.java`：

```java
public static final float MIN_PUPIL_DISTANCE = 0.050f; // 修改最小瞳距
public static final float MAX_PUPIL_DISTANCE = 0.080f; // 修改最大瞳距
```

#### 更改颜色主题

修改 `app/src/main/res/values/colors.xml`：

```xml
<color name="purple_500">#FF6200EE</color> <!-- 修改主要颜色 -->
<color name="teal_700">#FF018786</color>   <!-- 修改辅助颜色 -->
```

### 常见修改任务

#### 1. 修改默认播放速度
文件：`VRPlayerActivity.java`
```java
private float currentPlaySpeed = 1.0f; // 改为需要的值
```

#### 2. 修改快进/快退时长
文件：`VRSettings.java`
```java
public static final int FAST_FORWARD_DURATION = 10000; // 改为毫秒数
```

#### 3. 修改VR渲染球体精度
文件：`VRRenderer.java`，`initializeSphere()` 方法：
```java
int latitudes = 32;    // 调整纬度数（更高=更精细）
int longitudes = 64;   // 调整经度数（更高=更精细）
```

#### 4. 修改陀螺仪灵敏度
文件：`VRRenderer.java`，`drawEye()` 方法中调整旋转矩阵应用。

## 故障排除

### 编译错误

#### 错误：找不到Android SDK
解决方案：设置 `local.properties` 文件中的SDK路径
```properties
sdk.dir=/path/to/android/sdk
```

#### 错误：Gradle版本不兼容
解决方案：确保使用Gradle 8.1+
```bash
./gradlew --version
```

### 运行时问题

#### 问题：VR渲染黑屏
原因：纹理未正确加载
解决方案：
1. 检查URL是否有效
2. 确认视频能正常播放
3. 查看logcat日志中的OpenGL错误

#### 问题：陀螺仪无响应
原因：传感器不可用或权限不足
解决方案：
1. 检查设备是否有陀螺仪传感器
2. 确认应用权限已授予
3. 在手机设置中校准陀螺仪

#### 问题：视频播放卡顿
原因：网络速度或设备性能不足
解决方案：
1. 检查网络连接
2. 降低视频分辨率
3. 关闭其他应用以释放内存
4. 尝试预加载视频

### 性能优化

1. **降低渲染分辨率**
   - 修改 `VRRenderer.java` 中的视口大小

2. **减少球体精度**
   - 降低 `initializeSphere()` 中的纬度/经度数

3. **启用硬件加速**
   - 在 `AndroidManifest.xml` 中确保硬件加速已启用

4. **使用低分辨率视频**
   - 使用720p而非4K视频

## 依赖库文档

### ExoPlayer (Media3)
- 文档：https://developer.android.com/guide/topics/media/media3
- 支持格式：MP4, HLS, DASH, Matroska等

### Google VR SDK
- 文档：https://developers.google.com/vr/android
- 提供VR功能支持

### OpenGL ES 2.0
- 文档：https://www.khronos.org/opengles/
- 用于3D图形渲染

## 测试检查清单

- [ ] 编译无错误
- [ ] URL输入验证工作
- [ ] VR模式正常显示
- [ ] 普通模式正常显示
- [ ] 播放/暂停功能正常
- [ ] 播放速度调节工作
- [ ] 快进快退功能正常
- [ ] 陀螺仪正确控制视角
- [ ] 瞳距调整生效
- [ ] 屏幕大小调整生效
- [ ] 模式切换无闪烁
- [ ] 音视频同步
- [ ] 网络连接正常
- [ ] 没有内存泄漏

## 更新日志

### 版本 1.0.0 (初始版本)
- ✅ 完整的VR视频播放功能
- ✅ 陀螺仪控制
- ✅ 播放控制
- ✅ VR参数调整
- ✅ 模式切换

## 许可证

MIT License - 自由使用和修改

## 参考资源

### Android开发
- [Android开发者指南](https://developer.android.com/guide)
- [AndroidX库](https://developer.android.com/jetpack/androidx)

### VR开发
- [Google Cardboard](https://www.google.com/cardboard/)
- [Google VR SDK](https://developers.google.com/vr/android)

### 视频播放
- [Media3/ExoPlayer](https://developer.android.com/guide/topics/media/media3)

---

**更新日期**: 2026年2月3日
**版本**: 1.0.0

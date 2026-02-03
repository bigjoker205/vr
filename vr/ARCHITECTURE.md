# VR Video Player - 完整项目架构文档

## 项目概述

VR Video Player 是一款功能完整的Android VR视频播放器应用。用户可以通过输入视频URL链接来播放视频，应用支持两种播放模式：

1. **VR模式** - 使用OpenGL ES 2.0进行立体渲染，将视频显示为两个相同但微妙不同的画面供左右眼观看
2. **普通模式** - 使用ExoPlayer进行标准视频播放

应用使用陀螺仪传感器检测手机旋转，实时更新3D视角，提供沉浸式的VR体验。

## 项目特性

### 核心特性
- ✅ URL视频输入和播放
- ✅ VR分屏立体渲染
- ✅ 陀螺仪视角控制
- ✅ 双模式切换（VR/普通）
- ✅ 丰富的播放控制

### 播放控制特性
- ✅ 播放/暂停
- ✅ 播放速度调节 (0.5x - 2.0x)
- ✅ 快进/快退 (10秒步长)
- ✅ 进度条拖拽
- ✅ 实时时间显示

### VR特性
- ✅ 瞳距调整 (5cm - 8cm)
- ✅ 屏幕大小设置 (3" - 7")
- ✅ 陀螺仪启用/禁用
- ✅ 实时3D视角变换
- ✅ 双眼分屏渲染

## 技术架构

### 应用层级

```
┌─────────────────────────────────────┐
│        User Interface (UI)           │
│  MainActivity + VRPlayerActivity     │
└─────────────────────────────────────┘
         ↓          ↓          ↓
┌──────────────┬──────────────┬──────────────┐
│  ExoPlayer   │  VRRenderer  │  Utilities   │
│  (播放)      │  (VR渲染)    │  (工具类)    │
└──────────────┴──────────────┴──────────────┘
         ↓          ↓          ↓
┌──────────────────────────────────────┐
│  Android Framework & Libraries       │
│  - AndroidX, Media3, Sensor Manager  │
└──────────────────────────────────────┘
```

### 模块划分

#### 1. 主界面模块 (MainActivity)
**职责**: 用户输入、模式选择
- URL输入框
- VR模式和普通模式按钮
- 输入验证和错误处理

**关键方法**:
- `playVRVideo()` - 启动VR播放
- `playNormalVideo()` - 启动普通播放

#### 2. 播放器模块 (VRPlayerActivity)
**职责**: 视频播放、控制和参数管理
- 播放器初始化
- 控制面板管理
- 模式切换处理

**关键方法**:
- `setupPlayer()` - 初始化播放器
- `togglePlayPause()` - 控制播放
- `setPlaySpeed()` - 设置播放速度
- `showPlaySpeedMenu()` - 显示速度菜单
- `toggleVRMode()` - 切换VR/普通模式

#### 3. VR渲染模块 (VRRenderView + VRRenderer)
**职责**: OpenGL渲染、陀螺仪处理

**VRRenderView - 容器视图**
- GLSurfaceView扩展
- 陀螺仪事件处理
- VR参数管理

**VRRenderer - 渲染引擎**
- OpenGL着色器编译
- 球形网格生成
- 立体投影矩阵计算
- 纹理映射和绘制

**关键方法**:
- `enableGyroControl()` - 启用陀螺仪
- `onSensorChanged()` - 处理陀螺仪事件
- `onDrawFrame()` - 渲染回调
- `drawEye()` - 绘制单个眼睛视图

#### 4. 工具模块
**VideoUrlUtils** - URL处理
- URL验证
- 格式识别
- MIME类型判断

**VRSettings** - VR参数管理
- 参数范围定义
- 参数验证和约束
- 默认值管理

**TimeFormatter** - 时间格式化
- 毫秒转HH:MM:SS
- 秒数转MM:SS
- 自动选择最优格式

## 数据流

### 视频播放流程

```
用户输入URL
    ↓
URL验证 (VideoUrlUtils)
    ↓
创建MediaItem
    ↓
ExoPlayer加载
    ↓
选择渲染方式
    ├→ VR模式: VRRenderView渲染
    └→ 普通模式: PlayerView渲染
    ↓
播放开始
```

### VR视角更新流程

```
陀螺仪事件
    ↓
SensorManager.onSensorChanged()
    ↓
获取旋转向量
    ↓
转换为旋转矩阵
    ↓
设置到VRRenderer
    ↓
下一帧绘制时应用变换
    ↓
显示新视角
```

## 资源结构

### 布局资源 (layout/)

**activity_main.xml**
```xml
LinearLayout
├── TextView (标题)
├── EditText (URL输入)
├── Button (VR模式)
└── Button (普通模式)
```

**activity_vr_player.xml**
```xml
FrameLayout
├── FrameLayout (播放器容器)
└── LinearLayout (控制面板)
    ├── SeekBar (进度条)
    ├── LinearLayout (时间显示)
    └── LinearLayout (控制按钮)
```

### 数值资源 (values/)

| 资源文件 | 用途 |
|---------|------|
| strings.xml | 所有可本地化的文本 |
| colors.xml | 色彩定义 |
| dimens.xml | 尺寸定义 |
| themes.xml | 应用主题 |

## 外部依赖

### Gradle依赖

| 库 | 版本 | 用途 |
|----|------|------|
| androidx.appcompat | 1.6.1 | Android兼容性库 |
| androidx.media3 | 1.1.1 | 视频播放引擎 |
| com.google.vr | 2.0.8 | VR功能支持 |
| com.squareup.okhttp3 | 4.10.0 | HTTP网络请求 |
| com.squareup.retrofit2 | 2.9.0 | REST API客户端 |
| com.google.code.gson | 2.10.1 | JSON处理 |

### Android权限

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## 渲染管线详解

### 顶点着色器
```glsl
attribute vec3 vPosition;
attribute vec2 vTexCoord;
varying vec2 fragTexCoord;
uniform mat4 uMVPMatrix;

void main() {
    gl_Position = uMVPMatrix * vec4(vPosition, 1.0);
    fragTexCoord = vTexCoord;
}
```

### 片元着色器
```glsl
precision mediump float;
varying vec2 fragTexCoord;
uniform sampler2D uTexture;

void main() {
    gl_FragColor = texture2D(uTexture, fragTexCoord);
}
```

### 立体渲染
1. 创建正投影矩阵
2. 为左眼渲染 (viewport: 0, 0, 540, 1080)
3. 为右眼渲染 (viewport: 540, 0, 540, 1080)
4. 两个视图的差异由瞳距参数控制

## 生命周期管理

### Activity生命周期
```
onCreate()
    ↓ 初始化UI和播放器
onResume()
    ↓ 启用陀螺仪
onPause()
    ↓ 暂停播放，禁用陀螺仪
onDestroy()
    ↓ 释放资源
```

### GLSurfaceView生命周期
```
onSurfaceCreated()
    ↓ 初始化OpenGL资源
onSurfaceChanged()
    ↓ 处理屏幕尺寸变化
onDrawFrame()
    ↓ 每帧调用，进行渲染
onDetachedFromWindow()
    ↓ 清理资源
```

## 性能考虑

### 内存优化
- 使用适当的纹理分辨率
- 及时释放ExoPlayer资源
- 避免频繁的对象创建

### 渲染优化
- 使用VBO和VAO提高渲染效率
- 帧率限制防止过度渲染
- 合理使用深度测试和背面剔除

### 网络优化
- 使用ExoPlayer的自适应码率
- 智能缓冲策略
- 支持断点续传

## 扩展建议

### 短期改进
1. 实现菜单UI系统
2. 添加手势控制
3. 支持本地视频文件
4. 视频加载进度显示

### 中期增强
1. 视频缓存机制
2. 字幕支持（SRT、VTT）
3. 音量和亮度控制
4. 截图和录制功能

### 长期规划
1. 云端同步播放进度
2. VR控制器支持
3. 空间音频渲染
4. 多用户在线观看

## 测试策略

### 单元测试
- VideoUrlUtils 功能测试
- VRSettings 参数约束测试
- TimeFormatter 格式化测试

### 集成测试
- 播放器初始化
- 模式切换功能
- 参数调整功能

### 功能测试
- URL输入验证
- 视频加载和播放
- 陀螺仪控制
- 速度/参数调整
- 模式切换

### 兼容性测试
- 不同Android版本
- 不同屏幕尺寸
- 不同网络条件

## 常见问题解决

### 渲染问题
- 检查着色器编译错误
- 验证纹理绑定
- 检查矩阵计算

### 陀螺仪问题
- 确认传感器可用
- 检查权限设置
- 校准陀螺仪

### 播放问题
- 验证URL格式
- 检查网络连接
- 查看ExoPlayer日志

## 项目统计

| 指标 | 数值 |
|------|------|
| Java文件 | 7个 |
| XML布局 | 2个 |
| XML资源 | 4个 |
| 总行数 | ~1500+ |
| API最低版本 | 24 |
| API目标版本 | 34 |

---

**文档版本**: 1.0
**更新日期**: 2026年2月3日
**作者**: VR Video Player开发团队

README.md - VR Video Player 说明文档

# VR Video Player - VR视频播放器应用

一款功能完整的Android VR视频播放器，支持360度全景视频播放和陀螺仪控制。

## 功能特性

### 核心功能
1. **URL视频输入** - 支持直接输入视频链接进行播放
2. **VR模式** - 将视频画面适配为VR显示（分屏显示供双眼观看）
3. **陀螺仪控制** - 支持手机陀螺仪控制视角旋转，提供沉浸式体验
4. **普通模式** - 支持普通视频播放模式切换

### 播放控制
- **播放/暂停** - 基本播放控制
- **播放速度** - 支持0.5x、0.75x、1.0x、1.25x、1.5x、2.0x多种速度
- **快进/快退** - 10秒快进或快退
- **进度条拖拽** - 精确拖拽定位播放位置

### VR参数设置
- **瞳距调整** - 可调整瞳距（眼镜光学参数）
- **屏幕大小** - 调整虚拟屏幕尺寸（3英寸到7英寸）
- **陀螺仪控制** - 启用/禁用陀螺仪视角控制

## 项目结构

```
VR Video Player/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/vrvideo/player/
│   │       │   ├── MainActivity.java                 # 主界面
│   │       │   ├── ui/
│   │       │   │   └── VRPlayerActivity.java        # 播放器界面
│   │       │   └── vr/
│   │       │       ├── VRRenderView.java            # VR渲染视图
│   │       │       └── VRRenderer.java              # OpenGL渲染器
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_main.xml            # 主界面布局
│   │       │   │   └── activity_vr_player.xml       # 播放器界面布局
│   │       │   └── values/
│   │       │       ├── strings.xml                  # 字符串资源
│   │       │       ├── colors.xml                   # 颜色资源
│   │       │       ├── dimens.xml                   # 尺寸资源
│   │       │       └── themes.xml                   # 主题资源
│   │       └── AndroidManifest.xml                  # 应用清单
│   ├── build.gradle                                # App级Gradle配置
│   └── proguard-rules.pro                          # ProGuard混淆规则
├── build.gradle                                    # 项目级Gradle配置
├── settings.gradle                                 # Gradle设置
└── gradle.properties                               # Gradle属性

```

## 技术栈

### 核心库
- **Android AndroidX** - 现代Android开发框架
- **ExoPlayer (Media3)** - 高性能视频播放引擎
- **Google VR SDK** - VR渲染和陀螺仪支持
- **OpenGL ES 2.0** - 3D图形渲染

### 网络和数据
- **OkHttp** - HTTP网络请求
- **Retrofit** - REST API客户端
- **GSON** - JSON序列化/反序列化

### 最低要求
- **API Level** - 24（Android 7.0）
- **Target API** - 34（Android 14）
- **编译SDK** - 34

## 使用方式

### 启动应用
1. 在主界面输入视频链接URL
2. 点击"VR模式"进入VR播放
3. 或点击"普通模式"进行普通播放

### VR模式使用
1. 进入VR模式后，屏幕分为左右两个视口
2. 移动手机观看不同角度的视频
3. 使用控制面板调整播放参数

### 控制面板操作
- **暂停/播放** - 控制视频播放
- **速度** - 调整播放速度
- **瞳距** - 调整眼镜光学参数
- **屏幕** - 调整虚拟屏幕大小
- **模式切换** - 在VR和普通模式之间切换

## 构建和运行

### 编译项目
```bash
./gradlew build
```

### 运行调试
```bash
./gradlew installDebug
```

## 硬件需求

- Android手机或平板
- 陀螺仪传感器（用于VR模式）
- 至少1GB RAM（推荐2GB以上）
- 支持OpenGL ES 2.0

## 开发要点

### VR渲染
- 使用OpenGL ES 2.0实现球形纹理映射
- 实现了立体渲染（左眼和右眼分别渲染）
- 支持陀螺仪旋转矩阵应用

### 播放器集成
- ExoPlayer作为视频播放引擎
- 支持多种视频格式（HLS、DASH、MP4等）
- 自适应比特率播放

### 陀螺仪控制
- 使用Android SensorManager监听陀螺仪事件
- 实时应用旋转矩阵到3D渲染

## 已知限制

1. VR视频必须为360度全景视频格式
2. 目前不支持字幕显示
3. 音频只支持立体声，不支持空间音频

## 后续开发计划

- [ ] 实现字幕支持
- [ ] 添加更多VR视频格式支持
- [ ] 实现视频缓存机制
- [ ] 添加观看历史记录
- [ ] 支持VR控制器交互
- [ ] 实现空间音频支持
- [ ] 添加视频截图功能

## 许可证

MIT License

## 联系方式

如有问题或建议，欢迎反馈。

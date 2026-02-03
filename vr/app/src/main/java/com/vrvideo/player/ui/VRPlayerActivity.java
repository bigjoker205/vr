package com.vrvideo.player.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.vrvideo.player.R;
import com.vrvideo.player.vr.VRRenderView;

public class VRPlayerActivity extends AppCompatActivity {

    private String videoUrl;
    private boolean vrMode;
    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    private VRRenderView vrRenderView;
    private FrameLayout playerContainer;
    private LinearLayout controlPanel;
    
    // 控制面板控件
    private Button pauseResumeButton;
    private SeekBar seekBar;
    private TextView currentTimeText;
    private TextView durationText;
    private Button playSpeedButton;
    private Button pupilDistanceButton;
    private Button screenSizeButton;
    private Button toggleModeButton;
    
    private float currentPlaySpeed = 1.0f;
    private float currentPupilDistance = 0.065f;
    private float currentScreenSize = 5.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_player);
        
        // 强制横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        // 获取Intent数据
        videoUrl = getIntent().getStringExtra("video_url");
        vrMode = getIntent().getBooleanExtra("vr_mode", true);
        
        initializeUI();
        setupPlayer();
    }

    /**
     * 初始化UI
     */
    private void initializeUI() {
        playerContainer = findViewById(R.id.playerContainer);
        controlPanel = findViewById(R.id.controlPanel);
        
        pauseResumeButton = findViewById(R.id.pauseResumeButton);
        seekBar = findViewById(R.id.seekBar);
        currentTimeText = findViewById(R.id.currentTimeText);
        durationText = findViewById(R.id.durationText);
        playSpeedButton = findViewById(R.id.playSpeedButton);
        pupilDistanceButton = findViewById(R.id.pupilDistanceButton);
        screenSizeButton = findViewById(R.id.screenSizeButton);
        toggleModeButton = findViewById(R.id.toggleModeButton);
        
        // 设置控制事件
        pauseResumeButton.setOnClickListener(v -> togglePlayPause());
        seekBar.setOnSeekBarChangeListener(createSeekBarListener());
        playSpeedButton.setOnClickListener(v -> showPlaySpeedMenu());
        pupilDistanceButton.setOnClickListener(v -> showPupilDistanceMenu());
        screenSizeButton.setOnClickListener(v -> showScreenSizeMenu());
        toggleModeButton.setOnClickListener(v -> toggleVRMode());
        
        updateToggleModeButtonText();
    }

    /**
     * 初始化播放器
     */
    private void setupPlayer() {
        exoPlayer = new ExoPlayer.Builder(this).build();
        
        if (vrMode) {
            setupVRPlayer();
        } else {
            setupNormalPlayer();
        }
        
        // 加载视频
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        
        // 添加播放器监听器
        exoPlayer.addListener(createPlayerListener());
    }

    /**
     * 设置VR播放器
     */
    private void setupVRPlayer() {
        vrRenderView = new VRRenderView(this);
        playerContainer.addView(vrRenderView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        
        // 启用陀螺仪控制
        vrRenderView.enableGyroControl();
        
        // 将ExoPlayer的视频输出连接到VR渲染器
        // 注：实际实现需要自定义VideoProcessor
    }

    /**
     * 设置普通播放器
     */
    private void setupNormalPlayer() {
        playerView = new PlayerView(this);
        playerView.setPlayer(exoPlayer);
        playerContainer.addView(playerView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    /**
     * 创建播放器监听器
     */
    private Player.Listener createPlayerListener() {
        return new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == Player.STATE_READY) {
                    long duration = exoPlayer.getDuration();
                    durationText.setText(formatTime(duration));
                    seekBar.setMax((int) duration);
                }
            }
            
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                updatePlayPauseButton();
            }
        };
    }

    /**
     * 切换播放/暂停
     */
    private void togglePlayPause() {
        if (exoPlayer.isPlaying()) {
            exoPlayer.pause();
        } else {
            exoPlayer.play();
        }
    }

    /**
     * 创建SeekBar监听器
     */
    private SeekBar.OnSeekBarChangeListener createSeekBarListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentTimeText.setText(formatTime(progress));
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                exoPlayer.seekTo(seekBar.getProgress());
            }
        };
    }

    /**
     * 显示播放速度菜单
     */
    private void showPlaySpeedMenu() {
        // TODO: 实现速度菜单选择
        float[] speeds = {0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
        // 示例：设置为1.5倍速
        setPlaySpeed(1.5f);
    }

    /**
     * 设置播放速度
     */
    private void setPlaySpeed(float speed) {
        currentPlaySpeed = speed;
        exoPlayer.setPlaybackSpeed(speed);
        playSpeedButton.setText("速度: " + speed + "x");
    }

    /**
     * 显示瞳距菜单
     */
    private void showPupilDistanceMenu() {
        // TODO: 实现瞳距调整菜单
        // 可以使用SeekBar或按钮来调整瞳距
        adjustPupilDistance(0.065f);
    }

    /**
     * 调整瞳距
     */
    private void adjustPupilDistance(float distance) {
        currentPupilDistance = distance;
        if (vrRenderView != null) {
            vrRenderView.setVRParameters(distance, currentScreenSize);
        }
        pupilDistanceButton.setText("瞳距: " + String.format("%.3f", distance));
    }

    /**
     * 显示屏幕大小菜单
     */
    private void showScreenSizeMenu() {
        // TODO: 实现屏幕大小调整菜单
        float[] sizes = {3.0f, 4.0f, 5.0f, 6.0f, 7.0f};
        // 示例：设置为5英寸
        setScreenSize(5.0f);
    }

    /**
     * 设置屏幕大小
     */
    private void setScreenSize(float size) {
        currentScreenSize = size;
        if (vrRenderView != null) {
            vrRenderView.setVRParameters(currentPupilDistance, size);
        }
        screenSizeButton.setText("屏幕: " + size + "\"");
    }

    /**
     * 切换VR/普通模式
     */
    private void toggleVRMode() {
        vrMode = !vrMode;
        // 重新初始化播放器
        playerContainer.removeAllViews();
        
        if (exoPlayer != null) {
            long currentPosition = exoPlayer.getCurrentPosition();
            boolean isPlaying = exoPlayer.isPlaying();
            exoPlayer.release();
        }
        
        exoPlayer = new ExoPlayer.Builder(this).build();
        
        if (vrMode) {
            setupVRPlayer();
        } else {
            setupNormalPlayer();
        }
        
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.addListener(createPlayerListener());
        
        updateToggleModeButtonText();
    }

    /**
     * 更新模式切换按钮文本
     */
    private void updateToggleModeButtonText() {
        toggleModeButton.setText(vrMode ? "普通模式" : "VR模式");
    }

    /**
     * 更新播放/暂停按钮
     */
    private void updatePlayPauseButton() {
        if (exoPlayer.isPlaying()) {
            pauseResumeButton.setText("暂停");
        } else {
            pauseResumeButton.setText("播放");
        }
    }

    /**
     * 格式化时间
     */
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    /**
     * 快进
     */
    public void fastForward() {
        long currentPosition = exoPlayer.getCurrentPosition();
        exoPlayer.seekTo(currentPosition + 10000); // 快进10秒
    }

    /**
     * 快退
     */
    public void fastBackward() {
        long currentPosition = exoPlayer.getCurrentPosition();
        exoPlayer.seekTo(Math.max(0, currentPosition - 10000)); // 快退10秒
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
        if (vrRenderView != null) {
            vrRenderView.disableGyroControl();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vrRenderView != null) {
            vrRenderView.enableGyroControl();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        if (vrRenderView != null) {
            vrRenderView.disableGyroControl();
        }
    }
}

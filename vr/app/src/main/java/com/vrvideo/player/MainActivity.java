package com.vrvideo.player;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vrvideo.player.ui.VRPlayerActivity;

public class MainActivity extends AppCompatActivity {

    private EditText urlInput;
    private Button playButton;
    private Button normalPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.urlInput);
        playButton = findViewById(R.id.playVRButton);
        normalPlayButton = findViewById(R.id.playNormalButton);

        playButton.setOnClickListener(v -> playVRVideo());
        normalPlayButton.setOnClickListener(v -> playNormalVideo());
    }

    /**
     * 播放VR视频
     */
    private void playVRVideo() {
        String url = urlInput.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(this, "请输入视频链接", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, VRPlayerActivity.class);
        intent.putExtra("video_url", url);
        intent.putExtra("vr_mode", true);
        startActivity(intent);
    }

    /**
     * 播放普通视频
     */
    private void playNormalVideo() {
        String url = urlInput.getText().toString().trim();
        if (url.isEmpty()) {
            Toast.makeText(this, "请输入视频链接", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, VRPlayerActivity.class);
        intent.putExtra("video_url", url);
        intent.putExtra("vr_mode", false);
        startActivity(intent);
    }
}

package com.example.my_nga_fornums.dongman;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;
import com.google.gson.Gson;

public class DongmanVideoActivity extends BaseActivity {

    TextView videoName, videoDescription;
    ImageView imageView;
    Button actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dongman_video);

        // 获取VideoView组件
        VideoView videoView = findViewById(R.id.videoView);

        // 视频URL
        String videoUrl = "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.webm";

        // 设置视频URI
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        // 创建MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // 开始播放视频
        videoView.start();

        videoName = findViewById(R.id.videoName);
        videoDescription = findViewById(R.id.videoDescription);
        imageView = findViewById(R.id.imageView);
        actionButton = findViewById(R.id.actionButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 获取html页面的连接
        String url1 = getIntent().getStringExtra("DongmanInfo");
        DongmanBean dongmanInfo = new Gson().fromJson(getIntent().getStringExtra("DongmanInfo"), DongmanBean.class);
        String name = dongmanInfo.getName();
        String summary = dongmanInfo.getSummary();

        videoName.setText(name);
        videoDescription.setText(summary);

        if (dongmanInfo.getImage() == null){

        }else{
            String url = dongmanInfo.getImage();
            Log.d("url", url);

            // 使用Glide加载网络图片
            Glide.with(this)
                    .load(url)
                    .into(imageView);
        }

        //button点击按钮跳转页面
        actionButton.setOnClickListener(v -> {
            // 跳转到DongmanWebActivity
            Intent intent = new Intent(DongmanVideoActivity.this, DongmanWebActivity.class);
            intent.putExtra("pageUrl", "https://www.bing.com/search?q=123"+name);
            startActivity(intent);



        });

    }
}

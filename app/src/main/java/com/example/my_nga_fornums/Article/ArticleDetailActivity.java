package com.example.my_nga_fornums.Article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.my_nga_fornums.MainActivity;
import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleDetailActivity extends BaseActivity {
    public static final String ARTICLE_TITLE = "article_title";

    public static final String ARTICLE_IMAGE_ID = "article_image_id";

    public static final String ARTICLE_TIME = "article_time";


    private String articleTitle, articleImageId, articleTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // 获取启动该活动的intent
        Intent intent = getIntent();

        articleTitle = intent.getStringExtra(ARTICLE_TITLE);
        articleImageId = intent.getStringExtra(ARTICLE_IMAGE_ID);
        articleTime = intent.getStringExtra(ARTICLE_TIME);
        // 测试 TODO
        System.out.println("当前文章的标题为：" + articleTitle);
        System.out.println("当前图片的地址为：" + articleImageId);
        System.out.println("当前文章的发布时间为：" + articleTime);

        // 设置工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.article_detail_toolbar);
        setSupportActionBar(toolbar);

        // 启用HomeAsUp按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }

        // 获取视图
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView articleImageView = (ImageView) findViewById(R.id.article_image_view);
        TextView articleContentText = (TextView) findViewById(R.id.article_content_text);
        TextView articleAuthor = findViewById(R.id.article_author11);
        TextView articlePublishTime = findViewById(R.id.article_time11);

        // 查询数据库，获取文章信息
        List<Article> articles = LitePal.where("userId = ? AND articleTitle = ? AND articleTime = ?",
                MainActivity.currentUserId, articleTitle, articleTime).find(Article.class);

        // 根据查询到的信息为视图填充内容
        collapsingToolbarLayout.setTitle(articleTitle);
        articlePublishTime.setText(articleTime);
        articleAuthor.setText(articles.get(0).getArticleAuthor());
        articleContentText.setText(articles.get(0).getArticleContent());
        // 使用Glide加载图片
        Glide.with(this).load(articleImageId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(articleImageView);

        // 删除文章的功能
        FloatingActionButton delBtn = (FloatingActionButton) findViewById(R.id.delete_article);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新建一个对话提示是否删除
                new MaterialDialog.Builder(ArticleDetailActivity.this)
                        .title("提示")
                        .content("是否删除此文章？")
                        // 为该对话框添加删除事件
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DeleteArticle(dialog, which);
                            }
                        })
                        .positiveText("确认")
                        .negativeText("取消")
                        .show();
            }
        });

    }

    // 删除事件
    private void DeleteArticle(MaterialDialog dialog, DialogAction which){
        int isOk = LitePal.deleteAll(Article.class, "userId = ? AND articleTitle = ? AND articleTime = ?",
                MainActivity.currentUserId, articleTitle, articleTime);
        if(isOk > 0)
            Toast.makeText(ArticleDetailActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ArticleDetailActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable(){
            @Override
            public  void run(){
                try{
                    // 构建请求
                    OkHttpClient client = new OkHttpClient();
                    String url = "http://101.42.105.71:9999/article/delete?Id=" + articleTitle;
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();

                    // 执行请求
                    Response response = client.newCall(request).execute();
                    assert response.body() != null;
                    String responseData = response.body().string();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        // 启动目标活动
        Intent intent = new Intent();
        intent.setClass(ArticleDetailActivity.this, ArticleActivity.class);
        intent.putExtra("user_article_id", MainActivity.currentUserId);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 如果点击的是HomeAsUp按钮
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}

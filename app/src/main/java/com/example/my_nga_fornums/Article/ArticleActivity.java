package com.example.my_nga_fornums.Article;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends BaseActivity {
    private List<Article> articleList = new ArrayList<>();
    private ArticleAdapter adapter;
    private String userIdNumber;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        userIdNumber = getIntent().getStringExtra("user_article_id");
        // 测试 TODO
        System.out.println("当前用户账号为：" + userIdNumber);

        // 设置工具栏
        Toolbar toolbar = findViewById(R.id.article_toolbar);
        toolbar.setTitle("我的文章");
        setSupportActionBar(toolbar);

        // 启用HomeAsUp按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }

        // 给编辑按钮添加事件
        FloatingActionButton editBtn = (FloatingActionButton) findViewById(R.id.article_editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到编辑文章页面
                Intent editArticleIntent = new Intent(ArticleActivity.this, EditArticleActivity.class);
                editArticleIntent.putExtra("userId", userIdNumber);
                startActivityForResult(editArticleIntent, 7);
            }
        });
        // 刷新文章列表
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        InitArticles();
    }

    // 初始化文章列表的方法
    private void InitArticles(){
        articleList.clear();
        List<Article> articles = LitePal.where("userId = ?", userIdNumber).find(Article.class);
        articleList.addAll(articles);
        adapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // 处理菜单项点击事件
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                ArticleActivity.this.finish();
                break;
        }
        return true;
    }

    // 接收返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 7:
                if(resultCode == RESULT_OK)
                    InitArticles();
                break;
        }
    }
}

package com.example.my_nga_fornums.news;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.my_nga_fornums.MainActivity;
import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;
import org.litepal.LitePal;

import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {

    private WebView webView;
    private Toolbar navToolbar, commentToolBar;
    private String urlData, pageUniquekey, pageTtile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webView);
        navToolbar = findViewById(R.id.toolbar_webView);
        commentToolBar = findViewById(R.id.toolbar_webComment);

        findViewById(R.id.toolbar_webComment).bringToFront();

        // 获取html页面的连接
        urlData = getIntent().getStringExtra("pageUrl");
        pageUniquekey = getIntent().getStringExtra("uniquekey");
        pageTtile = getIntent().getStringExtra("news_title");

        // 设置WebView
        setupWebView();

        // 设置Toolbar
        setupToolbar();

        commentToolBar.setOnMenuItemClickListener(menuItemClickListener);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }
    }

    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(false);

        webView.loadUrl(urlData);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                checkCollectionStatus();
            }


        });

        webView.setWebChromeClient(new WebChromeClient());
    }

    private void setupToolbar() {
        setSupportActionBar(commentToolBar);
        navToolbar.setTitle("A岛");
        setSupportActionBar(navToolbar);
        commentToolBar.inflateMenu(R.menu.tool_webbottom);
        commentToolBar.setTitle("感谢观看");
    }

    private void checkCollectionStatus() {
        List<NewsCollectBean> beanList = LitePal.where("userIdNumer = ? AND newsId = ?", MainActivity.currentUserId == null ? "" : MainActivity.currentUserId, pageUniquekey).find(NewsCollectBean.class);
        MenuItem u = commentToolBar.getMenu().getItem(0);
        if(beanList.size() > 0) {
            u.setIcon(R.drawable.ic_star_border_favourite_yes);
        } else {
            u.setIcon(R.drawable.ic_star_border_favourite_no);
        }
    }

    private final Toolbar.OnMenuItemClickListener menuItemClickListener = menuItem -> {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.news_share) {
            shareNews();
        } else if (itemId == R.id.news_collect) {
            handleCollection();
        }
        return true;
    };

    private void shareNews() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, urlData);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    private void handleCollection() {
        if (!TextUtils.isEmpty(MainActivity.currentUserId)) {
            MenuItem u = commentToolBar.getMenu().getItem(0);
            List<NewsCollectBean> bean = LitePal.where("userIdNumer = ? AND newsId = ?", MainActivity.currentUserId, pageUniquekey).find(NewsCollectBean.class);
            String answer = "";
            if (bean.size() > 0) {
                int i = LitePal.deleteAll(NewsCollectBean.class, "userIdNumer = ? AND newsId = ?", MainActivity.currentUserId, pageUniquekey);
                if (i > 0) {
                    answer = "取消收藏！";
                    u.setIcon(R.drawable.ic_star_border_favourite_no);
                } else answer = "取消失败！";
            } else {
                NewsCollectBean currentNews = new NewsCollectBean();
                currentNews.setUserIdNumer(MainActivity.currentUserId);
                currentNews.setNewSTitle(pageTtile);
                currentNews.setNewsId(pageUniquekey);
                currentNews.setNewsUrl(urlData);
                boolean isSave = currentNews.save();
                if (isSave) {
                    answer = "收藏成功！";
                    u.setIcon(R.drawable.ic_star_border_favourite_yes);
                } else answer = "收藏失败！";
            }
            Toast.makeText(WebActivity.this, answer, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(WebActivity.this, "请先登录后再收藏！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        webView.getSettings().setJavaScriptEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        } else if (itemId == R.id.news_setting) {
            Toast.makeText(this, "夜间模式", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.news_feedback) {
            Toast.makeText(this, "举报！", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}

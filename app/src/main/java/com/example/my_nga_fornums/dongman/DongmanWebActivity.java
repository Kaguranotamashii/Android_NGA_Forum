package com.example.my_nga_fornums.dongman;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
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
import com.example.my_nga_fornums.news.NewsCollectBean;
import com.example.my_nga_fornums.tools.BaseActivity;
import org.litepal.LitePal;
import java.util.List;

//若需要启用Javascript，则抑制其警告
@SuppressLint("SetJavaScriptEnabled")
public class DongmanWebActivity extends BaseActivity {

    private WebView webView;

    private Toolbar navToolbar, commentToolBar;

    private String urlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = (WebView) findViewById(R.id.webView);
        navToolbar = (Toolbar) findViewById(R.id.toolbar_webView);
        commentToolBar = (Toolbar) findViewById(R.id.toolbar_webComment);
        //将底部评论框的toolbar放在主界面上
        findViewById(R.id.toolbar_webComment).bringToFront();
    }

    //活动由不可见变为可见时调用
    @Override
    protected void onStart() {
        super.onStart();
        // 获取html页面的连接
        urlData = getIntent().getStringExtra("pageUrl");

        // 通过WebView中的getSettings方法获得一个WebSettings对象
        WebSettings settings = webView.getSettings();
        // 详细讲解：https://www.jianshu.com/p/0d7d429bd216
        // 开启javascript：h5页要一般都有js,设置为true才允许h5页面执行js，但开启js非常耗内存，经常会导致oom，
        // 为了解决这个问题，可以在onStart方法中开启，在onStop中关闭。
        settings.setJavaScriptEnabled(true);

        //设置支持缩放
        settings.setSupportZoom(true);
        // 设置出现缩放工具。若为false，则该WebView不可缩放
        settings.setBuiltInZoomControls(true);
        // 设置允许js弹出alert对话框
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 设置webview推荐使用的窗口，使html界面自适应屏幕
        // 原因讲解：https://blog.csdn.net/SCHOLAR_II/article/details/80614486
        settings.setUseWideViewPort(true);
        // 设置WebView底层的布局算法，参考LayoutAlgorithm#NARROW_COLUMNS，将会重新生成WebView布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        // 设置缩放至屏幕的大小
        settings.setLoadWithOverviewMode(true);
        // 隐藏webview缩放按钮
        settings.setDisplayZoomControls(false);
        // 加载网页连接
        webView.loadUrl(urlData);

        // Toolbar通过setSupportActionBar(toolbar) 被修饰成了actionbar。
        setSupportActionBar(commentToolBar);
        // 设置菜单栏标题
        setSupportActionBar(navToolbar);


        webView.setWebViewClient(new WebViewClient() {
            // 重写此方法可以让webView处理https请求。
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();// 左上角的id
        if (itemId == android.R.id.home) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            // 结束当前活动
            DongmanWebActivity.this.finish();
        } else if (itemId == R.id.news_setting) {
            Toast.makeText(this, "夜间模式", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.news_feedback) {
            Toast.makeText(this, "举报！", Toast.LENGTH_SHORT).show();
        }
        return true;
    }



}
package com.example.my_nga_fornums.Article;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;
import com.example.my_nga_fornums.user.UserInfo;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditArticleActivity extends BaseActivity {
    // 视图控件
    private ImageView editImageView;
    private EditText editTitle, editContent;
    private Button publishBtn;

    // 常量，表示选择图片的请求码
    public static final int CHOOSE_ARTICLE_IMAGE = 22;

    // 编辑图片的路径
    private String editImagePath = "";

    // 创建文章对象
    private Article article = new Article();

    // 用户ID
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        // 获取用户ID
        userId = getIntent().getStringExtra("userId");

        // 设置工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.article_edit_toolbar);
        toolbar.setTitle("编辑文章");
        setSupportActionBar(toolbar);

        // 启用HomeAsUp按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }

        // 初始化控件
        editImageView = findViewById(R.id.edit_picture);
        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        publishBtn = findViewById(R.id.edit_publishBtn);

        // 从数据库获取用户信息
        List<UserInfo> userInfoList = LitePal.where("userAccount = ?", userId).find(UserInfo.class);
        article.setUserId(userId);
        article.setArticleAuthor(userInfoList.get(0).getNickName());
        System.out.println("当前文章详情为：" + article);

        // 设置图片点击监听器
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditArticleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(EditArticleActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                else
                    OpenAlbum();
            }
        });

        // 设置发布按钮的点击事件
        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editTitle.getText()) || TextUtils.isEmpty(editContent.getText()))
                    Toast.makeText(EditArticleActivity.this,"输入不能为空", Toast.LENGTH_SHORT).show();
                else{
                    // 设置文章内容
                    article.setArticleTitle(editTitle.getText().toString());
                    article.setArticleContent(editContent.getText().toString());
                    article.setArticleImagePath(editImagePath);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    // 测试 TODO
                    System.out.println(simpleDateFormat.format(calendar.getTime()));
                    article.setArticleTime(simpleDateFormat.format(calendar.getTime()));

                    // 保存文章到数据库
                    boolean save = article.save();
                    if(save) {
                        Toast.makeText(EditArticleActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(EditArticleActivity.this, "发布失败！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    // 网络上传文章
                    String title = editTitle.getText().toString();
                    String content = editContent.getText().toString();
                    String userID = userId;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 从ImageView 获取 Bitmap
                                editImageView.setDrawingCacheEnabled(true);
                                Bitmap bitmap = Bitmap.createBitmap(editImageView.getDrawingCache());
                                editImageView.setDrawingCacheEnabled(false);

                                // 将bitmap保存为文件
                                File imageFile = new File(getCacheDir(), "image.jpg");
                                FileOutputStream fos = new FileOutputStream(imageFile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
                                fos.flush();
                                fos.close();

                                OkHttpClient client = new OkHttpClient();
                                // 创建文件的请求体R
                                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), imageFile);

                                // 创建multipart from body
                                RequestBody requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("title", title)
                                        .addFormDataPart("content", content)
                                        .addFormDataPart("image", imageFile.getName(), fileBody)
                                        .build();

                                // 构建请求
                                Request request = new Request.Builder()
                                        .url("http://101.42.105.71:9999/article/addArticle")
                                        .post(requestBody)
                                        .build();

                                // 执行请求
                                Response response = client.newCall(request).execute();
                                assert response.body() != null;
                                String data = response.body().string();
                                Log.d("123456789",data);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();

                    // 跳转到我的文章界面
                    Intent intent = new Intent();
                    intent.setClass(EditArticleActivity.this, ArticleActivity.class);
                    intent.putExtra("user_article_id", userID);
                    startActivity(intent);
                }
            }
        });
    }

    // 打开相册
    private void OpenAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_ARTICLE_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // 安卓13及以上版本
                    if (ContextCompat.checkSelfPermission(EditArticleActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED) {
                        // 请求权限
                        ActivityCompat.requestPermissions(EditArticleActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
                    } else {
                        OpenAlbum();
                    }

                } else {
                    // 安卓12及以下版本
                    if (ContextCompat.checkSelfPermission(EditArticleActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        // 请求权限
                        ActivityCompat.requestPermissions(EditArticleActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        // 打开图库
                        OpenAlbum();
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_ARTICLE_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKiKat(data);
                    } else {
                        handleImageBeforeKiKat(data);
                    }
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKiKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如或是content类型的URI就使用普通方法处理
            imagePath = getImagePath(uri, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的直接获取图片路径就行
            imagePath = uri.getPath();
        }
        editImagePath = imagePath;
        // 根据图片路径显示图片
        diplayImage(imagePath);
    }

    private void handleImageBeforeKiKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        diplayImage(imagePath);
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void diplayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            editImageView.setImageBitmap(bitmap);
        } else {
            editImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                // 提醒刷新列表
                setResult(RESULT_OK, intent);
                EditArticleActivity.this.finish();
                return true;
        }
        return true;
    }
}

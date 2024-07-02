package com.example.my_nga_fornums;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import com.example.my_nga_fornums.Article.ArticleActivity;
import com.example.my_nga_fornums.dongman.DongmanFragment;
import com.example.my_nga_fornums.tools.BaseActivity;
import com.example.my_nga_fornums.user.LoginActivity;
import com.example.my_nga_fornums.user.UserDetailActivity;
import com.example.my_nga_fornums.user.UserFavoriteActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> list;
    private TextView userNickName, userSignature;
    private ImageView userAvatar;
    // 采用静态变量来存储当前登录的账号
    public static String currentUserId;
    // 记录读者账号，相当于Session来使用
    private String currentUserNickName, currentSignature, currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //打印日志

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        //注意：只需第一次创建或升级本地数据库，第二次运行就注释掉
        Connector.getDatabase();
        //Toast.makeText(MainActivity.this, "创建数据库成功", Toast.LENGTH_LONG).show();
        //获取抽屉布局实例
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //获取菜单控件实例
        navigationView = (NavigationView) findViewById(R.id.nav_design);
        //无法直接通过findViewById方法获取header布局id
        View v = navigationView.getHeaderView(0);
        CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.icon_image);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        list = new ArrayList<>();
    }

    //在活动由不可见变为可见的时候调用
    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setLogo(R.drawable.tubiao);
        //设置标题栏标题
        toolbar.setTitle("A岛");
        //设置自定义的标题栏实例
        setSupportActionBar(toolbar);
        //获取到ActionBar的实例
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //通过HomeAsUp来让导航按钮显示出来
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置Indicator来添加一个点击图标（默认图标是一个返回的箭头）
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        //设置菜单项的监听事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //逻辑页面处理
                mDrawerLayout.closeDrawers();
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_edit) {//每个菜单项的点击事件，通过Intent实现点击item简单实现活动页面的跳转。
                    if (!TextUtils.isEmpty(currentUserId)) {
                        Intent editIntent = new Intent(MainActivity.this, UserDetailActivity.class);
                        editIntent.putExtra("user_edit_id", currentUserId);
                        startActivityForResult(editIntent, 3);
                    } else {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        loginIntent.putExtra("loginStatus", "请先登录后才能操作！");
                        startActivityForResult(loginIntent, 1);
                    }
                } else if (itemId == R.id.nav_articles) {// 我发布的文章
                    if (!TextUtils.isEmpty(currentUserId)) {
                        Intent editIntent = new Intent(MainActivity.this, ArticleActivity.class);
                        editIntent.putExtra("user_article_id", currentUserId);
                        startActivityForResult(editIntent, 6);
                    } else {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        loginIntent.putExtra("loginStatus", "请先登录后才能操作！");
                        startActivityForResult(loginIntent, 1);
                    }
                } else if (itemId == R.id.nav_favorite) {
                    if (!TextUtils.isEmpty(currentUserId)) {
                        Intent loveIntent = new Intent(MainActivity.this, UserFavoriteActivity.class);
                        loveIntent.putExtra("user_love_id", currentUserId);
                        startActivity(loveIntent);
                    } else {
                        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                        loginIntent.putExtra("loginStatus", "请先登录后才能操作！");
                        startActivityForResult(loginIntent, 1);
                    }
                } else if (itemId == R.id.nav_clear_cache) {// 清除缓存
                    // Toast.makeText(MainActivity.this,"你点击了清除缓存，下步实现把",Toast.LENGTH_SHORT).show();
//                    clearCacheData();
                } else if (itemId == R.id.nav_switch) {// 切换账号
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    // 登录请求码是1
                    startActivityForResult(intent, 1);
                }
                return true;
            }
        });
        //设置tab标题
        list.add("ACG新闻");
        list.add("随机一图");

//TODO 根据不同页面碎片响应
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return list.get(position);
            }
            @Override
            public Fragment getItem(int position) {
                String tabTitle = list.get(position);
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                fragment = new DongmanFragment();
                bundle.putString("name", "shehui");
//                if (tabTitle.equals("ACG新闻")) {
////                    fragment = new NewsFragment();
//                    bundle.putString("name", "top");
//                } else if (tabTitle.equals("随机一图")) {
//                    fragment = new DongmanFragment();
//                    bundle.putString("name", "shehui");
//                } else {
////                    fragment = new NewsFragment(); // 默认的Fragment
//                }

                fragment.setArguments(bundle);
                return fragment;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return FragmentStatePagerAdapter.POSITION_NONE;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        // TODO 加载上次登录的账号，起到记住会话的功能

    }

    // 解析、展示图片
    private void diplayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            userAvatar.setImageBitmap(bitmap);
        } else {
            userAvatar.setImageResource(R.drawable.no_login_avatar);
        }
    }


    // 通过登录来接收值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        View v = navigationView.getHeaderView(0);
        userNickName = v.findViewById(R.id.text_nickname);
        userSignature = v.findViewById(R.id.text_signature);

        switch (requestCode) {
            case 1: // 切换账号登录后来主界面
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    currentUserId = data.getStringExtra("userID");
                    currentUserNickName = data.getStringExtra("userNick");
                    currentSignature = data.getStringExtra("userSign");
                    currentImagePath = data.getStringExtra("imagePath");
                    userNickName.setText(currentUserNickName);
                    userSignature.setText(currentSignature);
                    diplayImage(currentImagePath);
                }
                break;
            case 3: // 从个人信息返回来的数据，要更新导航栏中的数据，包括昵称，签名，图片路径
                if (resultCode == RESULT_OK) {
                    currentUserNickName = data.getStringExtra("nickName");
                    currentSignature = data.getStringExtra("signature");
                    currentImagePath = data.getStringExtra("imagePath");
                    userNickName.setText(currentUserNickName);
                    userSignature.setText(currentSignature);
                    diplayImage(currentImagePath);
                }
                break;
            default:
                break;
        }
    }


 //TODO 缓存功能？

    //加载标题栏的菜单布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //获取toolbar菜单项
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

//    根据点击的菜单项ID执行相应的操作，如打开侧滑菜单、显示用户反馈对话框、退出应用。
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 获取点击的菜单项的ID
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            // 打开侧滑菜单
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (itemId == R.id.userFeedback) {
            //填写用户反馈
            new MaterialDialog.Builder(MainActivity.this)
                    .title("用户反馈")
                    .inputRangeRes(1, 50, R.color.colorBlack)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input(null, null, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            Toast.makeText(MainActivity.this, "反馈成功！反馈内容为：" + input, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .positiveText("确定")
                    .negativeText("取消")
                    .show();
        } else if (itemId == R.id.userExit) {
            // 显示退出应用确认对话框
            SweetAlertDialog mDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("提示")
                    .setContentText("您是否要退出？")
                    .setCancelText("取消")
                    .setConfirmText("确定")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            //TODO 摧毁ActivityCollector中的所有Activity
                            MainActivity.this.finish();

                        }
                    });
            mDialog.show();
        }
        return true;
    }

    //TODO 加载数据


}
package com.example.my_nga_fornums.user;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.InputType;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;

import org.litepal.LitePal;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener{

    private static final int OPEN_GALLERY_REQUEST_CODE = 15;
    private ImageView userAvatar;
    private Toolbar detailToolbar;
    //private String userId;
    private UserInfo userInfo;
    // 定义线性布局
    private LinearLayout layout_avatar, layout_nickname, layout_sex, layout_birth, layout_signature;
    private TextView showNickName, showSex, showBirthday, showSignature;
    private ImageView Img;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        detailToolbar = findViewById(R.id.userData_toolbar);
        detailToolbar.setTitle("个人信息");

        //将当前Toolbar作为活动Bar
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        //为活动Bar设置一个返回按钮（没啥用感觉）
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }

        //获取每条信息栏组件
        layout_avatar = findViewById(R.id.lay_avatar);
        layout_nickname = findViewById(R.id.lay_nickname);
        layout_sex = findViewById(R.id.lay_sex);
        layout_birth = findViewById(R.id.lay_birthday);
        layout_signature = findViewById(R.id.lay_signature);

        //设置点击监听器
        layout_avatar.setOnClickListener(this);
        layout_nickname.setOnClickListener(this);
        layout_sex.setOnClickListener(this);
        layout_birth.setOnClickListener(this);
        layout_signature.setOnClickListener(this);

        //获取每条信息栏中可编辑部分组件
        userAvatar = findViewById(R.id.user_avatar);
        showNickName = findViewById(R.id.show_name);
        showSex = findViewById(R.id.show_sex);
        showBirthday = findViewById(R.id.show_birthday);
        showSignature = findViewById(R.id.show_sign);

        initData(getIntent().getStringExtra("user_edit_id"));
        //日期相关
        calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        if (!TextUtils.isEmpty(userInfo.getUserBirthDay()) && !userInfo.getUserBirthDay().equals("待完善")) {
            try {
                date = format.parse(userInfo.getUserBirthDay());
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
    private void initData(String userId) {
        userInfo = new UserInfo();
        userInfo.setNickName("syr");
        userInfo.setUserSex("男");
        userInfo.setUserBirthDay("2000-11-1");
        userInfo.setUserSignature("hellow world");

        //List<UserInfo> infos = LitePal.where("userAccount = ?", userId).find(UserInfo.class);
        //userInfo = infos.get(0);
        System.out.println("用户详情界面的信息为" + userInfo);
        showNickName.setText(userInfo.getNickName());
        showSex.setText(userInfo.getUserSex());
        showBirthday.setText(userInfo.getUserBirthDay());
        showSignature.setText(userInfo.getUserSignature());
        String curImagePath = userInfo.getImagePath();
        diplayImage(curImagePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onClick(View v) {

        int layoutId = v.getId();
        if (layoutId == layout_avatar.getId()){
            //是否拥有存储内容获取权限
            if (ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED){
                //请求存储内容获取权限
                ActivityCompat.requestPermissions(UserDetailActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            }
            else{
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , "image/*");
                startActivityForResult(intent, OPEN_GALLERY_REQUEST_CODE);
            }
        }
        else if(layoutId == layout_nickname.getId()){

            new MaterialDialog.Builder(UserDetailActivity.this)
                    .title("修改昵称")
                    .inputRangeRes(2, 8, R.color.colorBlack)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("请输入要修改的昵称", userInfo.getNickName(),new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            // CharSequence的值是可读可写序列，而String的值是只读序列。
                            // 重新设置值，当前活动被销毁时才保存到数据库
                            userInfo.setNickName(input.toString());
                            showNickName.setText(userInfo.getNickName());
                        }
                    })
                    .positiveText("确定")
                    .show();

        }
        else if(layoutId == layout_sex.getId()){

            new MaterialDialog.Builder(UserDetailActivity.this)
                    .title("修改性别")
                    .items(new String[]{"男", "女"})
                    .itemsCallbackSingleChoice(userInfo.getUserSex().equals("女") ? 1 : 0,new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            //System.out.println("选择哪一个" + which);
                            //System.out.println("选择的内容是" + text);
                            userInfo.setUserSex(text.toString());
                            showSex.setText(userInfo.getUserSex());
                            return true;
                        }
                    })
                    .show();

        }
        else if(layoutId == layout_birth.getId()){

            DatePickerDialog dialog = new DatePickerDialog(UserDetailActivity.this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    String birth = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    System.out.println(birth);
                    userInfo.setUserBirthDay(birth);
                    showBirthday.setText(birth);
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }
        else if(layoutId == layout_signature.getId()){

            new MaterialDialog.Builder(UserDetailActivity.this)
                    .title("修改个性签名")
                    .inputRangeRes(1, 38, R.color.colorBlack)
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("请输入要修改的个性签名", userInfo.getUserSignature(), new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {

                            System.out.println(input.toString());
                            userInfo.setUserSignature(input.toString());
                            showSignature.setText(userInfo.getUserSignature());
                        }
                    })
                    .positiveText("确定")
                    .show();

        }
    }
    //更换头像
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_GALLERY_REQUEST_CODE) { // 检测请求码
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                String imagePath = getImagePath(uri, null);
                diplayImage(imagePath);
            }
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
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
            userAvatar.setImageBitmap(bitmap);
        } else {
            userAvatar.setImageResource(R.drawable.akl);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 保存更改的数据
                userInfo.save();
                Intent intent = new Intent();
                intent.putExtra("nickName", showNickName.getText().toString());
                intent.putExtra("signature", showSignature.getText().toString());
                intent.putExtra("imagePath", userInfo.getImagePath());
                setResult(RESULT_OK, intent);
                System.out.println("当前个人信息活动页被销毁！！！");
                UserDetailActivity.this.finish();
                break;
        }
        return true;
    }

}


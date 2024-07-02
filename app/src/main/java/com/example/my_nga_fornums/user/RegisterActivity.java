package com.example.my_nga_fornums.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;
import org.litepal.LitePal;
import java.util.List;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = this.findViewById(R.id.register_click);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        EditText userAccount = findViewById(R.id.register_userAccount);
        EditText userPwd = findViewById(R.id.register_pwd);
        EditText userRePwd = findViewById(R.id.confirm_pwd);

        String userid = userAccount.getText().toString();
        String uspwd = userPwd.getText().toString();
        String usrepwd = userRePwd.getText().toString();

        if(userid.isEmpty() || uspwd.isEmpty() || usrepwd.isEmpty()) {
            // 判断字符串是否为null或者""
            Toast.makeText(RegisterActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        }
        else{
            // 判断两次输入的密码是否匹配，匹配则写入数据库，并且结束当前活动，自动返回登录界面
            if(uspwd.equals(usrepwd)) {
                List<UserInfo> userInfoList = LitePal.where("userAccount = ?", userid).find(UserInfo.class);
                if(!userInfoList.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "当前账号已被注册，请重新输入账号", Toast.LENGTH_SHORT).show();
                }
                else{
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserAccount(userid);
                    userInfo.setUserPwd(uspwd);
                    userInfo.setUserBirthDay("待完善");
                    userInfo.setUserSex("待完善");
                    userInfo.setUserSignature("这个人很懒，TA什么也没留下。");
                    // 给其设置一个用户名
                    userInfo.setNickName("用户" + (LitePal.findAll(UserInfo.class).size() + 1));
                    userInfo.save();
                    System.out.println(userInfo);
                    Intent intent = new Intent();
                    intent.putExtra("register_status", "注册成功");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
            else{
                Toast.makeText(RegisterActivity.this, "请确认输入密码与确认密码是否一致?", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

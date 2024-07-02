package com.example.my_nga_fornums.user;

import com.example.my_nga_fornums.MainActivity;


import com.example.my_nga_fornums.R;
import com.example.my_nga_fornums.tools.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.litepal.LitePal;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener  {

    private Button loginBtn;
    private Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerBtn = findViewById(R.id.login_register);
        loginBtn = findViewById(R.id.login_on);
    }
    @Override
    public void onClick(View view){
        Toast.makeText(LoginActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        String userAccount = findViewById(R.id.login_userAccount).toString();
        String userPwd = findViewById(R.id.login_pwd).toString();

        if(view.getId()==loginBtn.getId()){
            // 先判断输入不能为空
            if(userAccount.isEmpty()|| userPwd.isEmpty()){
                Toast.makeText(LoginActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
            }
            else{
                //此处用litepal去查询数据库，查询用户输入的账号和密码是否登录成功，其中账号是唯一标识
                List<UserInfo> userInfo = LitePal.where("userAccount = ?", userAccount).find(UserInfo.class);

                if(userInfo.isEmpty()) {
                    // 提示用户注册
                    Toast.makeText(LoginActivity.this, "账号不存在，请先注册！", Toast.LENGTH_SHORT).show();
                } else {
                    // 验证密码是否正确
                    if(!userPwd.equals(userInfo.get(0).getUserPwd())) {
                        Toast.makeText(LoginActivity.this, "请确认是否输入正确的密码?", Toast.LENGTH_SHORT).show();
                    } else {
                        // 登录成功，返回到主界面，主界面要保存登录的账号，便于查询读者信息，主界面使用onActivityResult来接收得到的账号
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userID", userAccount);
                        intent.putExtra("userNick", userInfo.get(0).getNickName());
                        intent.putExtra("userSign", userInfo.get(0).getUserSignature());
                        intent.putExtra("imagePath", userInfo.get(0).getImagePath());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        }
        else if(view.getId()==registerBtn.getId()){
            //此处跳转到注册页面
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            // 注册请求码是2
            startActivityForResult(intent, 2);
        }
    }
}

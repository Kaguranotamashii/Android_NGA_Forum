
package com.example.my_nga_fornums.tools;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

/**
 * BaseActivity作为所有活动的基类，用于管理活动的生命周期。
 * 它实现了活动的创建和销毁时的逻辑，包括日志记录和活动管理器的交互。
 * BaseActivity不需要注册。
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在活动创建时，记录日志以便于调试和跟踪活动生命周期。
        Log.d("BaseActivity", getClass().getSimpleName());
        // 将当前活动添加到活动管理器的活动列表中，以便于管理所有活动。
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在活动销毁时，从活动管理器的活动列表中移除当前活动。
        ActivityCollector.removeActivity(this);
    }
}
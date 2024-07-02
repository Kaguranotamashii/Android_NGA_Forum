
package com.example.my_nga_fornums.tools;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类用于管理所有正在运行的Activity实例。
 * 它提供方法来添加新的Activity，移除已存在的Activity，以及一次性结束所有Activity。
 * 这对于在应用程序需要退出或重新启动时非常有用，比如在登录状态改变或应用更新需要清理旧Activity时。
 */
public class ActivityCollector {

    /**
     * 存储所有已添加的Activity的静态列表。
     * 这个列表被用于管理Activity的生命周期，以便在需要时可以结束它们。
     */
    public static List<Activity> activities = new ArrayList<>();

    /**
     * 将一个Activity添加到活动列表中。
     * 这应该在Activity的onCreate方法中调用，以确保Activity被正确管理。
     *
     * @param activity 要添加到列表中的Activity实例。
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 从活动列表中移除一个Activity。
     * 这应该在Activity的onDestroy方法中调用，以确保Activity列表保持准确。
     *
     * @param activity 要从列表中移除的Activity实例。
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 结束所有当前存储的Activity。
     * 这是一个方便的方法，可以在需要一次性结束所有Activity时调用，比如在退出应用程序或重新登录时。
     * 方法会遍历列表中的每个Activity，并且只有在Activity尚未结束时才调用其finish方法。
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
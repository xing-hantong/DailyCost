package me.hantong.dailycost.utill;

import android.widget.Toast;

import androidx.annotation.StringRes;

import me.hantong.dailycost.App;

/**
 * Toast 工具类
 *
 * @author X
 */
public class ToastUtils {

    public static void show(@StringRes int resId) {
        Toast.makeText(App.getINSTANCE(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void show(String msg) {
        Toast.makeText(App.getINSTANCE(), msg, Toast.LENGTH_SHORT).show();
    }
}

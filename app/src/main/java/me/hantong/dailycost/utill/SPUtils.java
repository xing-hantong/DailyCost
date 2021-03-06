package me.hantong.dailycost.utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import me.hantong.dailycost.App;
import me.hantong.dailycost.database.AppDatabase;

/**
 * SharedPreferences 工具类
 *
 * @author X
 */
public class SPUtils {
    private static volatile SPUtils INSTANCE;
    private SharedPreferences sp;

    public static SPUtils getInstance(String spName) {
        if (TextUtils.isEmpty(spName)) {
            spName = "spUtils";
        }
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SPUtils(spName);
                }
            }
        }
        return INSTANCE;
    }

    private SPUtils(final String spName) {
        sp = App.getINSTANCE().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public boolean put(@NonNull final String key, final boolean value) {
        return sp.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

}
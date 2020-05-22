package me.hantong.dailycost.utill;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

/**
 * @author X
 * @date 2020/5/22
 */
public class CustomTabsUtil {
    public static void openWeb(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // github 黑色把
        builder.setToolbarColor(Color.parseColor("#ff24292d"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}

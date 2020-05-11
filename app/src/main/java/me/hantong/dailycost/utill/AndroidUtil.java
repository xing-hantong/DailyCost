package me.hantong.dailycost.utill;

import android.content.Context;
import android.content.Intent;

import me.hantong.dailycost.R;

/**
 * AndroidUtil
 *
 * @author X
 */
public class AndroidUtil {
    /**
     * 使用系统发送分享数据
     *
     * @param context 上下文
     * @param text    要分享的文本
     */
    public static void share(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.text_share_to)));
    }
}

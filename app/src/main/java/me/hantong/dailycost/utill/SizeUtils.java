package me.hantong.dailycost.utill;

import me.hantong.dailycost.App;

/**
 * 尺寸转换工具类
 *
 * @author X
 */
public class SizeUtils {
    public static int dp2px(final float dpValue) {
        final float scale = App.getINSTANCE().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

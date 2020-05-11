package me.hantong.dailycost.binding;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

import me.hantong.dailycost.R;
import me.hantong.dailycost.utill.BigDecimalUtil;
import me.hantong.dailycost.utill.SizeUtils;

/**
 * binding 属性适配器（自动被 DataBinding 引用）
 *
 * @author X
 */
public class BindAdapter {

    @BindingAdapter("android:visibility")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("custom_margin_bottom")
    public static void setMarginBottom(View view, int bottomMargin) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
        } else {
            marginParams = new ViewGroup.MarginLayoutParams(layoutParams);
        }
        marginParams.bottomMargin = SizeUtils.dp2px(bottomMargin);
    }

    @BindingAdapter("text_check_null")
    public static void setText(TextView textView, String text) {
        textView.setText(text);
        textView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("src_img_name")
    public static void setImg(ImageView imageView, String imgName) {
        Context context = imageView.getContext();
        if (TextUtils.isEmpty(imgName)) {
            imgName = "type_item_default";
        }
        int resId = context.getResources().getIdentifier(imgName, "mipmap", context.getPackageName());
        imageView.setImageResource(resId);
    }

    @BindingAdapter("text_money")
    public static void setMoneyText(TextView textView, BigDecimal bigDecimal) {
        textView.setText(BigDecimalUtil.fen2Yuan(bigDecimal));
    }

    @BindingAdapter("text_money_with_prefix")
    public static void setMoneyTextWithPrefix(TextView textView, BigDecimal bigDecimal) {
        String symbol = textView.getResources().getString(R.string.text_money_symbol);
        textView.setText(symbol + BigDecimalUtil.fen2Yuan(bigDecimal));
    }
}

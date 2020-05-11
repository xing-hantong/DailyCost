package me.hantong.dailycost.ui.addtype;

import androidx.databinding.ViewDataBinding;
import androidx.annotation.Nullable;

import java.util.List;

import me.hantong.dailycost.BR;
import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseDataBindingAdapter;

/**
 * 类型图片适配器
 *
 * @author X

 */

public class TypeImgAdapter extends BaseDataBindingAdapter<TypeImgBean> {

    private int mCurrentCheckPosition;

    public TypeImgAdapter(@Nullable List<TypeImgBean> data) {
        super(R.layout.item_type_img, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, TypeImgBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.typeImg, item);
        binding.executePendingBindings();
    }

    public void checkItem(int position) {
        // 选中某一个 item
        TypeImgBean temp;
        for (int i = 0; i < getData().size(); i++) {
            temp = getData().get(i);
            if (temp != null) {
                temp.isChecked = i == position;
            }
        }
        mCurrentCheckPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 获取当前选中的 item
     */
    public TypeImgBean getCurrentItem() {
        return getItem(mCurrentCheckPosition);
    }
}

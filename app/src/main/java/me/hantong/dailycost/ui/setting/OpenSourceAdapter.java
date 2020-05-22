package me.hantong.dailycost.ui.setting;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.hantong.dailycost.BR;
import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseDataBindingAdapter;

/**
 * @author X
 * @date 2020/5/22
 */
public class OpenSourceAdapter extends BaseDataBindingAdapter<OpenSourceBean> {
    public OpenSourceAdapter(@Nullable List<OpenSourceBean> data) {
        super(R.layout.item_open_source, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, OpenSourceBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.openSource, item);
        binding.executePendingBindings();
    }
}

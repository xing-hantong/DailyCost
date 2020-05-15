package me.hantong.dailycost.ui.home;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.hantong.dailycost.BR;
import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseDataBindingAdapter;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.utill.DateUtils;

/**
 * HomeAdapter
 *
 * @author X

 */

public class HomeAdapter extends BaseDataBindingAdapter<RecordWithType> {

    public HomeAdapter(@Nullable List<RecordWithType> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordWithType item) {
        ViewDataBinding binding = helper.getBinding();
        helper.addOnLongClickListener(R.id.ll_item_click);
        binding.setVariable(BR.recordWithType, item);
        boolean isDataShow = helper.getAdapterPosition() == 0 ||
                !DateUtils.isSameDay(item.time, getData().get(helper.getAdapterPosition() - 1).time);
        binding.setVariable(BR.isDataShow, isDataShow);
        binding.executePendingBindings();
    }
}

package me.hantong.dailycost.ui.typerecords;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import java.util.List;

import me.hantong.dailycost.BR;
import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseDataBindingAdapter;
import me.hantong.dailycost.database.entity.RecordWithType;

/**
 * RecordAdapter
 *
 * @author X

 */

public class RecordAdapter extends BaseDataBindingAdapter<RecordWithType> {

    public RecordAdapter(@Nullable List<RecordWithType> data) {
        super(R.layout.item_record_sort_money, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordWithType item) {
        ViewDataBinding binding = helper.getBinding();
        helper.addOnLongClickListener(R.id.ll_item_click);
        binding.setVariable(BR.recordWithType, item);
        binding.executePendingBindings();
    }
}

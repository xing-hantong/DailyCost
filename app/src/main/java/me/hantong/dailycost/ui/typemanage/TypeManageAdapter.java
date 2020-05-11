package me.hantong.dailycost.ui.typemanage;

import androidx.databinding.ViewDataBinding;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import me.hantong.dailycost.BR;
import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseDataBindingAdapter;
import me.hantong.dailycost.database.entity.RecordType;

/**
 * 类型管理适配器
 *
 * @author X

 */

public class TypeManageAdapter extends BaseDataBindingAdapter<RecordType> {

    public TypeManageAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type_manage, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();

        binding.setVariable(BR.recordType, item);
        boolean isLastItem = helper.getAdapterPosition() == mData.size() - 1;
        // 单位是 dp
        binding.setVariable(BR.bottomMargin, isLastItem ? 100 : 0);

        binding.executePendingBindings();
    }

    public void setNewData(List<RecordType> data, int type) {
        if (data != null && data.size() > 0) {
            List<RecordType> result = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).type == type) {
                    result.add(data.get(i));
                }
            }
            super.setNewData(result);
        } else {
            super.setNewData(null);
        }
    }
}

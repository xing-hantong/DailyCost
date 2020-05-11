package me.hantong.dailycost.ui.statistics.reports;

import androidx.databinding.ViewDataBinding;
import androidx.annotation.Nullable;

import java.util.List;

import me.hantong.dailycost.BR;
import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseDataBindingAdapter;
import me.hantong.dailycost.database.entity.TypeSumMoneyBean;

/**
 * ReportAdapter
 *
 * @author X

 */

public class ReportAdapter extends BaseDataBindingAdapter<TypeSumMoneyBean> {

    public ReportAdapter(@Nullable List<TypeSumMoneyBean> data) {
        super(R.layout.item_report, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, TypeSumMoneyBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.typeSumMoney, item);
        binding.executePendingBindings();
    }
}

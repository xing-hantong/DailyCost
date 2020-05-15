package me.hantong.dailycost.ui.add;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.floo.Floo;
import me.hantong.dailycost.App;
import me.hantong.dailycost.BR;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseDataBindingAdapter;
import me.hantong.dailycost.database.entity.RecordType;

/**
 * TypeAdapter
 *
 * @author X

 */

public class TypeAdapter extends BaseDataBindingAdapter<RecordType> {

    private int mCurrentCheckPosition;
    private int mCurrentCheckId = -1;
    private int mType;

    public TypeAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.recordType, item);
        binding.executePendingBindings();
    }

    /**
     * 筛选出支出和收入
     *
     * @param data 支出和收入总数据
     * @param type 类型 0：支出 1：收入
     * @see RecordType#TYPE_OUTLAY 支出
     * @see RecordType#TYPE_INCOME 收入
     */
    public void setNewData(@Nullable List<RecordType> data, int type) {
        mType = type;
        if (data != null && data.size() > 0) {
            List<RecordType> result = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).type == type) {
                    result.add(data.get(i));
                }
            }
            // 增加设置 item， type == -1 表示是设置 item
            RecordType settingItem = new RecordType(App.getINSTANCE().getString(R.string.text_setting), "type_item_setting", -1);
            result.add(settingItem);
            // 找出上次选中的 item
            int checkPosition = 0;
            if (result.get(0).type != -1) {
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).id == mCurrentCheckId) {
                        checkPosition = i;
                        break;
                    }
                }
                super.setNewData(result);
                clickItem(checkPosition);
            } else {
                super.setNewData(result);
            }
        } else {
            super.setNewData(null);
        }
    }

    /**
     * 选中某一个 item，或点击设置 item
     *
     * @param position 选中 item 的索引
     */
    public void clickItem(int position) {
        // 点击设置 item
        RecordType item = getItem(position);
        if (item != null && item.type == -1) {
            Floo.navigation(mContext, Router.Url.URL_TYPE_MANAGE)
                    .putExtra(Router.ExtraKey.KEY_TYPE, mType)
                    .start();
            return;
        }
        // 选中某一个 item
        RecordType temp;
        for (int i = 0; i < getData().size(); i++) {
            temp = getData().get(i);
            if (temp != null && temp.type != -1) {
                temp.isChecked = i == position;
            }
        }
        mCurrentCheckPosition = position;
        mCurrentCheckId = getCurrentItem().id;
        notifyDataSetChanged();
    }

    /**
     * 获取当前选中的 item
     */
    public RecordType getCurrentItem() {
        return getItem(mCurrentCheckPosition);
    }
}

package me.hantong.dailycost.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import java.util.List;

import me.hantong.dailycost.R;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.databinding.LayoutTypePageBinding;
import me.hantong.dailycost.ui.add.TypeAdapter;

/**
 * 翻页的 recyclerView + 指示器
 *
 * @author X
 */
public class TypePageView extends LinearLayout {

    private static final int ROW = 2;
    private static final int COLUMN = 4;

    private LayoutTypePageBinding mBinding;
    private TypeAdapter mAdapter;
    private PagerGridLayoutManager mLayoutManager;
    private int mCurrentTypeIndex = -1;

    public TypePageView(Context context) {
        this(context, null);
    }

    public TypePageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypePageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_type_page, this, true);

        // 1.水平分页布局管理器
        mLayoutManager = new PagerGridLayoutManager(
                ROW, COLUMN, PagerGridLayoutManager.HORIZONTAL);
        mBinding.recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TypeAdapter(null);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mAdapter.clickItem(position);
            mCurrentTypeIndex = position;
        });
        mBinding.recyclerView.setAdapter(mAdapter);

        mLayoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            int currentPageIndex;
            int pageSize;

            @Override
            public void onPageSizeChanged(int pageSize) {
                this.pageSize = pageSize;
                setIndicator();
            }

            @Override
            public void onPageSelect(int pageIndex) {
                currentPageIndex = pageIndex;
                setIndicator();
            }

            private void setIndicator() {
                if (pageSize > 1) {
                    mBinding.indicator.setVisibility(View.VISIBLE);
                    mBinding.indicator.setTotal(pageSize, currentPageIndex);
                } else {
                    mBinding.indicator.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void setNewData(@Nullable List<RecordType> data, int type) {
        mAdapter.setNewData(data, type);
    }

    /**
     * 该方法只改变一次
     */
    public void initCheckItem(RecordWithType record) {
        if (mCurrentTypeIndex == -1) {
            mCurrentTypeIndex = 0;
            int isTypeExist = 0;
            int size = mAdapter.getData().size();
            if (record != null && size > 0) {
                for (int i = 0; i < size; i++) {
                    if (record.mRecordTypes.get(0).id == mAdapter.getData().get(i).id) {
                        mCurrentTypeIndex = i;
                        isTypeExist++;
                        break;
                    }
                }
                if (isTypeExist != 0) {
                    // 选中对应的页
                    int pageIndex = mCurrentTypeIndex / (ROW * COLUMN);
                    post(() -> mLayoutManager.smoothScrollToPage(pageIndex));
                } else {
                    showTypeNotExistTip();
                }
            }
            mAdapter.clickItem(mCurrentTypeIndex);
        }
    }

    /**
     * 提示用户该记录的类型已经被删除
     */
    private void showTypeNotExistTip() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.text_tip_type_delete)
                .setPositiveButton(R.string.text_button_know, null)
                .create()
                .show();
    }

    public RecordType getCurrentItem() {
        return mAdapter.getCurrentItem();
    }

}

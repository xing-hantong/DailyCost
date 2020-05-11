package me.hantong.dailycost.ui.typesort;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.hantong.dailycost.Injection;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.databinding.ActivityTypeSortBinding;
import me.hantong.dailycost.datasource.BackupFailException;
import me.hantong.dailycost.utill.ToastUtils;
import me.hantong.dailycost.viewmodel.ViewModelFactory;

/**
 * 类型排序
 *
 * @author X

 */
public class TypeSortActivity extends BaseActivity {

    private static final String TAG = TypeSortActivity.class.getSimpleName();

    private static final int COLUMN = 4;

    private ActivityTypeSortBinding mBinding;
    private TypeSortViewModel mViewModel;
    private TypeSortAdapter mAdapter;
    private int mType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_type_sort;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TypeSortViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mType = getIntent().getIntExtra(Router.ExtraKey.KEY_TYPE, RecordType.TYPE_OUTLAY);

        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_drag_sort));
        mBinding.titleBar.tvRight.setText(R.string.text_done);
        mBinding.titleBar.tvRight.setOnClickListener(v -> sortRecordTypes());

        mBinding.rvType.setLayoutManager(new GridLayoutManager(this, COLUMN));
        mAdapter = new TypeSortAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.rvType);

        // open drag
        mAdapter.enableDragItem(itemTouchHelper);
    }

    private void sortRecordTypes() {
        mBinding.titleBar.tvRight.setEnabled(false);
        mDisposable.add(mViewModel.sortRecordTypes(mAdapter.getData()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish, throwable -> {
                    if (throwable instanceof BackupFailException) {
                        ToastUtils.show(throwable.getMessage());
                        Log.e(TAG, "备份失败（类型排序失败的时候）", throwable);
                        finish();
                    } else {
                        mBinding.titleBar.tvRight.setEnabled(true);
                        ToastUtils.show(R.string.toast_sort_fail);
                        Log.e(TAG, "类型排序失败", throwable);
                    }
                }));
    }

    private void initData() {
        mDisposable.add(mViewModel.getRecordTypes(mType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> mAdapter.setNewData(recordTypes),
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_types_fail);
                            Log.e(TAG, "获取类型数据失败", throwable);
                        }));
    }
}

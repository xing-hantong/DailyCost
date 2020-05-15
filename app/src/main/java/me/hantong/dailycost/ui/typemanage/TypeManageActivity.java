package me.hantong.dailycost.ui.typemanage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.floo.Floo;
import me.hantong.dailycost.Injection;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.databinding.ActivityTypeManageBinding;
import me.hantong.dailycost.datasource.BackupFailException;
import me.hantong.dailycost.utill.ToastUtils;
import me.hantong.dailycost.viewmodel.ViewModelFactory;

/**
 * 类型管理
 *
 * @author X

 */
public class TypeManageActivity extends BaseActivity {

    private static final String TAG = TypeManageActivity.class.getSimpleName();

    private ActivityTypeManageBinding mBinding;
    private TypeManageViewModel mViewModel;
    private TypeManageAdapter mAdapter;
    private List<RecordType> mRecordTypes;

    private int mCurrentType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_type_manage;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TypeManageViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mCurrentType = getIntent().getIntExtra(Router.ExtraKey.KEY_TYPE, RecordType.TYPE_OUTLAY);
        mBinding.titleBar.tvRight.setText(R.string.text_button_sort);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_type_manage));
        mBinding.titleBar.tvRight.setOnClickListener(v ->
                Floo.navigation(this, Router.Url.URL_TYPE_SORT)
                        .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                        .start());

        mBinding.rvType.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TypeManageAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (adapter.getData().size() > 1) {
                showDeleteDialog(mAdapter.getData().get(position));
            } else {
                ToastUtils.show(R.string.toast_least_one_type);
            }
            return true;
        });

        mAdapter.setOnItemClickListener((adapter, view, position) ->
                Floo.navigation(this, Router.Url.URL_ADD_TYPE)
                        .putExtra(Router.ExtraKey.KEY_TYPE_BEAN, mAdapter.getItem(position))
                        .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                        .start());

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            mCurrentType = checkedId == R.id.rb_outlay ? RecordType.TYPE_OUTLAY : RecordType.TYPE_INCOME;
            mAdapter.setNewData(mRecordTypes, mCurrentType);
            int visibility = mAdapter.getData().size() > 1 ? View.VISIBLE : View.INVISIBLE;
            mBinding.titleBar.tvRight.setVisibility(visibility);
        });

    }

    private void showDeleteDialog(RecordType recordType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_dialog_delete) + recordType.name)
                .setMessage(R.string.text_delete_type_note)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_button_affirm_delete, (dialog, which) -> deleteType(recordType))
                .create()
                .show();
    }

    public void addType(View view) {
        Floo.navigation(this, Router.Url.URL_ADD_TYPE)
                .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                .start();
    }

    private void deleteType(RecordType recordType) {
        mDisposable.add(mViewModel.deleteRecordType(recordType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（类型删除失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_delete_fail);
                                Log.e(TAG, "类型删除失败", throwable);
                            }
                        }
                ));
    }

    private void initData() {
        mDisposable.add(mViewModel.getAllRecordTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                            mRecordTypes = recordTypes;
                            int id = mCurrentType == RecordType.TYPE_OUTLAY ? R.id.rb_outlay : R.id.rb_income;
                            mBinding.typeChoice.rgType.clearCheck();
                            mBinding.typeChoice.rgType.check(id);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_types_fail);
                            Log.e(TAG, "获取类型数据失败", throwable);
                        }));
    }
}

package me.hantong.dailycost.ui.typerecords;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.hantong.dailycost.Injection;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseFragment;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.databinding.FragmentTypeRecordsBinding;
import me.hantong.dailycost.datasource.BackupFailException;
import me.hantong.dailycost.ui.home.HomeAdapter;
import me.hantong.dailycost.utill.ToastUtils;
import me.hantong.dailycost.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

/**
 * 某一类型记账记录
 * 按金额或时间排序
 *
 * @author X
 */
public class TypeRecordsFragment extends BaseFragment {
    private static final String TAG = TypeRecordsFragment.class.getSimpleName();
    public static final int SORT_TIME = 0;
    public static final int SORT_MONEY = 1;

    private FragmentTypeRecordsBinding mBinding;
    private TypeRecordsViewModel mViewModel;
    private HomeAdapter mSortTimeAdapter;
    private RecordAdapter mSortMoneyAdapter;

    private int mSortType;
    private int mRecordType;
    private int mRecordTypeId;
    private int mYear;
    private int mMonth;

    public static TypeRecordsFragment newInstance(int sortType, int recordType, int recordTypeId, int year, int month) {
        TypeRecordsFragment fragment = new TypeRecordsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Router.ExtraKey.KEY_SORT_TYPE, sortType);
        bundle.putInt(Router.ExtraKey.KEY_RECORD_TYPE, recordType);
        bundle.putInt(Router.ExtraKey.KEY_RECORD_TYPE_ID, recordTypeId);
        bundle.putInt(Router.ExtraKey.KEY_YEAR, year);
        bundle.putInt(Router.ExtraKey.KEY_MONTH, month);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_type_records;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TypeRecordsViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mSortType = bundle.getInt(Router.ExtraKey.KEY_SORT_TYPE);
            mRecordType = bundle.getInt(Router.ExtraKey.KEY_RECORD_TYPE);
            mRecordTypeId = bundle.getInt(Router.ExtraKey.KEY_RECORD_TYPE_ID);
            mYear = bundle.getInt(Router.ExtraKey.KEY_YEAR);
            mMonth = bundle.getInt(Router.ExtraKey.KEY_MONTH);
        }

        initView();

        getData();
    }

    private void initView() {
        mBinding.rvRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mSortType == SORT_TIME) {
            mSortTimeAdapter = new HomeAdapter(null);
            mBinding.rvRecords.setAdapter(mSortTimeAdapter);
            mSortTimeAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
                showOperateDialog(mSortTimeAdapter.getData().get(position));
                return false;
            });
        } else {
            mSortMoneyAdapter = new RecordAdapter(null);
            mBinding.rvRecords.setAdapter(mSortMoneyAdapter);
            mSortMoneyAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
                showOperateDialog(mSortMoneyAdapter.getData().get(position));
                return false;
            });
        }
    }

    private void showOperateDialog(RecordWithType record) {
        if (getContext() == null) {
            return;
        }
        new AlertDialog.Builder(getContext())
                .setItems(new String[]{getString(R.string.text_modify), getString(R.string.text_delete)}, (dialog, which) -> {
                    if (which == 0) {
                        modifyRecord(record);
                    } else {
                        deleteRecord(record);
                    }
                })
                .create()
                .show();
    }

    private void modifyRecord(RecordWithType record) {
        if (getContext() == null) {
            return;
        }
        Floo.navigation(getContext(), Router.Url.URL_ADD_RECORD)
                .putExtra(Router.ExtraKey.KEY_RECORD_BEAN, record)
                .start();
    }

    private void deleteRecord(RecordWithType record) {
        mDisposable.add(mViewModel.deleteRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（删除记账记录失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_record_delete_fail);
                                Log.e(TAG, "删除记账记录失败", throwable);
                            }
                        }));
    }

    @Override
    protected void lazyInitData() {

    }

    private void getData() {
        mDisposable.add(mViewModel.getRecordWithTypes(mSortType, mRecordType, mRecordTypeId, mYear, mMonth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            if (mSortType == 0) {
                                mSortTimeAdapter.setNewData(recordWithTypes);
                                if (recordWithTypes == null || recordWithTypes.size() < 1) {
                                    mSortTimeAdapter.setEmptyView(inflate(R.layout.layout_record_empty));
                                }
                            } else {
                                mSortMoneyAdapter.setNewData(recordWithTypes);
                                if (recordWithTypes == null || recordWithTypes.size() < 1) {
                                    mSortMoneyAdapter.setEmptyView(inflate(R.layout.layout_record_empty));
                                }
                            }
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_records_fail);
                            Log.e(TAG, "获取记录列表失败", throwable);
                        }));
    }
}

package me.hantong.dailycost.ui.add;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;


import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.hantong.dailycost.Injection;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.database.entity.Record;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.databinding.ActivityAddRecordBinding;
import me.hantong.dailycost.datasource.BackupFailException;
import me.hantong.dailycost.utill.BigDecimalUtil;
import me.hantong.dailycost.utill.DateUtils;
import me.hantong.dailycost.utill.ToastUtils;
import me.hantong.dailycost.viewmodel.ViewModelFactory;

/**
 * HomeActivity
 *
 * @author X

 */
public class AddRecordActivity extends BaseActivity {

    private static final String TAG = AddRecordActivity.class.getSimpleName();
    private static final String TAG_PICKER_DIALOG = "Datepickerdialog";

    private ActivityAddRecordBinding mBinding;

    private AddRecordViewModel mViewModel;

    private Date mCurrentChooseDate = DateUtils.getTodayDate();
    private Calendar mCurrentChooseCalendar = Calendar.getInstance();
    private int mCurrentType;

    private RecordWithType mRecord;

    int  mYear = mCurrentChooseCalendar.get(Calendar.YEAR);
    int  mMonth = mCurrentChooseCalendar.get(Calendar.MONTH);
    int  mDay = mCurrentChooseCalendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecordViewModel.class);

        initView();
        initData();
    }

    private void initData() {
        getAllRecordTypes();
    }

    private void initView() {
        mRecord = (RecordWithType) getIntent().getSerializableExtra(Router.ExtraKey.KEY_RECORD_BEAN);

        mBinding.titleBar.ibtClose.setBackgroundResource(R.drawable.ic_close);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());

        mBinding.edtRemark.setOnEditorActionListener((v, actionId, event) -> {
            mBinding.keyboard.setEditTextFocus();
            return false;
        });

        if (mRecord == null) {
            mCurrentType = RecordType.TYPE_OUTLAY;
            mBinding.titleBar.setTitle(getString(R.string.text_add_record));
        } else {
            mCurrentType = mRecord.mRecordTypes.get(0).type;
            mBinding.titleBar.setTitle(getString(R.string.text_modify_record));
            mBinding.edtRemark.setText(mRecord.remark);
            mBinding.keyboard.setText(BigDecimalUtil.fen2Yuan(mRecord.money));
            mCurrentChooseDate = mRecord.time;
            mCurrentChooseCalendar.setTime(mCurrentChooseDate);
            mBinding.qmTvDate.setText(DateUtils.getWordTime(mCurrentChooseDate));
        }

        mBinding.keyboard.setAffirmClickListener(text -> {
            if (mRecord == null) {
                insertRecord(text);
            } else {
                modifyRecord(text);
            }
        });

        mBinding.qmTvDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mCurrentChooseDate = DateUtils.getDate(year, monthOfYear + 1, dayOfMonth);
                            mCurrentChooseCalendar.setTime(mCurrentChooseDate);
                            mBinding.qmTvDate.setText(DateUtils.getWordTime(mCurrentChooseDate));
                        }
                    },
                    mYear, mMonth,mDay);
            datePickerDialog.show();
        });

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rb_outlay) {
                mCurrentType = RecordType.TYPE_OUTLAY;
                mBinding.typePageOutlay.setVisibility(View.VISIBLE);
                mBinding.typePageIncome.setVisibility(View.GONE);
            } else {
                mCurrentType = RecordType.TYPE_INCOME;
                mBinding.typePageOutlay.setVisibility(View.GONE);
                mBinding.typePageIncome.setVisibility(View.VISIBLE);
            }

        });
    }

    private void insertRecord(String text) {
        // 防止重复提交
        mBinding.keyboard.setAffirmEnable(false);
        Record record = new Record();
        record.money = BigDecimalUtil.yuan2FenBD(text);
        record.remark = mBinding.edtRemark.getText().toString().trim();
        record.time = mCurrentChooseDate;
        record.createTime = new Date();
        record.recordTypeId = mCurrentType == RecordType.TYPE_OUTLAY ?
                mBinding.typePageOutlay.getCurrentItem().id : mBinding.typePageIncome.getCurrentItem().id;

        mDisposable.add(mViewModel.insertRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（新增记录失败的时候）", throwable);
                                finish();
                            } else {
                                Log.e(TAG, "新增记录失败", throwable);
                                mBinding.keyboard.setAffirmEnable(true);
                                ToastUtils.show(R.string.toast_add_record_fail);
                            }
                        }
                ));
    }

    private void modifyRecord(String text) {
        // 防止重复提交
        mBinding.keyboard.setAffirmEnable(false);
        mRecord.money = BigDecimalUtil.yuan2FenBD(text);
        mRecord.remark = mBinding.edtRemark.getText().toString().trim();
        mRecord.time = mCurrentChooseDate;
        mRecord.recordTypeId = mCurrentType == RecordType.TYPE_OUTLAY ?
                mBinding.typePageOutlay.getCurrentItem().id : mBinding.typePageIncome.getCurrentItem().id;

        mDisposable.add(mViewModel.updateRecord(mRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（记录修改失败的时候）", throwable);
                                finish();
                            } else {
                                Log.e(TAG, "记录修改失败", throwable);
                                mBinding.keyboard.setAffirmEnable(true);
                                ToastUtils.show(R.string.toast_modify_record_fail);
                            }
                        }
                ));
    }

    private void getAllRecordTypes() {
        mDisposable.add(mViewModel.getAllRecordTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                    mBinding.typePageOutlay.setNewData(recordTypes, RecordType.TYPE_OUTLAY);
                    mBinding.typePageIncome.setNewData(recordTypes, RecordType.TYPE_INCOME);

                    if (mCurrentType == RecordType.TYPE_OUTLAY) {
                        mBinding.typeChoice.rgType.check(R.id.rb_outlay);
                        mBinding.typePageOutlay.initCheckItem(mRecord);
                    } else {
                        mBinding.typeChoice.rgType.check(R.id.rb_income);
                        mBinding.typePageIncome.initCheckItem(mRecord);
                    }

                }, throwable -> {
                    ToastUtils.show(R.string.toast_get_types_fail);
                    Log.e(TAG, "获取类型数据失败", throwable);
                }));
    }
}

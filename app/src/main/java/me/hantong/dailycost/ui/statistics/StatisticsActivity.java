package me.hantong.dailycost.ui.statistics;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.databinding.ActivityStatisticsBinding;
import me.hantong.dailycost.ui.statistics.bill.BillFragment;
import me.hantong.dailycost.ui.statistics.reports.ReportsFragment;
import me.hantong.dailycost.utill.DateUtils;
import me.hantong.dailycost.view.MonthYearPickerDialog;

/**
 * 统计
 *
 * @author X
 */
public class StatisticsActivity extends BaseActivity {
    private ActivityStatisticsBinding mBinding;
    private BillFragment mBillFragment;
    private ReportsFragment mReportsFragment;

    public BillFragment getmBillFragment() { return mBillFragment;}

    public ReportsFragment getmReportsFragment() { return mReportsFragment;}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistics;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {
        String title = DateUtils.getCurrentYearMonth();
        mBinding.titleBar.setTitle(title);
        mBinding.titleBar.ivTitle.setVisibility(View.VISIBLE);
        mBinding.titleBar.llTitle.setOnClickListener(v -> chooseMonth());
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_order);
        mBinding.typeChoice.rbIncome.setText(R.string.text_reports);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mBillFragment = new BillFragment();
        mReportsFragment = new ReportsFragment();
        infoPagerAdapter.addFragment(mBillFragment);
        infoPagerAdapter.addFragment(mReportsFragment);
        mBinding.viewPager.setAdapter(infoPagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(2);

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mBinding.viewPager.setCurrentItem(0, false);
            } else {
                mBinding.viewPager.setCurrentItem(1, false);
            }
        });
        mBinding.typeChoice.rgType.check(R.id.rb_outlay);
    }

    private void chooseMonth() {
        MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
        pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String title = DateUtils.getYearMonthFormatString(year, month);
                mBinding.titleBar.setTitle(title);
                mBillFragment.setYearMonth(year, month);
                mReportsFragment.setYearMonth(year, month);
            }
        });
        pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
    }

}

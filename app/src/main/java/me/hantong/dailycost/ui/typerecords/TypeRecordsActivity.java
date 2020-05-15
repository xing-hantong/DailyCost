package me.hantong.dailycost.ui.typerecords;

import android.os.Bundle;

import androidx.annotation.Nullable;

import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.databinding.ActivityStatisticsBinding;
import me.hantong.dailycost.ui.statistics.ViewPagerAdapter;

/**
 * 某一类型的记账记录
 *
 * @author X
 */
public class TypeRecordsActivity extends BaseActivity {

    private ActivityStatisticsBinding mBinding;

    private int mRecordType;
    private int mRecordTypeId;
    private int mYear;
    private int mMonth;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {
        if (getIntent() != null) {
            mBinding.titleBar.setTitle(getIntent().getStringExtra(Router.ExtraKey.KEY_TYPE_NAME));
            mRecordType = getIntent().getIntExtra(Router.ExtraKey.KEY_RECORD_TYPE, 0);
            mRecordTypeId = getIntent().getIntExtra(Router.ExtraKey.KEY_RECORD_TYPE_ID, 0);
            mYear = getIntent().getIntExtra(Router.ExtraKey.KEY_YEAR, 0);
            mMonth = getIntent().getIntExtra(Router.ExtraKey.KEY_MONTH, 0);
        }

        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_sort_time);
        mBinding.typeChoice.rbIncome.setText(R.string.text_sort_money);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        TypeRecordsFragment timeSortFragment = TypeRecordsFragment.newInstance(TypeRecordsFragment.SORT_TIME, mRecordType, mRecordTypeId, mYear, mMonth);
        TypeRecordsFragment moneySortFragment = TypeRecordsFragment.newInstance(TypeRecordsFragment.SORT_MONEY, mRecordType, mRecordTypeId, mYear, mMonth);
        infoPagerAdapter.addFragment(timeSortFragment);
        infoPagerAdapter.addFragment(moneySortFragment);
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
}

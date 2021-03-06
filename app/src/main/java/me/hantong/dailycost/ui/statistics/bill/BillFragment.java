package me.hantong.dailycost.ui.statistics.bill;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.floo.Floo;
import me.hantong.dailycost.Injection;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseFragment;
import me.hantong.dailycost.database.entity.DaySumMoneyBean;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.database.entity.SumMoneyBean;
import me.hantong.dailycost.databinding.FragmentBillBinding;
import me.hantong.dailycost.datasource.BackupFailException;
import me.hantong.dailycost.ui.home.HomeAdapter;
import me.hantong.dailycost.ui.statistics.StatisticsActivity;
import me.hantong.dailycost.utill.BigDecimalUtil;
import me.hantong.dailycost.utill.DateUtils;
import me.hantong.dailycost.utill.ToastUtils;
import me.hantong.dailycost.view.BarChartMarkerView;
import me.hantong.dailycost.viewmodel.ViewModelFactory;

/**
 * 统计-账单
 *
 * @author X
 */
public class BillFragment extends BaseFragment {

    private static final String TAG = BillFragment.class.getSimpleName();
    private FragmentBillBinding mBinding;
    private BillViewModel mViewModel;
    private HomeAdapter mAdapter;

    public int mYear;
    public int mMonth;
    public int mType;


    public FragmentBillBinding getmBinding() { return mBinding;}

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bill;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(BillViewModel.class);

        mYear = DateUtils.getCurrentYear();
        mMonth = DateUtils.getCurrentMonth();
        mType = RecordType.TYPE_OUTLAY;

        initView();
    }

    private void initView() {
        mBinding.rvRecordBill.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeAdapter(null);
        mBinding.rvRecordBill.setAdapter(mAdapter);
        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            showOperateDialog(mAdapter.getData().get(position));
            return false;
        });

        initBarChart();

        StatisticsActivity parentActivity = ((StatisticsActivity) Objects.requireNonNull(getActivity()));
        mBinding.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mType = RecordType.TYPE_OUTLAY;
                if (mBinding.rbOutlay.isPressed()) {
                    parentActivity.getmReportsFragment().getmBinding().rgType.check(R.id.rb_outlay);
                }
            } else {
                mType = RecordType.TYPE_INCOME;
                if (mBinding.rbIncome.isPressed()) {
                    parentActivity.getmReportsFragment().getmBinding().rgType.check(R.id.rb_income);
                }
            }
            getOrderData();
            getDaySumData();
            getMonthSumMoney();
        });
    }



    private void initBarChart() {
        mBinding.barChart.setNoDataText("");
        mBinding.barChart.setScaleEnabled(false);
        mBinding.barChart.getDescription().setEnabled(false);
        mBinding.barChart.getLegend().setEnabled(false);

        mBinding.barChart.getAxisLeft().setAxisMinimum(0);
        mBinding.barChart.getAxisLeft().setEnabled(false);
        mBinding.barChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mBinding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(getResources().getColor(R.color.colorTextGray));
        xAxis.setLabelCount(5);

        BarChartMarkerView mv = new BarChartMarkerView(getContext());
        mv.setChartView(mBinding.barChart);
        mBinding.barChart.setMarker(mv);
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

    private void setChartData(List<DaySumMoneyBean> daySumMoneyBeans) {
        if (daySumMoneyBeans == null || daySumMoneyBeans.size() < 1) {
            mBinding.barChart.setVisibility(View.INVISIBLE);
            return;
        } else {
            mBinding.barChart.setVisibility(View.VISIBLE);
        }

        int count = DateUtils.getDayCount(mYear, mMonth);
        List<BarEntry> barEntries = BarEntryConverter.getBarEntryList(count, daySumMoneyBeans);

        BarDataSet set1;
        if (mBinding.barChart.getData() != null && mBinding.barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBinding.barChart.getData().getDataSetByIndex(0);
            set1.setValues(barEntries);
            mBinding.barChart.getData().notifyDataChanged();
            mBinding.barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(barEntries, "");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColor(getResources().getColor(R.color.colorAccent));
            set1.setValueTextColor(getResources().getColor(R.color.colorTextWhite));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.5f);
            data.setHighlightEnabled(true);
            mBinding.barChart.setData(data);
        }
        mBinding.barChart.invalidate();
        mBinding.barChart.animateY(1000);
    }

    /**
     * 设置月份
     */
    public void setYearMonth(int year, int month) {
        if (year == mYear && month == mMonth) {
            return;
        }
        mYear = year;
        mMonth = month;
        // 更新数据
        getOrderData();
        getDaySumData();
        getMonthSumMoney();
    }

    @Override
    protected void lazyInitData() {
        mBinding.rgType.check(R.id.rb_outlay);
    }

    private void getOrderData() {
        mDisposable.add(mViewModel.getRecordWithTypes(mYear, mMonth, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            mAdapter.setNewData(recordWithTypes);
                            if (recordWithTypes == null || recordWithTypes.size() < 1) {
                                mAdapter.setEmptyView(inflate(R.layout.layout_statistics_empty));
                            }
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_records_fail);
                            Log.e(TAG, "获取记录列表失败", throwable);
                        }));
    }

    private void getDaySumData() {
        mDisposable.add(mViewModel.getDaySumMoney(mYear, mMonth, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setChartData,
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_statistics_fail);
                            Log.e(TAG, "获取统计数据失败", throwable);
                        }));
    }

    private void getMonthSumMoney() {
        mDisposable.add(mViewModel.getMonthSumMoney(mYear, mMonth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sumMoneyBean -> {
                            String outlay = getString(R.string.text_month_outlay_symbol) + "0";
                            String income = getString(R.string.text_month_income_symbol) + "0";
                            if (sumMoneyBean != null && sumMoneyBean.size() > 0) {
                                for (SumMoneyBean bean : sumMoneyBean) {
                                    if (bean.type == RecordType.TYPE_OUTLAY) {
                                        outlay = getString(R.string.text_month_outlay_symbol) + BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    } else if (bean.type == RecordType.TYPE_INCOME) {
                                        income = getString(R.string.text_month_income_symbol) + BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    }
                                }
                            }
                            mBinding.rbOutlay.setText(outlay);
                            mBinding.rbIncome.setText(income);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_month_summary_fail);
                            Log.e(TAG, "获取该月汇总数据失败", throwable);
                        }));
    }
}

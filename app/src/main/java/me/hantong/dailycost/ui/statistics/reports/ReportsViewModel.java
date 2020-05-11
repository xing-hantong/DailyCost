package me.hantong.dailycost.ui.statistics.reports;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.SumMoneyBean;
import me.hantong.dailycost.database.entity.TypeSumMoneyBean;
import me.hantong.dailycost.datasource.AppDataSource;
import me.hantong.dailycost.utill.DateUtils;

/**
 * 统计-报表
 *
 * @author X
 */
public class ReportsViewModel extends BaseViewModel {
    public ReportsViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<SumMoneyBean>> getMonthSumMoney(int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getMonthSumMoney(dateFrom, dateTo);
    }

    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getTypeSumMoney(dateFrom, dateTo, type);
    }

}

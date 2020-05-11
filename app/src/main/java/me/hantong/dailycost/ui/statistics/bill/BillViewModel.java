package me.hantong.dailycost.ui.statistics.bill;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.DaySumMoneyBean;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.database.entity.SumMoneyBean;
import me.hantong.dailycost.datasource.AppDataSource;
import me.hantong.dailycost.utill.DateUtils;

/**
 * 统计-账单
 *
 * @author X
 */
public class BillViewModel extends BaseViewModel {
    public BillViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordWithType>> getRecordWithTypes(int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getRecordWithTypes(dateFrom, dateTo, type);
    }

    public Flowable<List<DaySumMoneyBean>> getDaySumMoney(int year, int month, int type) {
        return mDataSource.getDaySumMoney(year, month, type);
    }

    public Flowable<List<SumMoneyBean>> getMonthSumMoney(int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getMonthSumMoney(dateFrom, dateTo);
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }

}

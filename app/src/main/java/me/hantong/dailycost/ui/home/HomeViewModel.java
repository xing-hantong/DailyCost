package me.hantong.dailycost.ui.home;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.database.entity.SumMoneyBean;
import me.hantong.dailycost.datasource.AppDataSource;

/**
 * 主页 ViewModel
 *
 * @author X
 */
public class HomeViewModel extends BaseViewModel {
    public HomeViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Completable initRecordTypes() {
        return mDataSource.initRecordTypes();
    }

    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        return mDataSource.getCurrentMonthRecordWithTypes();
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }

    public Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney() {
        return mDataSource.getCurrentMonthSumMoney();
    }
}

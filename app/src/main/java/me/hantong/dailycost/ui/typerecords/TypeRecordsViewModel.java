package me.hantong.dailycost.ui.typerecords;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.RecordWithType;
import me.hantong.dailycost.datasource.AppDataSource;
import me.hantong.dailycost.utill.DateUtils;

/**
 * 某一类型的记账记录
 *
 * @author X
 */
public class TypeRecordsViewModel extends BaseViewModel {
    public TypeRecordsViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordWithType>> getRecordWithTypes(int sortType, int type, int typeId, int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        if (sortType == TypeRecordsFragment.SORT_TIME) {
            return mDataSource.getRecordWithTypes(dateFrom, dateTo, type, typeId);
        } else {
            return mDataSource.getRecordWithTypesSortMoney(dateFrom, dateTo, type, typeId);
        }
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }

}

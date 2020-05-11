package me.hantong.dailycost.ui.typesort;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.datasource.AppDataSource;

/**
 * 类型排序 ViewModel
 *
 * @author X
 */
public class TypeSortViewModel extends BaseViewModel {
    public TypeSortViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getRecordTypes(int type) {
        return mDataSource.getRecordTypes(type);
    }

    public Completable sortRecordTypes(List<RecordType> recordTypes) {
        return mDataSource.sortRecordTypes(recordTypes);
    }
}

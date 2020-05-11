package me.hantong.dailycost.ui.add;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.Record;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author X
 */
public class AddRecordViewModel extends BaseViewModel {
    public AddRecordViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAllRecordType();
    }

    public Completable insertRecord(Record record) {
        return mDataSource.insertRecord(record);
    }

    public Completable updateRecord(Record record) {
        return mDataSource.updateRecord(record);
    }
}

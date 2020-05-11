package me.hantong.dailycost.ui.typemanage;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author X
 */
public class TypeManageViewModel extends BaseViewModel {
    public TypeManageViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAllRecordType();
    }

    public Completable deleteRecordType(RecordType recordType) {
        return mDataSource.deleteRecordType(recordType);
    }
}

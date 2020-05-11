package me.hantong.dailycost.ui.addtype;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.base.BaseViewModel;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.datasource.AppDataSource;

/**
 * 添加记账类型 ViewModel
 *
 * @author X
 */
public class AddTypeViewModel extends BaseViewModel {
    public AddTypeViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<TypeImgBean>> getAllTypeImgBeans(int type) {
        return mDataSource.getAllTypeImgBeans(type);
    }

    /**
     * 保存记账类型，包括新增和更新
     *
     * @param recordType 修改时传
     * @param type       类型
     * @param imgName    图片
     * @param name       类型名称
     */
    public Completable saveRecordType(RecordType recordType, int type, String imgName, String name) {
        if (recordType == null) {
            // 添加
            return mDataSource.addRecordType(type, imgName, name);
        } else {
            // 修改
            RecordType updateType = new RecordType(recordType.id, name, imgName, recordType.type, recordType.ranking);
            updateType.state = recordType.state;
            return mDataSource.updateRecordType(recordType, updateType);
        }
    }
}

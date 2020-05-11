package me.hantong.dailycost.ui.setting;

import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.hantong.dailycost.utill.BackupUtil;

/**
 * 设置 ViewModel
 *
 * @author X
 */
public class SettingViewModel extends ViewModel {

    public Flowable<List<BackupBean>> getBackupFiles() {
        return Flowable.create(e -> {
            e.onNext(BackupUtil.getBackupFiles());
            e.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    public Completable backupDB() {
        return Completable.create(e -> {
            boolean result = BackupUtil.userBackup();
            if (result) {
                e.onComplete();
            } else {
                e.onError(new Exception());
            }
        });
    }

    public Completable restoreDB(String restoreFile) {
        return Completable.create(e -> {
            boolean result = BackupUtil.restoreDB(restoreFile);
            if (result) {
                e.onComplete();
            } else {
                e.onError(new Exception());
            }
        });
    }
}

package me.hantong.dailycost.datasource;

import me.hantong.dailycost.App;
import me.hantong.dailycost.R;

/**
 * 备份失败异常
 *
 * @author X
 */
public class BackupFailException extends Exception {
    public BackupFailException() {
        super(App.getINSTANCE().getString(R.string.text_tip_backup_fail));
    }
}

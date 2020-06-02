package me.hantong.dailycost.utill;

import com.snatik.storage.Storage;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.hantong.dailycost.App;
import me.hantong.dailycost.R;
import me.hantong.dailycost.database.AppDatabase;
import me.hantong.dailycost.ui.setting.BackupBean;

/**
 * 备份相关工具类
 *
 * @author X
 */
public class BackupUtil {
    public static final String BACKUP_DIR = "backup_dailycost";
    public static final String AUTO_BACKUP_PREFIX = "DailyCostBackupAuto";
    public static final String USER_BACKUP_PREFIX = "DailyCostBackupUser";
    public static final String SUFFIX = ".db";
    public static final String BACKUP_SUFFIX = App.getINSTANCE().getString(R.string.text_before_reverting);

    private static boolean backupDB(String fileName) {
        Storage storage = new Storage(App.getINSTANCE());
        boolean isWritable = Storage.isExternalWritable();
        if (!isWritable) {
            return false;
        }
        String path = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR;
        if (!storage.isDirectoryExists(path)) {
            storage.createDirectory(path);
        }
        String filePath = path + File.separator + fileName;
        if (!storage.isFileExist(filePath)) {
            // 创建空文件，在模拟器上测试，如果没有这个文件，复制的时候会报 FileNotFound
            storage.createFile(filePath, "");
        }
        return storage.copy(App.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath(), path + File.separator + fileName);
    }

    public static boolean autoBackup() {
        String fileName = AUTO_BACKUP_PREFIX + SUFFIX;
        return backupDB(fileName);
    }

    public static boolean autoBackupForNecessary() {
        String fileName = AUTO_BACKUP_PREFIX + SUFFIX;
        Storage storage = new Storage(App.getINSTANCE());
        boolean isWritable = Storage.isExternalWritable();
        if (!isWritable) {
            return false;
        }
        String path = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR;
        if (!storage.isDirectoryExists(path)) {
            storage.createDirectory(path);
        }
        String filePath = path + File.separator + fileName;
        if (!storage.isFileExist(filePath)) {
            // 创建空文件，在模拟器上测试，如果没有这个文件，复制的时候会报 FileNotFound
            storage.createFile(filePath, "");
            return storage.copy(App.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath(), path + File.separator + fileName);
        }
        return true;
    }

    public static boolean userBackup() {
        String fileName = USER_BACKUP_PREFIX + SUFFIX;
        return backupDB(fileName);
    }

    public static boolean restoreDB(String restoreFile) {
        Storage storage = new Storage(App.getINSTANCE());
        if (storage.isFileExist(restoreFile)) {
            // 恢复之前，备份一下最新数据
            String fileName = "DailyCostBackup" + DateUtils.getCurrentDateString() + BACKUP_SUFFIX + SUFFIX;
            boolean isBackupSuccess = backupDB(fileName);
            boolean isRestoreSuccess = storage.copy(restoreFile, App.getINSTANCE().getDatabasePath(AppDatabase.DB_NAME).getPath());
            return isBackupSuccess && isRestoreSuccess;
        }
        return false;
    }

    public static List<BackupBean> getBackupFiles() {
        Storage storage = new Storage(App.getINSTANCE());
        String dir = storage.getExternalStorageDirectory() + File.separator + BACKUP_DIR;
        List<BackupBean> backupBeans = new ArrayList<>();
        BackupBean bean;
        List<File> files = storage.getFiles(dir, "[\\S]*\\.db");
        if (files == null) {
            return backupBeans;
        }
        File fileTemp;
        for (int i = 0; i < files.size(); i++) {
            fileTemp = files.get(i);
            bean = new BackupBean();
            bean.file = fileTemp;
            bean.name = fileTemp.getName();
            bean.size = storage.getReadableSize(fileTemp);
            bean.time = new Date(fileTemp.lastModified()).toString();
            backupBeans.add(bean);
        }
        return backupBeans;
    }
}

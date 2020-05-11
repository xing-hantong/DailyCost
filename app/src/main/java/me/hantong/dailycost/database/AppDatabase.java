package me.hantong.dailycost.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import me.hantong.dailycost.App;
import me.hantong.dailycost.database.converters.Converters;
import me.hantong.dailycost.database.dao.RecordDao;
import me.hantong.dailycost.database.dao.RecordTypeDao;
import me.hantong.dailycost.database.entity.Record;
import me.hantong.dailycost.database.entity.RecordType;


/**
 * 数据库
 *
 * @author X
 */
@Database(entities = {Record.class, RecordType.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "DailyCost.db";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(App.getINSTANCE(),
                            AppDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取记账类型操作类
     *
     * @return RecordTypeDao 记账类型操作类
     */
    public abstract RecordTypeDao recordTypeDao();

    /**
     * 获取记账操作类
     *
     * @return RecordDao 记账操作类
     */
    public abstract RecordDao recordDao();

}

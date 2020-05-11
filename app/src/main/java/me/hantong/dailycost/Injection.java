package me.hantong.dailycost;

import me.hantong.dailycost.database.AppDatabase;
import me.hantong.dailycost.datasource.AppDataSource;
import me.hantong.dailycost.datasource.LocalAppDataSource;
import me.hantong.dailycost.viewmodel.ViewModelFactory;

/**
 * @author X
 */
public class Injection {
    public static AppDataSource provideUserDataSource() {
        AppDatabase database = AppDatabase.getInstance();
        return new LocalAppDataSource(database);
    }

    public static ViewModelFactory provideViewModelFactory() {
        AppDataSource dataSource = provideUserDataSource();
        return new ViewModelFactory(dataSource);
    }
}

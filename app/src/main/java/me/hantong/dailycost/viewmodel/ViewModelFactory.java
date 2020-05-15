package me.hantong.dailycost.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import me.hantong.dailycost.datasource.AppDataSource;
import me.hantong.dailycost.ui.add.AddRecordViewModel;
import me.hantong.dailycost.ui.addtype.AddTypeViewModel;
import me.hantong.dailycost.ui.home.HomeViewModel;
import me.hantong.dailycost.ui.statistics.bill.BillViewModel;
import me.hantong.dailycost.ui.statistics.reports.ReportsViewModel;
import me.hantong.dailycost.ui.typemanage.TypeManageViewModel;
import me.hantong.dailycost.ui.typerecords.TypeRecordsViewModel;
import me.hantong.dailycost.ui.typesort.TypeSortViewModel;

/**
 * ViewModel 工厂
 *
 * @author X
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final AppDataSource mDataSource;

    public ViewModelFactory(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddRecordViewModel.class)) {
            return (T) new AddRecordViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeManageViewModel.class)) {
            return (T) new TypeManageViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeSortViewModel.class)) {
            return (T) new TypeSortViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(AddTypeViewModel.class)) {
            return (T) new AddTypeViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(BillViewModel.class)) {
            return (T) new BillViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(ReportsViewModel.class)) {
            return (T) new ReportsViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeRecordsViewModel.class)) {
            return (T) new TypeRecordsViewModel(mDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

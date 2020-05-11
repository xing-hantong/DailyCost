package me.hantong.dailycost.base;

import androidx.lifecycle.ViewModel;

import me.hantong.dailycost.datasource.AppDataSource;

/**
 * ViewModel基类
 * 包含 AppDataSource 数据源
 *
 * @author X
 */
public class BaseViewModel extends ViewModel {
    protected AppDataSource mDataSource;

    public BaseViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }
}

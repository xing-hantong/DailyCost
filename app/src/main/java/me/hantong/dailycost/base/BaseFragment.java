package me.hantong.dailycost.base;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.disposables.CompositeDisposable;


/**
 * 1.ViewDataBinding 封装
 * 2.数据懒加载：配合 ViewPager 时，需要 ViewPager#setOffscreenPageLimit 为最大
 * https://github.com/Lesincs/LazyInitFrag-Demo/
 * https://juejin.im/post/5a9398b56fb9a0634e6cb19a
 *
 * @author X

 */

public abstract class BaseFragment extends Fragment {

    /**
     * 标志位 判断数据是否初始化
     */
    private boolean isInitData = false;
    /**
     * 标志位 判断fragment是否可见
     */
    private boolean isVisibleToUser = false;
    /**
     * 标志位 判断view已经加载完成 避免空指针操作
     */
    private boolean isPrepareView = false;
    private ViewDataBinding dataBinding;
    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        View rootView = dataBinding.getRoot();
        onInit(savedInstanceState);
        return rootView;
    }

    /**
     * 子类必须实现，用于创建 view
     *
     * @return 布局文件 Id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 开始的方法
     *
     * @param savedInstanceState 保存的 Bundle
     */
    protected abstract void onInit(@Nullable Bundle savedInstanceState);

    /**
     * 获取 ViewDataBinding
     *
     * @param <T> BaseFragment#getLayoutId() 布局创建的 ViewDataBinding
     *            如 R.layout.fragment_demo 会创建出 FragmentDemoBinding.java
     * @return T
     */
    protected <T extends ViewDataBinding> T getDataBinding() {
        return (T) dataBinding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepareView = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        initData();
    }

    /**
     * 懒加载方法
     */
    private void initData() {
        if (!isInitData && isVisibleToUser && isPrepareView) {
            isInitData = true;
            lazyInitData();
        }
    }

    /**
     * 加载数据的方法,由子类实现
     */
    protected abstract void lazyInitData();

    protected View inflate(@LayoutRes int resource) {
        return getLayoutInflater().inflate(resource, null, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}

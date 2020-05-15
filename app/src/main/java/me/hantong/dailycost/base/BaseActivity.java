package me.hantong.dailycost.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import io.reactivex.disposables.CompositeDisposable;
import me.hantong.dailycost.utill.StatusBarUtil;

/**
 * 1.沉浸式状态栏
 * 2.ViewDataBinding 封装
 *
 * @author X

 */
public abstract class BaseActivity extends AppCompatActivity {

    private ViewDataBinding dataBinding;
    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        onInit(savedInstanceState);
    }

    /**
     * 子类必须实现，用于创建 view
     *
     * @return 布局文件 Id
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * 获取 ViewDataBinding
     *
     * @param <T> BaseActivity#getLayoutId() 布局创建的 ViewDataBinding
     *            如 R.layout.activity_demo 会创建出 ActivityDemoBinding.java
     * @return T
     */
    protected <T extends ViewDataBinding> T getDataBinding() {
        return (T) dataBinding;
    }

    /**
     * 设置沉浸式状态栏
     */
    private void setImmersiveStatus() {
        View[] views = setImmersiveView();
        if (views == null || views.length < 1) {
            return;
        }
        StatusBarUtil.immersive(this);
        for (View view : views) {
            StatusBarUtil.setPaddingSmart(this, view);
        }
    }

    /**
     * 子类重写该方法设置沉浸式状态栏
     *
     * @return null 或 view[]大小为0,则不启用沉浸式
     */
    protected View[] setImmersiveView() {
        ViewGroup rootView = (ViewGroup) dataBinding.getRoot();
        return new View[]{rootView.getChildAt(0)};
    }

    /**
     * 是否已经设置了沉浸式状态栏
     */
    private boolean isSetupImmersive;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSetupImmersive) {
            setImmersiveStatus();
            isSetupImmersive = true;
        }
    }

    /**
     * 开始的方法
     *
     * @param savedInstanceState 保存的 Bundle
     */
    protected abstract void onInit(@Nullable Bundle savedInstanceState);

    /**
     * inflate view root：null，attachToRoot：false
     *
     * @param resource 布局 id
     * @return view
     */
    protected View inflate(@LayoutRes int resource) {
        return getLayoutInflater().inflate(resource, null, false);
    }

    @Override
    public Resources getResources() {
        // 固定字体大小，不随系统字体大小改变
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }

}
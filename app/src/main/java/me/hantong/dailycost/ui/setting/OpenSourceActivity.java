package me.hantong.dailycost.ui.setting;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import me.hantong.dailycost.R;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.databinding.ActivitySettingBinding;
import me.hantong.dailycost.utill.CustomTabsUtil;

/**
 * @author X
 * @date 2020/5/22
 */
public class OpenSourceActivity extends BaseActivity {
    private ActivitySettingBinding mBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_open_source));

        mBinding.rvSetting.setLayoutManager(new LinearLayoutManager(this));
        OpenSourceAdapter adapter = new OpenSourceAdapter(null);
        mBinding.rvSetting.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) ->
                CustomTabsUtil.openWeb(this, adapter.getData().get(position).url));

        List<OpenSourceBean> list = new ArrayList<>();
        OpenSourceBean support = new OpenSourceBean("android support libraries - Google",
                "https://source.android.com",
                "Apache Software License 2.0");
        OpenSourceBean lifecycle = new OpenSourceBean("android arch lifecycle - Google",
                "https://source.android.com",
                "Apache Software License 2.0");
        OpenSourceBean room = new OpenSourceBean("android arch room - Google",
                "https://source.android.com",
                "Apache Software License 2.0");
        OpenSourceBean rxJava = new OpenSourceBean("RxJava - ReactiveX",
                "https://github.com/ReactiveX/RxJava",
                "Apache Software License 2.0");
        OpenSourceBean rxAndroid = new OpenSourceBean("RxAndroid - ReactiveX",
                "https://github.com/ReactiveX/rxAndroid",
                "Apache Software License 2.0");
        OpenSourceBean leakcanary = new OpenSourceBean("leakcanary - square",
                "https://github.com/square/leakcanary",
                "Apache Software License 2.0");
        OpenSourceBean BRVAH = new OpenSourceBean("BRVAH - CymChad",
                "https://github.com/CymChad/BaseRecyclerViewAdapterHelper",
                "Apache Software License 2.0");
        OpenSourceBean chart = new OpenSourceBean("MPAndroidChart - PhilJay",
                "https://github.com/PhilJay/MPAndroidChart",
                "Apache Software License 2.0");
        OpenSourceBean floo = new OpenSourceBean("floo - drakeet",
                "https://github.com/drakeet/Floo",
                "Apache Software License 2.0");
        OpenSourceBean layoutManage = new OpenSourceBean("StatusLayoutManager - Bakumon",
                "https://github.com/Bakumon/StatusLayoutManager",
                "MIT License");
        OpenSourceBean permission = new OpenSourceBean("AndPermission - yanzhenjie",
                "https://github.com/yanzhenjie/AndPermission",
                "Apache Software License 2.0");
        OpenSourceBean storage = new OpenSourceBean("android-storage - sromku",
                "https://github.com/sromku/android-storage",
                "Apache Software License 2.0");
        OpenSourceBean datePicker = new OpenSourceBean("MaterialDateTimePicker - wdullaer",
                "https://github.com/wdullaer/MaterialDateTimePicker",
                "Apache Software License 2.0");
        OpenSourceBean pagerLayout = new OpenSourceBean("pager-layoutmanager - GcsSloop",
                "https://github.com/GcsSloop/pager-layoutmanager",
                "Apache Software License 2.0");
        OpenSourceBean layoutManagerGroup = new OpenSourceBean("LayoutManagerGroup - DingMouRen",
                "https://github.com/DingMouRen/LayoutManagerGroup",
                "Apache Software License 2.0");
        OpenSourceBean alipayZeroSDK = new OpenSourceBean("AlipayZeroSdk - fython",
                "https://github.com/fython/AlipayZeroSdk",
                "Apache Software License 2.0");

        list.add(support);
        list.add(lifecycle);
        list.add(room);
        list.add(rxJava);
        list.add(rxAndroid);
        list.add(leakcanary);
        list.add(BRVAH);
        list.add(chart);
        list.add(floo);
        list.add(layoutManage);
        list.add(permission);
        list.add(storage);
        list.add(datePicker);
        list.add(pagerLayout);
        list.add(layoutManagerGroup);
        list.add(alipayZeroSDK);

        adapter.setNewData(list);
    }

}

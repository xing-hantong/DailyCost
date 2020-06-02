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
        OpenSourceBean google_services = new OpenSourceBean("android services - Google",
                "https://source.android.com",
                "Apache Software License 2.0");
        OpenSourceBean room = new OpenSourceBean("android arch room - Google",
                "https://source.android.com",
                "Apache Software License 2.0");
        OpenSourceBean firebase = new OpenSourceBean("firebase Authentication - Google",
                "https://github.com/firebase/FirebaseUI-Android",
                "Apache Software License 2.0");
        OpenSourceBean facebookSdk = new OpenSourceBean("Facebook Authentication - Facebook",
                "https://github.com/facebook/facebook-android-sdk",
                "Facebook Inc.");
        OpenSourceBean rxAndroid = new OpenSourceBean("RxAndroid - ReactiveX",
                "https://github.com/ReactiveX/rxAndroid",
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
        OpenSourceBean permission = new OpenSourceBean("AndPermission - yanzhenjie",
                "https://github.com/yanzhenjie/AndPermission",
                "Apache Software License 2.0");
        OpenSourceBean storage = new OpenSourceBean("android-storage - sromku",
                "https://github.com/sromku/android-storage",
                "Apache Software License 2.0");
        OpenSourceBean AndResGuard = new OpenSourceBean("AndResGuard - shwenzhang",
                "https://github.com/shwenzhang/AndResGuard",
                "Apache Software License 2.0");
        OpenSourceBean butterknife = new OpenSourceBean("butterknife - JakeWharton",
                "https://github.com/JakeWharton/butterknife",
                "Apache Software License 2.0");




        list.add(support);
        list.add(lifecycle);
        list.add(google_services);
        list.add(room);
        list.add(firebase);
        list.add(facebookSdk);
        list.add(rxAndroid);
        list.add(BRVAH);
        list.add(chart);
        list.add(floo);
        list.add(permission);
        list.add(storage);
        list.add(AndResGuard);
        list.add(butterknife);


        adapter.setNewData(list);
    }

}

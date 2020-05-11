package me.hantong.dailycost;

import android.app.Application;

import androidx.databinding.library.baseAdapters.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;
import java.util.Map;

import me.drakeet.floo.Floo;
import me.drakeet.floo.Target;
import me.drakeet.floo.extensions.LogInterceptor;
import me.drakeet.floo.extensions.OpenDirectlyHandler;

/**
 * @author X
 */
public class App extends Application {
    private static App INSTANCE;

    public static App getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        INSTANCE = this;
        Map<String, Target> mappings = new HashMap<>(8);
        mappings.put(Router.Url.URL_HOME, new Target("mk://localhost/home"));
        mappings.put(Router.Url.URL_ADD_RECORD, new Target("mk://localhost/addRecord"));
        mappings.put(Router.Url.URL_TYPE_MANAGE, new Target("mk://localhost/typeManage"));
        mappings.put(Router.Url.URL_TYPE_SORT, new Target("mk://localhost/typeSort"));
        mappings.put(Router.Url.URL_ADD_TYPE, new Target("mk://localhost/addType"));
        mappings.put(Router.Url.URL_STATISTICS, new Target("mk://localhost/statistics"));
        mappings.put(Router.Url.URL_TYPE_RECORDS, new Target("mk://localhost/typeRecords"));
        mappings.put(Router.Url.URL_SETTING, new Target("mk://localhost/setting"));
        mappings.put(Router.Url.URL_OPEN_SOURCE, new Target("mk://localhost/openSource"));
        mappings.put(Router.Url.URL_ABOUT, new Target("mk://localhost/about"));

        Floo.configuration()
                .setDebugEnabled(BuildConfig.DEBUG)
                .addRequestInterceptor(new PureSchemeInterceptor(getString(R.string.scheme)))
                .addRequestInterceptor(new LogInterceptor("Request"))
                .addTargetInterceptor(new PureSchemeInterceptor(getString(R.string.scheme)))
                .addTargetInterceptor(new LogInterceptor("Target"))
                .addTargetNotFoundHandler(new OpenDirectlyHandler());

        Floo.apply(mappings);
    }
}

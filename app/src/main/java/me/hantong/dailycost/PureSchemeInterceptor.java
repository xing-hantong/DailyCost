package me.hantong.dailycost;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BuildConfig;

import me.drakeet.floo.Chain;
import me.drakeet.floo.Interceptor;

/**
 * Floo 路由 scheme 拦截器，修正 debug 下的 scheme
 * @author X
 */
public class PureSchemeInterceptor implements Interceptor {
    private @NonNull
    final String scheme;

    public PureSchemeInterceptor(@NonNull String scheme) {
        this.scheme = scheme;
    }

    @NonNull
    @Override
    public Chain intercept(@NonNull Chain chain) {
        if (BuildConfig.DEBUG && Router.SCHEME.equals(chain.request().getScheme())) {
            chain = new Chain(chain.request().buildUpon().scheme(scheme).build());
        }
        return chain;
    }
}

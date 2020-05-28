package me.hantong.dailycost.ui.login;

/**
 * @author X
 * @date 2020/5/28
 */

import android.content.Context;

import com.firebase.ui.auth.AuthUI;
import me.hantong.dailycost.R;
import com.google.firebase.auth.ActionCodeSettings;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public final class ConfigurationUtils {

    private ConfigurationUtils() {
        throw new AssertionError("No instance for you!");
    }

    public static boolean isGoogleMisconfigured(@NonNull Context context) {
        return context.getString(R.string.default_web_client_id).equals("CHANGE-ME");
    }

    public static boolean isFacebookMisconfigured(@NonNull Context context) {
        return context.getString(R.string.facebook_application_id).equals("CHANGE-ME");

    }

    @NonNull
    public static List<AuthUI.IdpConfig> getConfiguredProviders(@NonNull Context context) {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();

        if (!isGoogleMisconfigured(context)) {
            providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        }
//        providers.add(new AuthUI.IdpConfig.GoogleBuilder().build());

        if (!isFacebookMisconfigured(context)) {
            providers.add(new AuthUI.IdpConfig.FacebookBuilder().build());
        }
//        providers.add(new AuthUI.IdpConfig.FacebookBuilder().build());

        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setAndroidPackageName("me.hantong.dailycost", true, null)
                .setHandleCodeInApp(true)
                .setUrl("https://google.com")
                .build();

        providers.add(new AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings)
                .build());

        providers.add(new AuthUI.IdpConfig.TwitterBuilder().build());
        providers.add(new AuthUI.IdpConfig.PhoneBuilder().build());
        providers.add(new AuthUI.IdpConfig.MicrosoftBuilder().build());
        providers.add(new AuthUI.IdpConfig.YahooBuilder().build());
        providers.add(new AuthUI.IdpConfig.AppleBuilder().build());

        return providers;
    }
}

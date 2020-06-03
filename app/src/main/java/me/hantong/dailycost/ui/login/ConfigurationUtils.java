package me.hantong.dailycost.ui.login;

/**
 * @author X
 * @date 2020/5/28
 */

import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.ActionCodeSettings;

import java.util.ArrayList;
import java.util.List;

import me.hantong.dailycost.R;

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

}

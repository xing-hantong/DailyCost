package me.hantong.dailycost.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.hantong.dailycost.Router;
import me.drakeet.floo.Floo;

/**
 * LauncherActivity
 *
 * @author X

 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Floo.navigation(this, Router.Url.URL_HOME).start();
        finish();
    }
}

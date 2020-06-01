package me.hantong.dailycost.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.drakeet.floo.Floo;
import me.hantong.dailycost.Router;

/**
 * LauncherActivity
 *
 * @author X

 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Floo.navigation(this, Router.Url.URL_LOGIN).start();
        finish();
    }
}

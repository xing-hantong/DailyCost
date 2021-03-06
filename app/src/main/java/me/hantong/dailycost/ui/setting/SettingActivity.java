package me.hantong.dailycost.ui.setting;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.ExtraConstants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.floo.Floo;
import me.hantong.dailycost.ConfigManager;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.databinding.ActivitySettingBinding;
import me.hantong.dailycost.ui.login.SignedInActivity;
import me.hantong.dailycost.utill.ToastUtils;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * 设置
 *
 * @author X
 */
public class SettingActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private ActivitySettingBinding mBinding;
    private SettingViewModel mViewModel;
    private SettingAdapter mAdapter;
    private IdpResponse mResponse;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);

        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initView() {
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_setting));

        mBinding.rvSetting.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SettingAdapter(null);
        mResponse = getIntent().getParcelableExtra(ExtraConstants.IDP_RESPONSE);

        List<SettingSectionEntity> list = new ArrayList<>();

        list.add(new SettingSectionEntity(getString(R.string.text_setting_money)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_setting_type_manage), null)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_manage_your_account), null)));

        list.add(new SettingSectionEntity(getString(R.string.text_setting_backup)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_go_backup), getString(R.string.text_setting_go_backup_content))));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_setting_restore), getString(R.string.text_setting_restore_content))));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_setting_auto_backup), getString(R.string.text_setting_auto_backup_content), ConfigManager.isAutoBackup())));

        list.add(new SettingSectionEntity(getString(R.string.text_other)));
        list.add(new SettingSectionEntity(new SettingSectionEntity.Item(getString(R.string.text_setting_lisence), null)));


        mAdapter.setNewData(list);

        mAdapter.setOnItemClickListener((adapter1, view, position) -> {
            switch (position) {
                case 1:
                    goTypeManage();
                    break;
                case 2:
                    goAccount();
                    break;
                case 4:
                    showBackupDialog();
                    break;
                case 5:
                    showRestoreDialog();
                    break;
                case 8:
                    goOpenSource();
                    break;
                default:
                    break;
            }
        });
        // Switch
        mAdapter.setOnItemChildClickListener((adapter12, view, position) -> {
            switch (position) {
                case 6:
                    switchAutoBackup(position);
                    break;
                default:
                    break;
            }
        });
        mBinding.rvSetting.setAdapter(mAdapter);
    }

    private void switchAutoBackup(int position) {
        boolean oldIsConfigOpen = mAdapter.getData().get(position).t.isConfigOpen;
        if (oldIsConfigOpen) {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(R.string.text_close_auto_backup)
                    .setMessage(R.string.text_close_auto_backup_tip)
                    .setNegativeButton(R.string.text_button_cancel, (dialog, which) -> mAdapter.notifyDataSetChanged())
                    .setPositiveButton(R.string.text_affirm, (dialog, which) -> setAutoBackup(position, false))
                    .create()
                    .show();
        } else {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ConfigManager.setIsAutoBackup(true);
                initView();
                return;
            }
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, 11, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                            .setRationale(R.string.text_storage_content)
                            .setPositiveButtonText(R.string.text_affirm)
                            .setNegativeButtonText(R.string.text_button_cancel)
                            .build());
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case 11:
                ConfigManager.setIsAutoBackup(true);
                initView();
                break;
            case 12:
                backupDB();
                break;
            case 13:
                restore();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setRationale(R.string.text_storage_permission_tip)
                    .setTitle(R.string.text_storage)
                    .setPositiveButton(R.string.text_affirm)
                    .setNegativeButton(R.string.text_button_cancel)
                    .build()
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            initView();
        }
    }

    private void setAutoBackup(int position, boolean isBackup) {
        ConfigManager.setIsAutoBackup(isBackup);
        mAdapter.getData().get(position).t.isConfigOpen = isBackup;
        mAdapter.notifyDataSetChanged();
    }

    private void showBackupDialog() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            backupDB();
            return;
        }
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, 12, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setRationale(R.string.text_storage_content)
                        .setPositiveButtonText(R.string.text_affirm)
                        .setNegativeButtonText(R.string.text_button_cancel)
                        .build());
    }

    private void backupDB() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.text_backup)
                .setMessage(R.string.text_backup_save)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_affirm, (dialog, which) -> {
                    mDisposable.add(mViewModel.backupDB()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(() -> ToastUtils.show(R.string.toast_backup_success),
                                    throwable -> {
                                        ToastUtils.show(R.string.toast_backup_fail);
                                        Log.e(TAG, "备份失败", throwable);
                                    }));
                })
                .create()
                .show();
    }

    private void showRestoreDialog() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            restore();
            return;
        }
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, 13, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setRationale(R.string.text_storage_content)
                        .setPositiveButtonText(R.string.text_affirm)
                        .setNegativeButtonText(R.string.text_button_cancel)
                        .build());
    }

    private void restore() {
        mDisposable.add(mViewModel.getBackupFiles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(backupBeans -> {
                            BackupFliesDialog dialog = new BackupFliesDialog(this, backupBeans);
                            dialog.setOnItemClickListener(file -> restoreDB(file.getPath()));
                            dialog.show();
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_backup_list_fail);
                            Log.e(TAG, "备份文件列表获取失败", throwable);
                        }));
    }

    private void restoreDB(String restoreFile) {
        mDisposable.add(mViewModel.restoreDB(restoreFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Floo.stack(this)
                                .target(Router.IndexKey.INDEX_KEY_HOME)
                                .result("refresh")
                                .start(),
                        throwable -> {
                            ToastUtils.show(R.string.toast_restore_fail);
                            Log.e(TAG, "恢复备份失败", throwable);
                        }));
    }

    private void goTypeManage() {
        Floo.navigation(this, Router.Url.URL_TYPE_MANAGE)
                .start();
    }

    private void goOpenSource() {
        Floo.navigation(this, Router.Url.URL_OPEN_SOURCE)
                .start();
    }

    private void startSignedInActivity(@Nullable IdpResponse response) {
        startActivity(SignedInActivity.createIntent(this, response));
    }

    private void goAccount() {
        startSignedInActivity(mResponse);
        finish();
    }
}

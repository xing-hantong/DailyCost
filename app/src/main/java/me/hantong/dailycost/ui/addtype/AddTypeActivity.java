package me.hantong.dailycost.ui.addtype;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.hantong.dailycost.App;
import me.hantong.dailycost.Injection;
import me.hantong.dailycost.R;
import me.hantong.dailycost.Router;
import me.hantong.dailycost.base.BaseActivity;
import me.hantong.dailycost.database.entity.RecordType;
import me.hantong.dailycost.databinding.ActivityAddTypeBinding;
import me.hantong.dailycost.datasource.BackupFailException;
import me.hantong.dailycost.utill.ToastUtils;
import me.hantong.dailycost.viewmodel.ViewModelFactory;

/**
 * 添加或修改记账类型
 *
 * @author X
 */
public class AddTypeActivity extends BaseActivity {

    private static final String TAG = AddTypeActivity.class.getSimpleName();

    private static final int COLUMN = 4;

    private ActivityAddTypeBinding mBinding;
    private AddTypeViewModel mViewModel;
    private TypeImgAdapter mAdapter;

    private int mType;
    private RecordType mRecordType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_type;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTypeViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mType = getIntent().getIntExtra(Router.ExtraKey.KEY_TYPE, RecordType.TYPE_OUTLAY);
        mRecordType = (RecordType) getIntent().getSerializableExtra(Router.ExtraKey.KEY_TYPE_BEAN);

        String prefix = mRecordType == null ? getString(R.string.text_add) : getString(R.string.text_modify);
        String type = mType == RecordType.TYPE_OUTLAY ? getString(R.string.text_outlay_type) : getString(R.string.text_income_type);

        mBinding.edtTypeName.setText(mRecordType == null ? "" : mRecordType.name);
        mBinding.edtTypeName.setSelection(mBinding.edtTypeName.getText().length());

        mBinding.titleBar.setTitle(prefix + type);
        mBinding.titleBar.tvRight.setText(R.string.text_save);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.tvRight.setOnClickListener(v -> saveType());

        mBinding.rvType.setLayoutManager(new GridLayoutManager(this, COLUMN));
        mAdapter = new TypeImgAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> checkItem(position));
    }

    private void checkItem(int position) {
        mAdapter.checkItem(position);
        int resId = getResources().getIdentifier(mAdapter.getCurrentItem().imgName, "mipmap", getPackageName());
        mBinding.ivType.setImageResource(resId);
    }

    private void initData() {
        getAllTypeImg();
    }

    private void getAllTypeImg() {
        mDisposable.add(mViewModel.getAllTypeImgBeans(mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((typeImgBeans) -> {
                    mAdapter.setNewData(typeImgBeans);
                    if (mRecordType == null) {
                        checkItem(0);
                    } else {
                        for (int i = 0; i < typeImgBeans.size(); i++) {
                            if (TextUtils.equals(mRecordType.imgName, typeImgBeans.get(i).imgName)) {
                                checkItem(i);
                                return;
                            }
                        }
                    }
                }, throwable -> {
                    ToastUtils.show(R.string.toast_type_img_fail);
                    Log.e(TAG, "类型图片获取失败", throwable);
                }));
    }

    private void saveType() {
        mBinding.titleBar.tvRight.setEnabled(false);
        String text = mBinding.edtTypeName.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Animation animation = AnimationUtils.loadAnimation(App.getINSTANCE(), R.anim.shake);
            mBinding.edtTypeName.startAnimation(animation);
            mBinding.titleBar.tvRight.setEnabled(true);
            return;
        }
        TypeImgBean bean = mAdapter.getCurrentItem();
        mDisposable.add(mViewModel.saveRecordType(mRecordType, mType, bean.imgName, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish, throwable -> {
                    if (throwable instanceof BackupFailException) {
                        ToastUtils.show(throwable.getMessage());
                        Log.e(TAG, "备份失败（类型保存失败的时候）", throwable);
                        finish();
                    } else {
                        mBinding.titleBar.tvRight.setEnabled(true);
                        String failTip = TextUtils.isEmpty(throwable.getMessage()) ? getString(R.string.toast_type_save_fail) : throwable.getMessage();
                        ToastUtils.show(failTip);
                        Log.e(TAG, "类型保存失败", throwable);
                    }
                }));
    }
}

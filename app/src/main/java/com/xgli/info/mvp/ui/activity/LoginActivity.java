package com.xgli.info.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.xgli.info.di.component.DaggerLoginComponent;
import com.xgli.info.mvp.contract.LoginContract;
import com.xgli.info.mvp.presenter.LoginPresenter;

import com.xgli.info.R;


import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.base.bean.BaseBean;
import me.jessyan.armscomponent.commonsdk.core.Constants;
import me.jessyan.armscomponent.commonsdk.data.UserManager;
import me.jessyan.armscomponent.commonsdk.utils.AppUtil;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/09/2019 13:23
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
@BindView(R.id.et_phone)
  EditText userName;
@BindView(R.id.et_password)
  EditText passWord;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }
    private void login() {
        if (TextUtils.isEmpty(userName.getText())) {
//            userName.startShakeAnimation();
            ToastUtil.showToast(getString(R.string.account_null));
            return;
        }
        if (TextUtils.isEmpty(passWord.getText())) {
//            passWord.startShakeAnimation();
            ToastUtil.showToast(getString(R.string.password_null));
            return;
        }
        if (!AppUtil.isNetworkAvailable()) {
            ToastUtil.showToast(getString(R.string.network_tip));
            return;
        }
        mPresenter.login(userName.getText().toString().trim()
                , passWord.getText().toString().trim());
    }
    @OnClick({R.id.btn_login,R.id.tv_register,R.id.tv_forget_password
    })
     void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_login) {
            boolean isNetConnected = AppUtil.isNetworkAvailable();
            if (!isNetConnected) {
                ToastUtil.showToast(getString(R.string.network_tip));
            } else {
                login();
            }

        } else if (i == R.id.tv_register) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, 1);

        } else if (i == R.id.tv_forget_password) {
//            LogUtil.e("忘记密码");
        }
    }

    @Override
    public void updateData(BaseBean baseBean) {
        boolean isFirstLogin =   MyApplication.mComponent.getAppDataManager()
                .getBoolean(UserManager.getInstance().getCurrentUserObjectId() + Constants.FIRST_STATUS, false);

        MyApplication.mComponent
                .getAppDataManager().
               putBoolean(UserManager.getInstance().getCurrentUserObjectId() + Constants.FIRST_STATUS, !isFirstLogin);
//        dealResultInfo(isFirstLogin);

        ArmsUtils.startActivity(MainActivity.class);
    }

//    private void dealResultInfo(boolean isFirstLogin) {
//        if (getAppComponent().getSharedPreferences().getBoolean(Constant.ALONE, false)) {
//            Router.getInstance().deal(RouterRequest
//                    .newBuild().context(getActivity()).isFinish(true).provideName(RouterConfig.MAIN_PROVIDE_NAME)
//                    .actionName("login").build());
//        } else {
//            HomeActivity.start(getActivity(), isFirstLogin);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_REGISTER:
                    String name = data.getStringExtra("username");
                    String password = data.getStringExtra("password");
                    if (name != null && password != null) {
                        passWord.setText(password);
                        userName.setText(name);
                    }
                    MyApplication.mComponent
                            .getAppDataManager()
                            .putBoolean(UserManager.getInstance().getCurrentUserObjectId() + Constants.FIRST_STATUS, true);
                    login();
                    break;

            }
        }
    }
    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}

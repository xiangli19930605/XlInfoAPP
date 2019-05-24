package com.xgli.info.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.xgli.info.di.component.DaggerRegisterComponent;
import com.xgli.info.mvp.contract.RegisterContract;
import com.xgli.info.mvp.presenter.RegisterPresenter;

import com.xgli.info.R;


import butterknife.BindView;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import me.jessyan.armscomponent.commonsdk.bean.CustomInstallation;
import me.jessyan.armscomponent.commonsdk.bean.RandomData;
import me.jessyan.armscomponent.commonsdk.bean.User;
import me.jessyan.armscomponent.commonsdk.utils.AppUtil;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.armscomponent.commonsdk.widget.CustomEditText;
import me.jessyan.autosize.utils.LogUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/10/2019 14:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    @BindView(R.id.aet_register_name)
     CustomEditText name;
    @BindView(R.id.aet_register_password)
     CustomEditText passWord;
    @BindView(R.id.aet_register_password_confirm)
     CustomEditText passWordComfirm;
    @BindView(R.id.btn_register_confirm)
     Button register;
    @BindView(R.id.iv_register_bg)
     ImageView bg;
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_register; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        register.setOnClickListener(view -> {
            if (TextUtils.isEmpty(name.getText().toString().trim())) {
                ToastUtil.showToast(getString(R.string.account_null));
                name.startShakeAnimation();
                return;
            }
            if (TextUtils.isEmpty(passWord.getText())) {
                ToastUtil.showToast(getString(R.string.password_null));
                return;
            }
            if (TextUtils.isEmpty(passWordComfirm.getText())) {
                ToastUtil.showToast(getString(R.string.password_null));
                passWordComfirm.startShakeAnimation();
                return;
            }
            if (!passWord.getText().toString().trim().equals(passWordComfirm.getText().toString().trim())) {
                ToastUtil.showToast(getString(R.string.register_password_error));
                return;
            }
            if (!AppUtil.isNetworkAvailable()) {
                ToastUtil.showToast(getString(R.string.network_tip));
                return;
            }
//            showLoadDialog("正在注册，请稍候.......");
            User user = new User();
            //                                默认注册为男性
            user.setSex(true);
            //                                 设备类型
            user.setDeviceType("android");
            //                                与设备ID绑定
            CustomInstallation customInstallation = new CustomInstallation();
            user.setInstallId(customInstallation.getInstallationId());
            user.setNick(RandomData.getRandomNick());
            user.setSignature(RandomData.getRandomSignature());
            user.setAvatar(RandomData.getRandomAvatar());
            LogUtils.e("用户的头像信息:" + user.getAvatar());

            user.setUsername(name.getText().toString().trim());
            user.setPassword(passWord.getText().toString().trim());

            user.setTitleWallPaper(RandomData.getRandomTitleWallPaper());
            user.setSchool("中国地质大学(武汉)");
            user.setName(RandomData.getRandomName());
            user.setCollege(RandomData.getRandomCollege());
            user.setYear(RandomData.getRandomYear());
            user.setEducation(RandomData.getRandomEducation());
            user.setClassNumber(RandomData.getRandomClassNumber());
            user.setMajor(RandomData.getRandomMajor());
            user.setWallPaper(RandomData.getRandomWallPaper());
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User s, BmobException e) {
                    if (e == null) {
                        ToastUtil.showToast("注册成功");
                        //   进行用户Id和设备的绑定
//                        if (UserManager.getInstance().getCurrentUser() != null) {
//                            LogUtils.e("uid：" + UserManager.getInstance().getCurrentUser().getObjectId());
//                        }
                        dealFinish();

                    } else {
                        ToastUtil.showToast("注册失败" + e.toString());
                        LogUtils.e(e.toString());
                    }
                }
            });
        });
    }
    private void dealFinish() {
        Intent intent = new Intent();
        intent.putExtra("username", name.getText().toString().trim());
        intent.putExtra("password", passWord.getText().toString().trim());
        setResult(Activity.RESULT_OK, intent);
        finish();
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

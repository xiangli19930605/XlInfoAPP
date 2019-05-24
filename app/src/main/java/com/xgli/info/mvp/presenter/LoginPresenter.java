package com.xgli.info.mvp.presenter;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.UserManager;

import com.jess.arms.base.BaseApplication;
import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.CustomInstallation;
import me.jessyan.armscomponent.commonsdk.bean.User;
import me.jessyan.armscomponent.commonsdk.core.Constants;
import me.jessyan.armscomponent.commonsdk.data.AppDataManager;
import me.jessyan.armscomponent.commonsdk.utils.ToastUtil;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.xgli.info.mvp.contract.LoginContract;


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
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void login(String account, String password) {
//        iView.showLoading("正在登录......");
        final User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        AppDataManager sharedPreferences = MyApplication.mComponent.getAppDataManager();

        if (sharedPreferences.getString(Constants.LONGITUDE) != "") {
            user.setLocation(new BmobGeoPoint(Double.parseDouble(sharedPreferences.getString(Constants.LONGITUDE)),
                    Double.parseDouble(sharedPreferences.getString(Constants.LATITUDE))));
        }
        user.login(new SaveListener<BmobUser>() {
                                       @Override
                                       public void done(BmobUser bmobUser, BmobException e) {
                                           if (e == null) {
                                               ToastUtil.showToast("登录成功");
                                               mRootView.updateData(null);
                                               sharedPreferences.putBoolean(Constants.LOGIN_STATUS, true);
                                               //                                        登录成功之后，
                                               //                                        检查其他设备绑定的用户，强迫其下线
//                                               LogUtil.e("检查该用户绑定的其他设备.....");
//                                               addSubscription(UserManager.getInstance().checkInstallation(new UpdateListener() {
//                                                   @Override
//                                                   public void done(BmobException e) {
//                                                       UserManager.getInstance().updateUserInfo(ConstantUtil.INSTALL_ID,new CustomInstallation().getInstallationId()
//                                                               ,null);
//                                                       if (e == null) {
//                                                           iView.showLoading("正在获取好友资料.........");
//                                                           updateUserInfo();
//                                                       } else {
//                                                           ToastUtils.showShortToast("登录失败,请重新登录" + e.toString());
//                                                           CommonLogger.e("登录失败" + e.toString());
//                                                           iView.hideLoading();
//                                                       }
//                                                   }
//                                               }));
                                           } else {
                                               ToastUtil.showToast("登录失败" + e.toString());
//                                               iView.hideLoading();
                                           }
                                       }
                                   }
        );
    }




}

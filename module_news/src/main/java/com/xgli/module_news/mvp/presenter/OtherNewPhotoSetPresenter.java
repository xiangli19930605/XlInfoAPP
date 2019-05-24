package com.xgli.module_news.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.xgli.module_news.mvp.contract.OtherNewPhotoSetContract;
import com.xgli.module_news.mvp.model.entity.PhotoSetBean;
import com.xgli.module_news.mvp.model.entity.PictureBean;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/28/2019 16:09
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class OtherNewPhotoSetPresenter extends BasePresenter<OtherNewPhotoSetContract.Model, OtherNewPhotoSetContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public OtherNewPhotoSetPresenter(OtherNewPhotoSetContract.Model model, OtherNewPhotoSetContract.View rootView) {
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

    public void getOtherNewPhotoSetData(final String photoSetId) {
        mModel.getPhotoSetData(  clipPhotoSetId(photoSetId))
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PhotoSetBean>(mErrorHandler) {
                    @Override
                    public void onNext(PhotoSetBean datas) {
                        mRootView.updateData(datas);
                    }
                });
    }


    /**
     * 裁剪图集ID
     *
     * @param photoId
     * @return
     */
    public  String clipPhotoSetId(String photoId) {
        if (TextUtils.isEmpty(photoId)) {
            return "";
        }
        int i = photoId.indexOf("|");
        if (i >= 4) {
            String result = photoId.replace('|', '/');
            return result.substring(i - 4);
        }
        return null;
    }





}

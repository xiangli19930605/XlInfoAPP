package com.xgli.module_news.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.mvp.contract.OtherNewsDetailContract;
import com.xgli.module_news.mvp.model.entity.OtherNewsDetailBean;
import com.xgli.module_news.mvp.model.entity.PhotoSetBean;

import java.util.Map;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/30/2019 10:02
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class OtherNewsDetailPresenter extends BasePresenter<OtherNewsDetailContract.Model, OtherNewsDetailContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public OtherNewsDetailPresenter(OtherNewsDetailContract.Model model, OtherNewsDetailContract.View rootView) {
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


    public void  getOtherNewsDetailData(final String postId) {
        mModel.getNewsDetail( postId)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<String, OtherNewsDetailBean>, OtherNewsDetailBean>() {
                    @Override
                    public OtherNewsDetailBean apply(@NonNull Map<String, OtherNewsDetailBean> stringOtherNewsDetailBeanMap) throws Exception {
                        return stringOtherNewsDetailBeanMap.get(postId);
                    }
                })
                .doOnNext(new Consumer<OtherNewsDetailBean>() {
                    @Override
                    public void accept(@NonNull OtherNewsDetailBean otherNewsDetailBean) throws Exception {
                        if (otherNewsDetailBean.getImg() != null && otherNewsDetailBean
                                .getImg().size() > 0) {
                            String body = otherNewsDetailBean.getBody();
                            for (OtherNewsDetailBean.ImgEntity imgEntity : otherNewsDetailBean.getImg()) {
                                String ref = imgEntity.getRef();
                                String src = imgEntity.getSrc();
                                String img = "<img src=\"http\" />".replace("http", src);
                                body = body.replaceAll(ref, img);
                            }
                            otherNewsDetailBean.setBody(body);
                        }
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁


                .subscribe(new ErrorHandleSubscriber<OtherNewsDetailBean>(mErrorHandler) {
                    @Override
                    public void onNext(OtherNewsDetailBean datas) {
                        mRootView.updateData(datas);
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        RetrofitUrlManager.getInstance().putDomain(NewsUtil.WANGYI_DOMAIN_NAME, NewsUtil.BASE_URL);
    }


}

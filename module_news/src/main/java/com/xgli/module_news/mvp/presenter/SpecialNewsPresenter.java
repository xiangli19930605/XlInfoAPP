package com.xgli.module_news.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.jess.arms.utils.LogUtils;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.mvp.contract.SpecialNewsContract;
import com.xgli.module_news.mvp.model.api.OtherNewsApi;
import com.xgli.module_news.mvp.model.entity.PhotoSetBean;
import com.xgli.module_news.mvp.model.entity.RawSpecialNewsBean;
import com.xgli.module_news.mvp.model.entity.SpecialNewsBean;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/28/2019 15:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class SpecialNewsPresenter extends BasePresenter<SpecialNewsContract.Model, SpecialNewsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public SpecialNewsPresenter(SpecialNewsContract.Model model, SpecialNewsContract.View rootView) {
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



    public void getSpecialNewsData(final String specialId) {
        mModel
                .getSpecialNewsData(specialId)
                .subscribeOn(Schedulers.io())
                .map(stringRawSpecialNewsBeanMap -> stringRawSpecialNewsBeanMap.get(specialId))
                .doOnNext(new Consumer<RawSpecialNewsBean>() {
                    @Override
                    public void accept(RawSpecialNewsBean rawSpecialNewsBean) throws Exception {
                        addDispose(Observable.just(rawSpecialNewsBean.getBanner()).subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        mRootView.updateBanner(s);
                                    }
                                }));
                    }
                }).flatMap(rawSpecialNewsBean -> Observable.fromIterable(rawSpecialNewsBean.getTopics())
                .flatMap(topicsEntity -> Observable.fromIterable(topicsEntity.getDocs())
                        .map(SpecialNewsBean::new).startWith(new SpecialNewsBean(topicsEntity.getShortname()))))
                .flatMap((Function<SpecialNewsBean, ObservableSource<SpecialNewsBean>>) specialNewsBean -> {
                    if (specialNewsBean != null && SpecialNewsBean.TYPE_PHOTO_SET == specialNewsBean.getType()) {
                        return mModel
                                .getPhotoSetData(clipPhotoSetId(specialNewsBean.getBean().getSkipID())).flatMap((Function<PhotoSetBean, ObservableSource<SpecialNewsBean>>) photoSetBean -> {
                                    if (photoSetBean.getPhotos() != null && photoSetBean.getPhotos().size() > 0) {
                                        List<RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity> list = new ArrayList<>();
                                        for (PhotoSetBean.PhotosEntity entity :
                                                photoSetBean.getPhotos()) {
                                            RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity item = new RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity();
                                            item.setImgsrc(entity.getImgurl());
                                            list.add(item);
                                        }
                                        specialNewsBean.getBean().setImgextra(list);
                                    }
                                    return Observable.fromArray(specialNewsBean);
                                });
                    }
                    return Observable.fromArray(specialNewsBean);
                }).toList().observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<SpecialNewsBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onSuccess(@NonNull List<SpecialNewsBean> specialNewsBeen) {
                mRootView.updateData(specialNewsBeen);

//                iView.hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
//                iView.showError(null, () -> getSpecialNewsData(specialId));
            }
        });

    }



    /**
     * 裁剪图集ID
     *
     * @param photoId
     * @return
     */
    public String clipPhotoSetId(String photoId) {
        //        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(ph);
        if (TextUtils.isEmpty(photoId)) {
            return "";
        }
        int i = photoId.indexOf("|");
        if (i >= 4) {
            String result = photoId.replace('|', '/');
            String str = result.substring(i - 4);
            LogUtils.debugInfo("photoId:" + str);
            return result.substring(i - 4);
        } else {
            LogUtils.debugInfo("空空空空空空");
        }
        return null;
    }
    @Override
    public void onStart() {
        super.onStart();
        RetrofitUrlManager.getInstance().putDomain(NewsUtil.WANGYI_DOMAIN_NAME, NewsUtil.BASE_URL);
    }




}

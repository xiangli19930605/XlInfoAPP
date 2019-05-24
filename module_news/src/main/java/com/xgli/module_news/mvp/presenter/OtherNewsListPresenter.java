package com.xgli.module_news.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.text.TextUtils;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.jess.arms.utils.LogUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.xgli.module_news.app.utils.NewsUtil;
import com.xgli.module_news.mvp.contract.OtherNewsListContract;
import com.xgli.module_news.mvp.model.api.OtherNewsApi;
import com.xgli.module_news.mvp.model.entity.NewInfoBean;
import com.xgli.module_news.mvp.model.entity.PhotoSetBean;
import com.xgli.module_news.mvp.model.entity.PictureBean;
import com.xgli.module_news.mvp.ui.fragment.adapter.OtherNewsListAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/17/2019 13:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class OtherNewsListPresenter extends BasePresenter<OtherNewsListContract.Model, OtherNewsListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    OtherNewsListAdapter mAdapter;
    @Inject
    List<NewInfoBean> mDatas;
    private int lastPage = 1;
    private int preEndIndex;
    int NUMBER_OF_PAGE = 10;

    @Inject
    public OtherNewsListPresenter(OtherNewsListContract.Model model, OtherNewsListContract.View rootView) {
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

    public void getNewsList(final boolean pullToRefresh, final String typeId) {
        if (pullToRefresh) lastPage = 1;//下拉刷新默认只请求第一页

        mModel.getNewsList(typeId.equals("T1348647909107") ? "headline" : "list", typeId, (lastPage - 1) * 20)
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(stringListMap -> Observable.fromIterable(stringListMap.get(typeId)))
                .flatMap((Function<NewInfoBean, ObservableSource<NewInfoBean>>) newInfoBean -> {
                    if (NewsUtil.PHOTO_SET.equals(newInfoBean.getSkipType()) && (newInfoBean.getImgextra() == null
                            || newInfoBean.getImgextra().size() < 3)) {
                        return mModel
                                .getPhotoSetData(clipPhotoSetId(newInfoBean.getPhotosetID())).subscribeOn(Schedulers.io()).flatMap((Function<PhotoSetBean, ObservableSource<NewInfoBean>>) photoSetBean -> {
                                    if (photoSetBean.getPhotos() != null && photoSetBean.getPhotos().size() > 0) {
                                        List<NewInfoBean.ImgextraEntity> list = new ArrayList<>();
                                        for (PhotoSetBean.PhotosEntity entity :
                                                photoSetBean.getPhotos()) {
                                            NewInfoBean.ImgextraEntity item = new NewInfoBean.ImgextraEntity();
                                            item.setImgsrc(entity.getImgurl());
                                            list.add(item);
                                        }
                                        newInfoBean.setImgextra(list);
                                    }
                                    return Observable.fromArray(newInfoBean);
                                }).observeOn(AndroidSchedulers.mainThread());
                    } else {
                        return Observable.fromArray(newInfoBean);
                    }
                })
                .toList()
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .subscribe(new SingleObserver<List<NewInfoBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<NewInfoBean> newInfoBeen) {
                        lastPage = lastPage + 1;
                        if (pullToRefresh) mDatas.clear();//如果是下拉刷新则清空列表

                        preEndIndex = mDatas.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mDatas.addAll(newInfoBeen);
                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged();
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex,newInfoBeen.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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

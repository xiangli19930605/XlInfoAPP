package com.xgli.module_news.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import javax.inject.Inject;

import com.jess.arms.utils.RxLifecycleUtils;
import com.xgli.module_news.mvp.contract.PhotoListContract;
import com.xgli.module_news.mvp.model.entity.PictureBean;
import com.xgli.module_news.mvp.ui.fragment.adapter.PhotoListAdapter;

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
public class PhotoListPresenter extends BasePresenter<PhotoListContract.Model, PhotoListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<PictureBean.PictureEntity> mDatas;
    @Inject
    PhotoListAdapter mAdapter;
    private int lastPage = 1;
    private int preEndIndex;
    int NUMBER_OF_PAGE = 10;
    @Inject
    public PhotoListPresenter(PhotoListContract.Model model, PhotoListContract.View rootView) {
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
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {

//        requestGirls(true);//打开 App 时自动加载列表
    }

    public void requestGirls(final boolean pullToRefresh) {
        if (pullToRefresh) lastPage = 1;//下拉刷新默认只请求第一页

        mModel.getGirlList(NUMBER_OF_PAGE, lastPage)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<PictureBean>(mErrorHandler) {
                    @Override
                    public void onNext(PictureBean datas) {
                        lastPage = lastPage + 1;
                        if (pullToRefresh) mDatas.clear();//如果是下拉刷新则清空列表

                        preEndIndex = mDatas.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mDatas.addAll(datas.getResults());
                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged();
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex, datas.getResults().size());
                    }
                });
    }






}

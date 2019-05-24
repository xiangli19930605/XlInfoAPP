package com.xgli.module_project.mvp.presenter;

import android.app.Application;

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
import com.xgli.module_project.mvp.contract.ProjectListContract;
import com.xgli.module_project.mvp.model.entity.BaseResponse;
import com.xgli.module_project.mvp.model.entity.FeedArticleData;
import com.xgli.module_project.mvp.model.entity.ProjectListData;
import com.xgli.module_project.mvp.ui.adapter.ProjectListAdapter;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/05/2019 15:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class ProjectListPresenter extends BasePresenter<ProjectListContract.Model, ProjectListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<FeedArticleData> mDatas;
    @Inject
    ProjectListAdapter mAdapter;
    private int lastPage = 1;
    private int preEndIndex;
    private boolean isFirst = true;
    @Inject
    public ProjectListPresenter(ProjectListContract.Model model, ProjectListContract.View rootView) {
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

    public void getProjectListData( int cid,final boolean pullToRefresh){
        if (pullToRefresh) lastPage = 1;//下拉刷新默认只请求第一页
        boolean isEvictCache = pullToRefresh;//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存
//        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
//            isFirst = false;
//            isEvictCache = false;
//        }
        mModel.getProjectListData(lastPage,cid)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
//                    if (pullToRefresh)
//                        mRootView.showLoading();//显示下拉刷新的进度条
//                    else
//                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse<ProjectListData>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse<ProjectListData> listBaseResponse) {
                        if (listBaseResponse.getData().getDatas().size() == 0) {
//                            mAdapter.setEmptyView();
                            mRootView.noLoadMore();
                            return;
                        }
                        lastPage = lastPage + 1;
                        if (pullToRefresh){
                            mAdapter.setNewData(listBaseResponse.getData().getDatas());
                        }
                        else {
                            mAdapter.addData(listBaseResponse.getData().getDatas());
                        }
                    }


                });



    }

}

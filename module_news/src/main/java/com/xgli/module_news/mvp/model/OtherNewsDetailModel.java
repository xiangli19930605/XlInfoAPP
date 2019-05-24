package com.xgli.module_news.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.xgli.module_news.mvp.contract.OtherNewsDetailContract;
import com.xgli.module_news.mvp.model.api.OtherNewsApi;
import com.xgli.module_news.mvp.model.entity.OtherNewsDetailBean;

import java.util.Map;

import io.reactivex.Observable;


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
public class OtherNewsDetailModel extends BaseModel implements OtherNewsDetailContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public OtherNewsDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Map<String, OtherNewsDetailBean>> getNewsDetail(String postId) {
        return  mRepositoryManager
                .obtainRetrofitService(OtherNewsApi.class)
                .getNewsDetail(postId);
    }
}
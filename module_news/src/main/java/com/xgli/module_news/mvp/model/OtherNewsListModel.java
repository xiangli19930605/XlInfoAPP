package com.xgli.module_news.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.FragmentScope;

import javax.inject.Inject;

import com.xgli.module_news.mvp.contract.OtherNewsListContract;
import com.xgli.module_news.mvp.model.api.OtherNewsApi;
import com.xgli.module_news.mvp.model.entity.NewInfoBean;
import com.xgli.module_news.mvp.model.entity.PhotoSetBean;
import com.xgli.module_news.mvp.model.entity.PictureBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


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
public class OtherNewsListModel extends BaseModel implements OtherNewsListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public OtherNewsListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<Map<String, List<NewInfoBean>>> getNewsList(String typeId, String id, int page) {
        return mRepositoryManager
                .obtainRetrofitService(OtherNewsApi.class)
                .getNewsList(typeId,id, page);
    }

    @Override
    public Observable<PhotoSetBean> getPhotoSetData(String photoId) {
        return mRepositoryManager
                .obtainRetrofitService(OtherNewsApi.class)
                .getPhotoSetData(photoId);
    }
}
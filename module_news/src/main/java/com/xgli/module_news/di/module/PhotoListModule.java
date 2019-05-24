package com.xgli.module_news.di.module;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xgli.module_news.mvp.contract.PhotoListContract;
import com.xgli.module_news.mvp.model.PhotoListModel;
import com.xgli.module_news.mvp.model.entity.PictureBean;
import com.xgli.module_news.mvp.ui.fragment.adapter.PhotoListAdapter;

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
@Module
public abstract class PhotoListModule {

    @Binds
    abstract PhotoListContract.Model bindPhotoListModel(PhotoListModel model);

    @FragmentScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(PhotoListContract.View view) {
        return new GridLayoutManager(view.getActivity(), 2);
    }

    @FragmentScope
    @Provides
    static PhotoListAdapter providePhotoListAdapter(List<PictureBean.PictureEntity> mList){
        return new PhotoListAdapter(mList);
    }

    @FragmentScope
    @Provides
    static List<PictureBean.PictureEntity> provideList() {
        return new ArrayList<>();
    }
}
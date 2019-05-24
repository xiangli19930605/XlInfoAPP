package com.xgli.module_news.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xgli.module_news.mvp.contract.OtherNewsListContract;
import com.xgli.module_news.mvp.model.OtherNewsListModel;
import com.xgli.module_news.mvp.model.entity.NewInfoBean;
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
@Module
public abstract class OtherNewsListModule {

    @Binds
    abstract OtherNewsListContract.Model bindOtherNewsListModel(OtherNewsListModel model);


    @FragmentScope
    @Provides
    static OtherNewsListAdapter providePhotoListAdapter(List<NewInfoBean> mList){
        return new OtherNewsListAdapter(mList);
    }

    @FragmentScope
    @Provides
    static List<NewInfoBean> provideList() {
        return new ArrayList<>();
    }
}
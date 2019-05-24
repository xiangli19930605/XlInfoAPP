package com.xgli.module_project.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.xgli.module_project.R;
import com.xgli.module_project.mvp.contract.ProjectListContract;
import com.xgli.module_project.mvp.model.ProjectListModel;
import com.xgli.module_project.mvp.model.entity.FeedArticleData;
import com.xgli.module_project.mvp.ui.adapter.ProjectListAdapter;

import java.util.ArrayList;
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
@Module
public abstract class ProjectListModule {

    @Binds
    abstract ProjectListContract.Model bindProjectListModel(ProjectListModel model);
    @FragmentScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(ProjectListContract.View view) {
        return new LinearLayoutManager(view.getActivity());
    }

    @FragmentScope
    @Provides
    static List<FeedArticleData> provideList() {
        return new ArrayList<>();
    }

    @FragmentScope
    @Provides
    static ProjectListAdapter provideSmartRefreshAdapter(List<FeedArticleData> list) {
        return new ProjectListAdapter(R.layout.item_project_list,list);
    }
}
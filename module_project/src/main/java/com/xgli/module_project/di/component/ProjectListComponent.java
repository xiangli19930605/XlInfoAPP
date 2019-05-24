package com.xgli.module_project.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_project.di.module.ProjectListModule;
import com.xgli.module_project.mvp.contract.ProjectListContract;

import com.jess.arms.di.scope.FragmentScope;
import com.xgli.module_project.mvp.ui.fragment.ProjectListFragment;


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
@Component(modules = ProjectListModule.class, dependencies = AppComponent.class)
public interface ProjectListComponent {
    void inject(ProjectListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ProjectListComponent.Builder view(ProjectListContract.View view);

        ProjectListComponent.Builder appComponent(AppComponent appComponent);

        ProjectListComponent build();
    }
}
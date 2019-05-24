package com.xgli.module_news.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_news.di.module.PhotoListModule;
import com.xgli.module_news.mvp.contract.PhotoListContract;

import com.jess.arms.di.scope.FragmentScope;
import com.xgli.module_news.mvp.ui.fragment.PhotoListFragment;


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
@Component(modules = PhotoListModule.class, dependencies = AppComponent.class)
public interface PhotoListComponent {
    void inject(PhotoListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PhotoListComponent.Builder view(PhotoListContract.View view);

        PhotoListComponent.Builder appComponent(AppComponent appComponent);

        PhotoListComponent build();
    }
}
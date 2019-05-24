package com.xgli.module_news.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_news.di.module.OtherNewsListModule;
import com.xgli.module_news.mvp.contract.OtherNewsListContract;

import com.jess.arms.di.scope.FragmentScope;
import com.xgli.module_news.mvp.ui.fragment.OtherNewsListFragment;


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
@Component(modules = OtherNewsListModule.class, dependencies = AppComponent.class)
public interface OtherNewsListComponent {
    void inject(OtherNewsListFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        OtherNewsListComponent.Builder view(OtherNewsListContract.View view);

        OtherNewsListComponent.Builder appComponent(AppComponent appComponent);

        OtherNewsListComponent build();
    }
}
package com.xgli.module_news.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_news.di.module.NewsModule;
import com.xgli.module_news.mvp.contract.NewsContract;

import com.jess.arms.di.scope.FragmentScope;
import com.xgli.module_news.mvp.ui.fragment.NewsFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/26/2019 14:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = NewsModule.class, dependencies = AppComponent.class)
public interface NewsComponent {
    void inject(NewsFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        NewsComponent.Builder view(NewsContract.View view);

        NewsComponent.Builder appComponent(AppComponent appComponent);

        NewsComponent build();
    }
}
package com.xgli.module_news.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_news.di.module.SpecialNewsModule;
import com.xgli.module_news.mvp.contract.SpecialNewsContract;

import com.jess.arms.di.scope.ActivityScope;
import com.xgli.module_news.mvp.ui.activity.SpecialNewsActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/28/2019 15:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = SpecialNewsModule.class, dependencies = AppComponent.class)
public interface SpecialNewsComponent {
    void inject(SpecialNewsActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SpecialNewsComponent.Builder view(SpecialNewsContract.View view);

        SpecialNewsComponent.Builder appComponent(AppComponent appComponent);

        SpecialNewsComponent build();
    }
}
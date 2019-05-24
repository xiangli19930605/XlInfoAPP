package com.xgli.module_main.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.xgli.module_main.di.module.SecondModule;
import com.xgli.module_main.mvp.contract.SecondContract;

import com.jess.arms.di.scope.FragmentScope;
import com.xgli.module_main.mvp.ui.fragment.SecondFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/16/2019 15:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = SecondModule.class, dependencies = AppComponent.class)
public interface SecondComponent {
    void inject(SecondFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        SecondComponent.Builder view(SecondContract.View view);

        SecondComponent.Builder appComponent(AppComponent appComponent);

        SecondComponent build();
    }
}
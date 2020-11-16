package edu.iastate.adamcorp.expensetracker.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;
import edu.iastate.adamcorp.expensetracker.ExpenseTrackerApp;
import edu.iastate.adamcorp.expensetracker.di.modules.ActivityBindingModule;
import edu.iastate.adamcorp.expensetracker.di.modules.ApplicationModule;
import edu.iastate.adamcorp.expensetracker.di.modules.ContextModule;
import edu.iastate.adamcorp.expensetracker.di.modules.ServiceBindingModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        ContextModule.class,
        ActivityBindingModule.class,
        ServiceBindingModule.class,
        ApplicationModule.class
})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {
    void inject(ExpenseTrackerApp application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}

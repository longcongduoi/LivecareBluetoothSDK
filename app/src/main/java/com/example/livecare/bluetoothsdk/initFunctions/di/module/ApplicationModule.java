package com.example.livecare.bluetoothsdk.initFunctions.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private Application mApplication;

    public ApplicationModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    Context provideContext() {
        return mApplication;
    }
}
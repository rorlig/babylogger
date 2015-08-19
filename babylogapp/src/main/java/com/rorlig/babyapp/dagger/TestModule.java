package com.rorlig.babyapp.dagger;

import com.rorlig.babyapp.BabyLoggerApplication;

import dagger.Module;

//import com.rorlig.babylog.retrofit.EventClient;
//import com.rorlig.babylog.retrofit.TestEventClient;

/**
 * @author  gaurav gupta
 * Test Module provides stub client for network service...
 */

@Module(injects={BabyLoggerApplication.class}, library=true, overrides = true)

public class TestModule {

    BabyLoggerApplication application;

    public TestModule(BabyLoggerApplication application) {
        this.application = application;
    }


    /*
     * Provides a test event client for mockup while the server gets ready...
     */

//    @Provides
//    EventClient provideEventClient() {
//        return new TestEventClient();
//    }


}

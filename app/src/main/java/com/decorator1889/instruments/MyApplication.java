package com.decorator1889.instruments;
import android.app.Application;
import android.content.Context;

import com.appodeal.ads.Appodeal;
import com.decorator1889.instruments.LocalDb.RealmDb;
import com.decorator1889.instruments.LocalDb.RealmDbSurgery;
import com.decorator1889.instruments.LocalDb.RealmDbTest;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;


public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        RealmDb.init(this);
        RealmDbTest.init(this);
        RealmDbSurgery.init(this);
        MyApplication.context = getApplicationContext();




        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}

package com.decorator1889.instruments.Network;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.decorator1889.instruments.Models.InstrumentModel;
import com.decorator1889.instruments.Models.SurgeryInstrumentModel;
import com.decorator1889.instruments.Models.TestModel;
import com.decorator1889.instruments.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class OpenAirQuality {
    private static final String HOST_URL = "http://ovz2.a-decorator1889.noklm.vps.myjino.ru/instruments/api/";
    Cache cache =  new Cache(MyApplication.getAppContext().getCacheDir(), 1024*1024*75);
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .cache(cache)
            .addInterceptor(chain -> {
                Request request = chain.request();
                if (!isNetworkAvailable()) {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale \
                    request = request
                            .newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                return chain.proceed(request);
            })
            .build();
    Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();
    public Observable<List<InstrumentModel>> fetchData() {


        Retrofit tmpRetrofit = new Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        OpenAirQualityService service = tmpRetrofit.create(OpenAirQualityService.class);
        return service.fetchData();
    }
    public Observable<List<SurgeryInstrumentModel>> fetchSurgeryData() {


        Retrofit tmpRetrofit = new Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        OpenAirQualityService service = tmpRetrofit.create(OpenAirQualityService.class);
        return service.fetchSurgeryData();
    }
    public Observable<List<TestModel>> fetchTest() {
        Retrofit tmpRetrofit = new Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        OpenAirQualityService service = tmpRetrofit.create(OpenAirQualityService.class);
        return service.fetchTest();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

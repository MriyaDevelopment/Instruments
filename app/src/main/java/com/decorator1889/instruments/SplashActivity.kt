package com.decorator1889.instruments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.decorator1889.instruments.LocalDb.RealmDb
import com.decorator1889.instruments.LocalDb.RealmDbTest
import com.decorator1889.instruments.Network.OpenAirQuality
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class SplashActivity : AppCompatActivity() {

    var open = OpenAirQuality()
    var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setContentView(R.layout.activity_splash)
        loadCompany()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        },1500)

    }

    private fun loadCompany() {

        mCompositeDisposable?.add(
            open.fetchData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ results -> RealmDb.saveResult(results) }, this::initError)
        )

        mCompositeDisposable?.add(
            open.fetchTest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ results -> RealmDbTest.saveResult(results) }, this::initError)
        )
    }
    fun initError(error: Throwable){
        println(error)

    }
    }

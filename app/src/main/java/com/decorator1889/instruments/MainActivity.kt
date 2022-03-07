package com.decorator1889.instruments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.appodeal.ads.Appodeal
import com.decorator1889.instruments.View.CategoryFragment
import com.google.android.gms.ads.*
import com.ogury.consent.manager.ConsentManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val fm: FragmentManager = supportFragmentManager
    private val fragment = CategoryFragment()
    private val adTypes = Appodeal.BANNER or Appodeal.INTERSTITIAL
    private val consentValue = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Appodeal.initialize(this, "3012314c3e0522f77ab2d7aa418829590c2c9aeed8c6f6a4", adTypes, consentValue)
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Appodeal.show(this, Appodeal.BANNER_VIEW)

        fm.beginTransaction().add(R.id.main_container, fragment).commit()
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }
    }
    fun showUpButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    fun hideUpButton() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

}
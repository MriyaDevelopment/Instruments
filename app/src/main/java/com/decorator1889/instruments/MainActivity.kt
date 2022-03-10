package com.decorator1889.instruments

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.decorator1889.instruments.databinding.ActivityMainBinding
import com.decorator1889.instruments.util.gone
import com.decorator1889.instruments.util.visible

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Instruments_Theme)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBnv()
    }

    private fun initBnv() {
        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> findNavController(R.id.nav_host_fragment).navigate(R.id.mainFragment)
                R.id.test -> findNavController(R.id.nav_host_fragment).navigate(R.id.testLevelFragment)
                R.id.favorite -> findNavController(R.id.nav_host_fragment).navigate(R.id.favoriteFragment)
                R.id.profile -> findNavController(R.id.nav_host_fragment)
                else -> navigateByLogin()
            }
            true
        }
    }

    private fun navigateByLogin() {
        TODO("Not yet implemented")
    }

    fun hideBottomNavigationView(){
        binding.run {
            val i = resources.getDimensionPixelSize(R.dimen.margin10)
            layoutBnv.animate().translationY(layoutBnv.height.toFloat() + i)
        }
    }

    fun showBottomNavigationView() {
        binding.run {
            layoutBnv.animate().translationY(0f)
        }
    }
}
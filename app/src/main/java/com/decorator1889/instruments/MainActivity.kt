package com.decorator1889.instruments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.decorator1889.instruments.databinding.ActivityMainBinding

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
                R.id.home -> findNavController(R.id.nav_host_fragment)
                R.id.test -> findNavController(R.id.nav_host_fragment)
                R.id.favorite -> findNavController(R.id.nav_host_fragment)
                R.id.profile -> findNavController(R.id.nav_host_fragment)
                else -> navigateByLogin()
            }
            true
        }
    }

    private fun navigateByLogin() {
        TODO("Not yet implemented")
    }
}
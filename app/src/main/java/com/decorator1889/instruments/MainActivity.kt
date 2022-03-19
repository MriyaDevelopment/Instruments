package com.decorator1889.instruments

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.decorator1889.instruments.databinding.ActivityMainBinding
import com.decorator1889.instruments.fragments.OnBoardingFragmentDirections
import com.decorator1889.instruments.util.createSnackbar
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
        loginInOnce()
    }

    private fun initBnv() {
        binding.bnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> findNavController(R.id.nav_host_fragment).navigate(R.id.mainFragment)
                R.id.test -> findNavController(R.id.nav_host_fragment).navigate(R.id.levelsFragment)
                R.id.favorite -> findNavController(R.id.nav_host_fragment).navigate(R.id.favoriteFragment)
                R.id.profile -> findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
            }
            true
        }
    }

    private fun loginInOnce() {
        if (App.getInstance().userToken?.isEmpty() == true) {
            findNavController(R.id.nav_host_fragment).navigate(R.id.onBoardingFragment)
        } else {
            findNavController(R.id.nav_host_fragment).navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToMainFragment())
        }
    }

    fun hideBottomNavigationView(){
        binding.run {
            val i = resources.getDimensionPixelSize(R.dimen.margin10)
            layoutBnv.animate().translationY(layoutBnv.height.toFloat() + i)
        }
    }

    fun goneBottomNav() {
        binding.layoutBnv.gone()
    }

    fun visibleBottomNav() {
        binding.layoutBnv.visible()
    }

    fun showBottomNavigationView() {
        binding.run {
            layoutBnv.animate().translationY(0f)
        }
    }

    fun userLogOut() {
        App.getInstance().logOut()
        viewModelStore.clear()
        val navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val navGraph = inflater.inflate(R.navigation.navigation_main_graph)
        navController.graph = navGraph
    }

    fun checkBnvMenuItem(itemId: Int) {
        binding.bnv.menu.findItem(itemId).isChecked = true
    }

    fun selectLevels() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.levelsFragment)
    }
}
package com.oguzhancetin.goodpostureapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

import com.oguzhancetin.goodpostureapp.databinding.ActivityMainBinding
import com.oguzhancetin.goodpostureapp.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val _binding get() = binding!!
    private val model: MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GoodPostureApp)
        super.onCreate(savedInstanceState)

        setActivity()
        binding = ActivityMainBinding.inflate(layoutInflater)
        _binding.toolbar.setNavigationIcon(R.drawable.ic_back_arrow_icon)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment
        val mNavController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(mNavController.graph)
        _binding.toolbar
            .setupWithNavController(mNavController, appBarConfiguration)
        setContentView(_binding.root)
    }

    private fun setActivity() {
        model.userIntoInfo.observe(this) {
            when (it) {
                false -> {
                    Intent(this, ScreenSlidePagerActivity::class.java).also { intent ->
                        startActivity(intent)
                        finish()
                    }
                    model.writeUserIntroInfo(true)
                }
                true -> {
                }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}



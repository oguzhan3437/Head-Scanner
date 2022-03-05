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
import com.oguzhancetin.goodpostureapp.fragment.ScreenSlidePagerActivity
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val mNavController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(mNavController.graph)
        _binding.toolbar
            .setupWithNavController(mNavController, appBarConfiguration)
        setContentView(_binding.root)
    }

    private fun setActivity() {
        model.userIntoInfo.observe(this) {
            //TODO: change when condition to it
            when (false) {
                false -> {
                    val intent = Intent(this, ScreenSlidePagerActivity::class.java)
                    startActivity(intent)
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



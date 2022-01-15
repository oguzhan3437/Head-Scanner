package com.oguzhancetin.goodpostureapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.oguzhancetin.goodpostureapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val _binding get() = binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}



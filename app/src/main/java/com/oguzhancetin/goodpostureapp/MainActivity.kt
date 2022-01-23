package com.oguzhancetin.goodpostureapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View

import com.oguzhancetin.goodpostureapp.databinding.ActivityMainBinding
import com.oguzhancetin.goodpostureapp.fragment.ScreenSlidePagerActivity
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

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {


        var intent = Intent(this,ScreenSlidePagerActivity::class.java)
        startActivity(intent)
        return super.onCreateView(name, context, attrs)

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}



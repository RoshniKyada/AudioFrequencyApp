package com.demo.audiofrequencyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.audiofrequencyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding  //defining the binding class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.example.angles.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.angles.R
import com.example.angles.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportFragmentManager.beginTransaction().replace(R.id.zamena, MainFragment()).commit()
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }
}
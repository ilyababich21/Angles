package com.example.angles.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.angles.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.zamena, MainFragment()).commit()
    }
}
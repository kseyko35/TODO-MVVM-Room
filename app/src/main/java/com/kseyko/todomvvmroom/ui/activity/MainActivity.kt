package com.kseyko.todomvvmroom.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kseyko.todomvvmroom.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
package com.billbull.dev.android.kaizen.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.billbull.dev.android.kaizen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 101
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // bind view (activity_main.xml)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // setting for the toolbar
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = "KaiZen App"

        binding.fabAdd.setOnClickListener {
            startActivityForResult(Intent(this, FormAddActivity::class.java), REQUEST_CODE)
        }

        // bind all view into the content
        setContentView(binding.root)
    }
}
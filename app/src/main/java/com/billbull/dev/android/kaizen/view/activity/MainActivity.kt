package com.billbull.dev.android.kaizen.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.billbull.dev.android.kaizen.databinding.ActivityMainBinding
import com.billbull.dev.android.kaizen.helper.invisible
import com.billbull.dev.android.kaizen.helper.visible
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel
import com.billbull.dev.android.kaizen.models.db.init.AppDatabase
import com.billbull.dev.android.kaizen.view.adapter.MainActivityAdapter
import com.billbull.dev.android.kaizen.viewmodels.ActivityViewModel
import com.billbull.dev.android.kaizen.viewmodels.ActivityViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 101
    }

    private lateinit var db: AppDatabase
    private lateinit var viewModel: ActivityViewModel
    private lateinit var viewModelFactory: ActivityViewModelFactory
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityAdapter: MainActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // bind view (activity_main.xml)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // bind view model and data
        db = AppDatabase.getInstance(this)
        viewModelFactory = ActivityViewModelFactory(db)
        viewModel = ViewModelProvider(this, viewModelFactory)[ActivityViewModel::class.java]

        // setting for the toolbar
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = "KaiZen App"

        binding.fabAdd.setOnClickListener {
            startActivityForResult(Intent(this, FormAddActivity::class.java), REQUEST_CODE)
        }

        fetchAllData()

        // bind all view into the content
        setContentView(binding.root)
    }

    private fun fetchAllData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getAllActivity().observe(this@MainActivity, Observer<List<ActivityModel>> {
                mainActivityAdapter = MainActivityAdapter(it) { item ->
                    Log.e("CLICKED : ", item.activity_name)
                }
                binding.rvMain.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = mainActivityAdapter
                }
                binding.progressBar.invisible()
                if (it.isEmpty()) {
                    binding.rvMain.invisible()
                    binding.tvMainText.visible()
                } else {
                    binding.rvMain.visible()
                    binding.tvMainText.invisible()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
           when (resultCode) {
               FormAddActivity.RESULT_CODE -> {
                   Log.e("RESULT DATA", data?.getParcelableExtra<ActivityModel>("data")?.activity_name ?: "")
                   Snackbar.make(binding.rvMain, "SUCCESS ADD DATA", Snackbar.LENGTH_SHORT).show()
               }
           }
        }
    }

}
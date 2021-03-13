package com.billbull.dev.android.kaizen.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
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
import kotlinx.coroutines.*

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
        viewModel.getAllActivity().observe(this@MainActivity, Observer<List<ActivityModel>> {
            mainActivityAdapter = MainActivityAdapter(it, { deleteId ->
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle("Delete ?")
                    setMessage("Are you sure want to delete this ?")
                    setPositiveButton("YES") { _, _ ->
                        CoroutineScope(Dispatchers.Main).launch(CoroutineExceptionHandler { coroutineContext, throwable ->
                            // HERE WHEN ERROR
                        }) {
                            viewModel.deleteActivityById(deleteId).also {
                                Snackbar.make(
                                    binding.fabAdd,
                                    "successfully deleted",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    setNegativeButton("NO") { _, _ -> null }
                    // requestWindowFeature(Window.FEATURE_NO_TITLE)
                    show()
                }
            }) { item ->
                startActivityForResult(
                    Intent(this, FormAddActivity::class.java).apply {
                        putExtra("data", item)
                    }, REQUEST_CODE
                )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            when (resultCode) {
                FormAddActivity.RESULT_CODE -> {
                    Log.e(
                        "RESULT DATA",
                        data?.getParcelableExtra<ActivityModel>("data")?.activity_name ?: ""
                    )
                    Snackbar.make(binding.rvMain, "SUCCESS ADD DATA", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}
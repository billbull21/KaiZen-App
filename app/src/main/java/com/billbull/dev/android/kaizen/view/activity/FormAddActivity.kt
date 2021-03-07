package com.billbull.dev.android.kaizen.view.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.billbull.dev.android.kaizen.databinding.ActivityFormAddBinding
import com.billbull.dev.android.kaizen.helper.FormValidation.isValid
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel
import com.billbull.dev.android.kaizen.models.db.init.AppDatabase
import com.billbull.dev.android.kaizen.viewmodels.ActivityViewModel
import com.billbull.dev.android.kaizen.viewmodels.ActivityViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FormAddActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var viewModel: ActivityViewModel
    private lateinit var viewModelFactory: ActivityViewModelFactory
    private lateinit var binding: ActivityFormAddBinding

    companion object {
        const val RESULT_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // bind the view
        binding = ActivityFormAddBinding.inflate(layoutInflater)

        // bind view model and data
        db = AppDatabase.getInstance(this)
        viewModelFactory = ActivityViewModelFactory(db)
        viewModel = ViewModelProvider(this, viewModelFactory)[ActivityViewModel::class.java]

        // setting for the toolbar
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = "Add Activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set up custom id's
        val etName = (binding.etName.editText as TextInputEditText)
        val etTime = (binding.etTime.editText as TextInputEditText)

        val dateNow = "${
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        }:${Calendar.getInstance().get(Calendar.MINUTE).toString().padStart(2, '0')}"
        etTime.setText(dateNow)

        binding.btnSelectTime.setOnClickListener {
            val mCurrentTime = etTime.text?.split(":")
            val hour: Int = Integer.parseInt(mCurrentTime!![0])
            val minute: Int = Integer.parseInt(mCurrentTime[1])
            val mTimePicker = TimePickerDialog(
                this,
                { timePicker, selectedHour, selectedMinute ->
                    etTime?.setText(
                        "${selectedHour.toString().padStart(2, '0')}:${
                            selectedMinute.toString().padStart(2, '0')
                        }"
                    )
                },
                hour,
                minute,
                true
            ) //Yes 24 hour time

            mTimePicker.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mTimePicker.show()

        }

        binding.btnSave.setOnClickListener {
            val form = arrayOf(binding.etName, binding.etTime)
            if (isValid(form)) {
                val model =
                    ActivityModel(0, etName.text.toString(), etTime.text.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        viewModel.insertActivity(model).also {
                            // set the result
                            setResult(RESULT_CODE, Intent().apply {
                                putExtra("data", model)
                            })
                            finish() // pop to previous activity
                        }
                    } catch (e: Exception) {
                        print("ERROR Save : $e")
                    }
                }
            }
        }

        // some listener here
        etName.doOnTextChanged { text, start, before, count ->
            run {
                if (text != null && text.isNotEmpty()) {
                    binding.etName.error = null
                    binding.etName.isErrorEnabled = false
                }
            }
        }

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
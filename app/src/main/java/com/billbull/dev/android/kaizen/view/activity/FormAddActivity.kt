package com.billbull.dev.android.kaizen.view.activity

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.billbull.dev.android.kaizen.databinding.ActivityFormAddBinding
import com.billbull.dev.android.kaizen.helper.FormValidation.isNotValid
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel
import com.billbull.dev.android.kaizen.viewmodels.ActivityViewModel
import java.util.*

class FormAddActivity : AppCompatActivity() {

    private lateinit var viewModel: ActivityViewModel
    private lateinit var binding: ActivityFormAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // bind the view
        binding = ActivityFormAddBinding.inflate(layoutInflater)

        // bind view model
        viewModel = ActivityViewModel(this)

        // setting for the toolbar
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = "Add Activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.etTime.setText(
            "${
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
            }:${Calendar.getInstance().get(Calendar.MINUTE).toString().padStart(2, '0')}"
        )

        binding.btnSelectTime.setOnClickListener {
            val mCurrentTime = binding.etTime.text.split(":")
            val hour: Int = Integer.parseInt(mCurrentTime[0])
            val minute: Int = Integer.parseInt(mCurrentTime[1])
            val mTimePicker = TimePickerDialog(
                this,
                { timePicker, selectedHour, selectedMinute ->
                    binding.etTime.setText(
                        "${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')}"
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
            if (isNotValid(form)) {
                val model = ActivityModel(0, binding.etName.text.toString(), binding.etTime.text.toString())
                viewModel.insertActivity(model)
            }
        }

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
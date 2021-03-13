package com.billbull.dev.android.kaizen.view.activity

import android.app.TimePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.billbull.dev.android.kaizen.databinding.ActivityFormAddBinding
import com.billbull.dev.android.kaizen.helper.FormValidation.isValid
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel
import com.billbull.dev.android.kaizen.models.db.init.AppDatabase
import com.billbull.dev.android.kaizen.services.MyAlarmManager
import com.billbull.dev.android.kaizen.viewmodels.ActivityViewModel
import com.billbull.dev.android.kaizen.viewmodels.ActivityViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_form_add.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class FormAddActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var viewModel: ActivityViewModel
    private lateinit var viewModelFactory: ActivityViewModelFactory
    private lateinit var binding: ActivityFormAddBinding

    private lateinit var alarmReceiver: MyAlarmManager

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

        alarmReceiver = MyAlarmManager()

        // setting for the toolbar
        setSupportActionBar(binding.includeToolbar.toolbar)
        supportActionBar?.title = "Add Activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set up custom id's
        val etName = (binding.etName.editText as TextInputEditText)
        val etTime = (binding.etTime.editText as TextInputEditText)

        // fill the field with data that we send before
        val dataIntent = intent.getParcelableExtra<ActivityModel>("data")
        if (dataIntent != null) {
            etName.setText(dataIntent.activity_name)
            etTime.setText(dataIntent.activity_time)
            binding.swDailyReminder.isChecked = dataIntent.is_remind_active
        } else {
            val dateNow = "${
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
            }:${Calendar.getInstance().get(Calendar.MINUTE).toString().padStart(2, '0')}"
            etTime.setText(dateNow)
        }

        // select the time
        binding.btnSelectTime.setOnClickListener {
            openTimePicker(etTime)
        }

        // save to database
        binding.btnSave.setOnClickListener {
            val form = arrayOf(binding.etName, binding.etTime)
            if (isValid(form)) {
                AlertDialog.Builder(this).apply {
                    setTitle("Save ?")
                    setMessage("Are you sure want to save this ?")
                    setPositiveButton("YES") { _, _ ->
                        val model = ActivityModel(0, etName.text.toString(), etTime.text.toString(), binding.swDailyReminder.isChecked)
                        CoroutineScope(Dispatchers.Main).launch(CoroutineExceptionHandler { _, error ->
                            when (error) {
                                is SQLiteConstraintException -> {
                                    Snackbar.make(binding.placeSnackBar, "the clock is already in use", Snackbar.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Log.e("ERROR IS : ", error.cause.toString())
                                }
                            }
                        }) {
                            viewModel.insertActivity(model).also {
                                Log.e("ID", it.toString())
                                if (binding.swDailyReminder.isChecked)
                                    alarmReceiver.setRepeatingAlarm(this@FormAddActivity, it.toInt(), etTime.text.toString(), "It's time to do ${etName.text}")
                                else alarmReceiver.cancelAlarm(this@FormAddActivity, it.toInt())
                                // set the result
                                setResult(RESULT_CODE, Intent().apply {
                                    putExtra("data", model)
                                })
                                finish() // pop to previous activity
                            }
                        }
                    }
                    setNegativeButton("NO") { _, _ -> null }
                    // requestWindowFeature(Window.FEATURE_NO_TITLE)
                    show()
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

    private fun openTimePicker(etTime: EditText) {
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

}
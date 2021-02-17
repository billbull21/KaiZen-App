package com.billbull.dev.android.kaizen.helper

import android.util.Patterns
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

object FormValidation {

    fun <T> isNotValid(editText: Array<T>): Boolean where T: EditText {
        val probe = arrayListOf<Int>()
        for (form in editText) {
            if (form.text.trim().isEmpty()) {
                probe.add(editText.indexOf(form))
                form.error = "fields is required"
            }
        }
        if (probe.isEmpty()) return true
        return false
    }

    fun isNotValid(editText: Array<TextInputLayout>): Boolean {
        val probe = arrayListOf<Int>()
        for (form in editText) {
            val et = (form.editText as TextInputEditText).text
            if (et == null || et.trim().isEmpty()) {
                probe.add(editText.indexOf(form))
                form.error = "fields is required"
                form.isErrorEnabled = true
            }
        }
        if (probe.isEmpty()) return true
        return false
    }

    fun isEmailValid(form : String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(form).matches()) return true
        return false
    }

    fun isMatch(text1: String, text2: String): Boolean {
        if (text1 == text2) return false
        return true
    }

}
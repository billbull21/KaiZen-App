package com.billbull.dev.android.kaizen.helper

import android.util.Patterns
import android.widget.EditText

object FormValidation {

    fun isNotValid(editText: Array<EditText>): Boolean {
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

    fun isEmailValid(form : String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(form).matches()) return true
        return false
    }

    fun isMatch(text1: String, text2: String): Boolean {
        if (text1 == text2) return false
        return true
    }

}
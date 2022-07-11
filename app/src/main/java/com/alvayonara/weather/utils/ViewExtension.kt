package com.alvayonara.weather.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

object Field {
    fun observeField(view: EditText): Observable<Boolean> =
        RxTextView.textChanges(view)
            .skipInitialValue()
            .map { field ->
                TextUtils.isEmpty(field)
            }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
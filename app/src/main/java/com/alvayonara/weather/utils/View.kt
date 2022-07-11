package com.alvayonara.weather.utils

import android.text.TextUtils
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

}
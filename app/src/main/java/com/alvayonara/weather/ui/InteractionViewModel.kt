package com.alvayonara.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvayonara.common.utils.Event
import com.alvayonara.core.data.source.local.entity.WeatherEntity

class InteractionViewModel: ViewModel() {

    private val _delete = MutableLiveData<Event<Boolean>>()
    val delete: LiveData<Event<Boolean>> = _delete

    private val _edit = MutableLiveData<Event<WeatherEntity>>()
    val edit: LiveData<Event<WeatherEntity>> = _edit

    private val _add = MutableLiveData<Event<Boolean>>()
    val add: LiveData<Event<Boolean>> = _add

    fun setIsDeleted(delete: Boolean) {
        this._delete.value = Event(delete)
    }

    fun setIsEdited(weatherEntity: WeatherEntity) {
        this._edit.value = Event(weatherEntity)
    }

    fun setIsAdded(add: Boolean) {
        this._add.value = Event(add)
    }
}
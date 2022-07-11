package com.alvayonara.weather.ui.addeditcity

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.alvayonara.common.utils.Event
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.domain.model.Current
import com.alvayonara.core.domain.usecase.WeatherUseCase
import com.alvayonara.navigation.NavigationCommand
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddEditCityViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase) :
    ViewModel() {

    private val _compositeDisposable by lazy { CompositeDisposable() }

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    private val _isValid: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val isValid: MutableLiveData<Event<Boolean>> get() = _isValid

    private val _add = MutableLiveData<Add>()
    val add: LiveData<Add> = _add

    private val _edit = MutableLiveData<Edit>()
    val edit: LiveData<Edit> = _edit

    /**
     * Used to back to previous fragment from [ViewModel]
     */
    fun navigateBack() {
        this._navigation.value = Event(NavigationCommand.Back)
    }

    @SuppressLint("CheckResult")
    fun checkButtonState(observableOtp: List<Observable<Boolean>>) {
        val invalidFieldsStream = Observable.combineLatest(
            observableOtp[0],
            observableOtp[1],
            observableOtp[2]
        ) { isLocationEmpty: Boolean, isLatitudeEmpty: Boolean, isLongitudeEmpty: Boolean ->
            !isLocationEmpty && !isLatitudeEmpty && !isLongitudeEmpty
        }
        invalidFieldsStream.subscribe { isValid ->
            _isValid.postValue(Event(isValid))
        }
    }

    fun add(current: Current) {
        _compositeDisposable.add(weatherUseCase.isLocationExist(current.location)
            .doOnSubscribe { _add.postValue(Add.Loading) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .take(1)
            .subscribe { isLocationExist ->
                if (isLocationExist) {
                    _add.postValue(Add.LocationExist)
                } else {
                    _compositeDisposable.add(
                        weatherUseCase.insertWeather(
                            WeatherEntity(
                                location = current.location,
                                latitude = current.latitude,
                                longitude = current.longitude
                            )
                        ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                _add.postValue(Add.Success)
                            }, {})
                    )
                }
            })
    }

    fun edit(weatherEntity: WeatherEntity) {
        _compositeDisposable.add(weatherUseCase.updateWeather(weatherEntity)
            .doOnSubscribe { _edit.postValue(Edit.Loading) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _edit.postValue(Edit.Success)
            }, {})
        )
    }

    sealed class Add {
        object Loading : Add()
        object Success : Add()
        object LocationExist : Add()
    }

    sealed class Edit {
        object Loading : Edit()
        object Success : Edit()
    }

    override fun onCleared() {
        super.onCleared()
        _compositeDisposable.clear()
    }
}
package com.alvayonara.weather.ui.detailcity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.alvayonara.common.utils.Connectivity
import com.alvayonara.common.utils.Event
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.domain.usecase.WeatherUseCase
import com.alvayonara.navigation.NavigationCommand
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailCityViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val context: Context
) :
    ViewModel() {

    private val _compositeDisposable by lazy { CompositeDisposable() }

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    private val _detail = MutableLiveData<Detail>()
    val detail: LiveData<Detail> = _detail

    private val _delete = MutableLiveData<Delete>()
    val delete: LiveData<Delete> = _delete

    /**
     * Used to handle navigation from [ViewModel]
     */
    fun navigate(directions: NavDirections) {
        this._navigation.value = Event(NavigationCommand.To(directions))
    }

    /**
     * Used to back to previous fragment from [ViewModel]
     */
    fun navigateBack() {
        this._navigation.value = Event(NavigationCommand.Back)
    }

    fun getDetail(weatherEntity: WeatherEntity) {
        val isInternetOn = Connectivity.isInternetOn(context)
            .doOnSubscribe { _detail.postValue(Detail.Loading) }
            .filter { connected -> connected }
            .flatMap {
                weatherUseCase.getWeather(
                    weatherEntity.latitude!!,
                    weatherEntity.longitude!!
                )
            }
            .flatMapSingle { response ->
                weatherUseCase.updateWeather(
                    weatherEntity.copy(
                        currentWeather = response.current?.weather?.firstOrNull()?.main,
                        temperature = response.current?.temp.toString(),
                        humidity = response.current?.humidity.toString(),
                        windSpeed = response.current?.windSpeed.toString(),
                        pressure = response.current?.pressure.toString()
                    )
                )
            }
            .flatMap { weatherUseCase.getWeatherById(weatherEntity.id!!) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _detail.postValue(Detail.Success(it))
            }, { throwable ->
                _detail.postValue(Detail.Failed(throwable))
            })
        _compositeDisposable.add(isInternetOn)

        val isInternetOff = Connectivity.isInternetOn(context)
            .doOnSubscribe { _detail.postValue(Detail.Loading) }
            .filter { connected -> !connected }
            .flatMap { weatherUseCase.getWeatherById(weatherEntity.id!!) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _detail.postValue(Detail.Success(it)) }
        _compositeDisposable.add(isInternetOff)
    }

    fun delete(latitude: String, longitude: String) {
        _compositeDisposable.add(weatherUseCase.deleteWeather(latitude, longitude)
            .doOnSubscribe { _delete.postValue(Delete.Loading) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ _delete.postValue(Delete.Success) }, {})
        )
    }

    sealed class Detail {
        object Loading : Detail()
        data class Success(val data: WeatherEntity) : Detail()
        data class Failed(val data: Throwable) : Detail()
    }

    sealed class Delete {
        object Loading : Delete()
        object Success : Delete()
    }

    override fun onCleared() {
        super.onCleared()
        _compositeDisposable.clear()
    }
}
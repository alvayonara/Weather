package com.alvayonara.weather.ui.listcity

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.alvayonara.common.utils.Connectivity
import com.alvayonara.common.utils.Event
import com.alvayonara.core.data.source.local.entity.WeatherEntity
import com.alvayonara.core.data.source.remote.response.WeatherResponse
import com.alvayonara.core.domain.usecase.WeatherUseCase
import com.alvayonara.navigation.NavigationCommand
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListCityViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val context: Context
) : ViewModel() {

    private val _compositeDisposable by lazy { CompositeDisposable() }

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    private val _list = MutableLiveData<ListCity>()
    val list: LiveData<ListCity> = _list

    init {
        getAllWeather()
    }

    /**
     * Used to handle navigation from [ViewModel]
     */
    fun navigate(directions: NavDirections) {
        this._navigation.value = Event(NavigationCommand.To(directions))
    }

    @SuppressLint("CheckResult")
    fun getAllWeather() {
        val isInternetOnEmpty = Connectivity.isInternetOn(context)
            .doOnSubscribe { _list.postValue(ListCity.Loading) }
            .filter { connected -> connected }
            .flatMap { weatherUseCase.isWeatherExist() }
            .filter { isWeatherExist -> !isWeatherExist }
            .flatMap { weatherUseCase.getWeather("22", "22") }
            .flatMapSingle { response ->
                weatherUseCase.insertWeather(response.mapWeatherResponseToEntity("location"))
            }
            .flatMap { weatherUseCase.getAllWeather() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _list.postValue(ListCity.Success(it))
            }, { throwable ->
                _list.postValue(ListCity.Failed(throwable))
            })
        _compositeDisposable.add(isInternetOnEmpty)

        val isInternetOnNotEmpty = Connectivity.isInternetOn(context)
            .doOnSubscribe { _list.postValue(ListCity.Loading) }
            .filter { connected -> connected }
            .flatMap { weatherUseCase.isWeatherExist() }
            .filter { isWeatherExist -> isWeatherExist }
            .flatMap { weatherUseCase.getAllWeather() }
            .flatMapIterable { listCity -> listCity }
            .take(1)
            .flatMap { weatherEntity ->
                weatherUseCase.getWeather(
                    weatherEntity.latitude!!,
                    weatherEntity.longitude!!
                ).flatMapSingle {
                    weatherUseCase.updateWeather(
                        weatherEntity.copy(
                            currentWeather = it.current?.weather?.firstOrNull()?.main,
                            temperature = it.current?.temp.toString(),
                            humidity = it.current?.humidity.toString(),
                            windSpeed = it.current?.windSpeed.toString(),
                            pressure = it.current?.pressure.toString()
                        )
                    )
                }
            }
            .flatMap { weatherUseCase.getAllWeather() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _list.postValue(ListCity.Success(it))
            }, { throwable ->
                _list.postValue(ListCity.Failed(throwable))
            })
        _compositeDisposable.add(isInternetOnNotEmpty)

        val isInternetOffEmpty = Connectivity.isInternetOn(context)
            .doOnSubscribe { _list.postValue(ListCity.Loading) }
            .filter { connected -> !connected }
            .flatMap { weatherUseCase.isWeatherExist() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _list.postValue(ListCity.Empty) }
        _compositeDisposable.add(isInternetOffEmpty)

        val isInternetOffNotEmpty = Connectivity.isInternetOn(context)
            .doOnSubscribe { _list.postValue(ListCity.Loading) }
            .filter { connected -> !connected }
            .flatMap { weatherUseCase.isWeatherExist() }
            .filter { isWeatherExist -> isWeatherExist }
            .flatMap { weatherUseCase.getAllWeather() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { _list.postValue(ListCity.Success(it)) }
        _compositeDisposable.add(isInternetOffNotEmpty)
    }

    private fun WeatherResponse.mapWeatherResponseToEntity(location: String): WeatherEntity =
        WeatherEntity(
            location = location,
            latitude = this.lat.toString(),
            longitude = this.lon.toString(),
            currentWeather = this.current?.weather?.firstOrNull()?.main,
            temperature = this.current?.temp.toString(),
            humidity = this.current?.humidity.toString(),
            windSpeed = this.current?.windSpeed.toString(),
            pressure = this.current?.pressure.toString()
        )

    sealed class ListCity {
        object Loading : ListCity()
        object Empty : ListCity()
        data class Success(val data: List<WeatherEntity>) : ListCity()
        data class Failed(val data: Throwable) : ListCity()
    }

    override fun onCleared() {
        super.onCleared()
        _compositeDisposable.clear()
    }
}
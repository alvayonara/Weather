package com.alvayonara.weather.di

import com.alvayonara.weather.ui.listcity.ListCityAdapter
import dagger.Module
import dagger.Provides

@Module
class ListCityModule {

    @Provides
    fun provideListCityAdapter() = ListCityAdapter()
}
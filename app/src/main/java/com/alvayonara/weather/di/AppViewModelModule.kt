package com.alvayonara.weather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvayonara.weather.ui.ViewModelFactory
import com.alvayonara.weather.ui.addeditcity.AddEditCityViewModel
import com.alvayonara.weather.ui.detailcity.DetailCityViewModel
import com.alvayonara.weather.ui.listcity.ListCityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppViewModelModule {

    @Binds
    @IntoMap
    @AppViewModelKey(AddEditCityViewModel::class)
    abstract fun bindAddEditViewModel(addEditCityViewModel: AddEditCityViewModel): ViewModel

    @Binds
    @IntoMap
    @AppViewModelKey(DetailCityViewModel::class)
    abstract fun bindDetailCityViewModel(detailCityViewModel: DetailCityViewModel): ViewModel

    @Binds
    @IntoMap
    @AppViewModelKey(ListCityViewModel::class)
    abstract fun bindListCityViewModel(listCityViewModel: ListCityViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
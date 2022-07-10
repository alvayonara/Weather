package com.alvayonara.weather.di

import com.alvayonara.core.di.CoreComponent
import com.alvayonara.weather.ui.addeditcity.AddEditCityFragment
import com.alvayonara.weather.ui.detailcity.DetailCityFragment
import com.alvayonara.weather.ui.listcity.ListCityFragment
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class, AppViewModelModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(fragment: AddEditCityFragment)
    fun inject(fragment: DetailCityFragment)
    fun inject(fragment: ListCityFragment)
}
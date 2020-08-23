package de.parndt.calendar

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.HasAndroidInjector
import de.parndt.calendar.di.ApplicationComponent
import de.parndt.calendar.di.DaggerApplicationComponent
import javax.inject.Inject

class CalendarApplication:DaggerApplication(),HasAndroidInjector {

    lateinit var applicationComponent:ApplicationComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        applicationComponent = DaggerApplicationComponent.builder().networkModule("https://postit.allthing.eu").context(this).build()
        return applicationComponent
    }

}
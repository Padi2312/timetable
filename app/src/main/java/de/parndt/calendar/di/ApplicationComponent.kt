package de.parndt.calendar.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import de.parndt.calendar.CalendarApplication
import de.parndt.calendar.ui.StartFragment
import javax.inject.Singleton

@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    PlatformTypesInjectorModule::class,
    MainActivityModule::class
])
@Singleton
interface ApplicationComponent:AndroidInjector<CalendarApplication> {

    override fun inject(calendarApplication: CalendarApplication)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun context(context:Context):Builder
        fun build():ApplicationComponent
    }
}
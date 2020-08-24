package de.parndt.calendar.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.calendar.ui.MainActivity
import de.parndt.calendar.general.start.StartFragment

@Module
@Suppress("unused")
abstract class PlatformTypesInjectorModule {
    @ContributesAndroidInjector(modules = [StartFragmentModule::class])
    abstract fun contributeStartFragment(): StartFragment

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
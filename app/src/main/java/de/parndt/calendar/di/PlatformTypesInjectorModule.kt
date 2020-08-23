package de.parndt.calendar.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.calendar.MainActivity
import de.parndt.calendar.ui.StartFragment

@Module
abstract class PlatformTypesInjectorModule {
    @ContributesAndroidInjector(modules = [StartFragmentModule::class])
    abstract fun contributeStartFragment():StartFragment

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity
}
package de.parndt.timetable.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.timetable.general.lectures.LecturesFragment
import de.parndt.timetable.general.settings.SettingsFragment
import de.parndt.timetable.general.tabs.daily.di.LecturesFragmentModule
import de.parndt.timetable.general.tabs.daily.di.SettingsFragmentModule
import de.parndt.timetable.ui.MainActivity


@Module
@Suppress("unused")
abstract class PlatformTypesInjectorModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [LecturesFragmentModule::class])
    abstract fun contributeLecturesFragment(): LecturesFragment

    @ContributesAndroidInjector(modules = [SettingsFragmentModule::class])
    abstract fun contributeSettingsFragment(): SettingsFragment
}


package de.parndt.timetable.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.timetable.alarmclock.AlarmClockFragment
import de.parndt.timetable.general.tabs.daily.di.AlarmClockFragmentModule
import de.parndt.timetable.lectures.LecturesFragment
import de.parndt.timetable.settings.SettingsFragment
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

    @ContributesAndroidInjector(modules = [AlarmClockFragmentModule::class])
    abstract fun contributeAlarmClockFragment(): AlarmClockFragment
}


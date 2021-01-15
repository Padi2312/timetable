package de.parndt.timetable.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.timetable.general.TabsFragment
import de.parndt.timetable.general.tabs.daily.DailyFragment
import de.parndt.timetable.general.tabs.daily.di.DailyFragmentModule
import de.parndt.timetable.general.tabs.weekly.WeeklyFragment
import de.parndt.timetable.ui.MainActivity


@Module
@Suppress("unused")
abstract class PlatformTypesInjectorModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeTabsFragment(): TabsFragment

    @ContributesAndroidInjector
    abstract fun contributeWeeklyFragment(): WeeklyFragment

    @ContributesAndroidInjector(modules = [DailyFragmentModule::class])
    abstract fun contributeDailyFragment(): DailyFragment

}


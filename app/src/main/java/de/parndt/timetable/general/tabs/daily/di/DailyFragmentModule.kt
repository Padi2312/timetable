package de.parndt.timetable.general.tabs.daily.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import de.parndt.timetable.general.tabs.daily.DailyFragment
import de.parndt.timetable.general.tabs.daily.DailyFragmentViewModel


@Module
class DailyFragmentModule {
    @Provides
    fun dailyFragmentModuleViewModel(
        target: DailyFragment,
        factory: DailyFragmentViewModel.Factory
    ): DailyFragmentViewModel =
        ViewModelProvider(target, factory)[DailyFragmentViewModel::class.java]
}
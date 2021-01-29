package de.parndt.timetable.general.tabs.daily.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import de.parndt.timetable.alarmclock.AlarmClockFragment
import de.parndt.timetable.alarmclock.AlarmClockViewModel

@Module
class AlarmClockFragmentModule {
    @Provides
    fun AlarmClockFragmentModuleViewModel(
            target: AlarmClockFragment,
            factory: AlarmClockViewModel.Factory
    ): AlarmClockViewModel =
        ViewModelProvider(target, factory)[AlarmClockViewModel::class.java]
}
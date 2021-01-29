package de.parndt.timetable.general.tabs.daily.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import de.parndt.timetable.settings.SettingsFragment
import de.parndt.timetable.settings.SettingsViewModel


@Module
class SettingsFragmentModule {
    @Provides
    fun settingsFragmentModuleViewModel(
            target: SettingsFragment,
            factory: SettingsViewModel.Factory
    ): SettingsViewModel =
        ViewModelProvider(target, factory)[SettingsViewModel::class.java]
}
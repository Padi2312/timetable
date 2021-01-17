package de.parndt.timetable.general.tabs.daily.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import de.parndt.timetable.general.lectures.LecturesFragment
import de.parndt.timetable.general.lectures.LecturesViewModel


@Module
class LecturesFragmentModule {
    @Provides
    fun lecturesFragmentModuleViewModel(
        target: LecturesFragment,
        factory: LecturesViewModel.Factory
    ): LecturesViewModel =
        ViewModelProvider(target, factory)[LecturesViewModel::class.java]
}
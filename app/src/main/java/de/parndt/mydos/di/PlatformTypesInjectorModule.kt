package de.parndt.mydos.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.mydos.general.TabsFragment
import de.parndt.mydos.general.tabs.home.HomeFragment
import de.parndt.mydos.general.tabs.notes.NotesFragment
import de.parndt.mydos.general.tabs.settings.SettingsFragment
import de.parndt.mydos.general.tabs.todos.TodosFragment
import de.parndt.mydos.ui.MainActivity
import de.parndt.mydos.ui.customcomponent.datetimeselection.DateTimeSelectionFragment
import de.parndt.mydos.ui.customcomponent.newtododialog.NewTodoDialogFragment

@Module
@Suppress("unused")
abstract class PlatformTypesInjectorModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeTabsFragment(): TabsFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeTodosFragment(): TodosFragment

    @ContributesAndroidInjector
    abstract fun contributeFriendsFragment(): NotesFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeDialogNewTodo(): NewTodoDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeDateTimeSelectionFragment(): DateTimeSelectionFragment
}


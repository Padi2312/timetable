package de.parndt.mydos.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.mydos.ui.MainActivity
import de.parndt.mydos.ui.customcomponent.datetimeselection.DateTimeSelectionFragment
import de.parndt.mydos.ui.customcomponent.newtododialog.NewTodoDialogFragment
import de.parndt.mydos.views.TabsFragment
import de.parndt.mydos.views.tabs.home.HomeFragment
import de.parndt.mydos.views.tabs.notes.FriendsFragment
import de.parndt.mydos.views.tabs.settings.SettingsFragment
import de.parndt.mydos.views.tabs.todos.TodosFragment

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
    abstract fun contributeFriendsFragment(): FriendsFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeDialogNewTodo(): NewTodoDialogFragment

    @ContributesAndroidInjector
    abstract fun contributeDateTimeSelectionFragment(): DateTimeSelectionFragment
}


package de.parndt.schupnet.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.parndt.schupnet.HomeFragment
import de.parndt.schupnet.ui.MainActivity
import de.parndt.schupnet.general.start.StartFragment

@Module
@Suppress("unused")
abstract class PlatformTypesInjectorModule {
    @ContributesAndroidInjector(modules = [StartFragmentModule::class])
    abstract fun contributeStartFragment(): StartFragment

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
}


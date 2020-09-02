package de.parndt.schupnet

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.HasAndroidInjector
import de.parndt.schupnet.di.ApplicationComponent
import de.parndt.schupnet.di.DaggerApplicationComponent

class SchupnetApplication:DaggerApplication(),HasAndroidInjector {

    lateinit var applicationComponent:ApplicationComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        applicationComponent = DaggerApplicationComponent.builder().networkModule("https://postit.allthing.eu").context(this).build()
        return applicationComponent
    }

}
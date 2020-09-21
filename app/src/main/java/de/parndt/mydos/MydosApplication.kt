package de.parndt.mydos

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.HasAndroidInjector
import de.parndt.mydos.database.di.RoomDatabaseModule
import de.parndt.mydos.di.ApplicationComponent
import de.parndt.mydos.di.DaggerApplicationComponent

class MydosApplication : DaggerApplication(), HasAndroidInjector {

    lateinit var applicationComponent: ApplicationComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        applicationComponent =
            DaggerApplicationComponent.builder().roomDatabaseModule(RoomDatabaseModule(this))
                .networkModule("https://mydos.allthing.eu").context(this).build()
        return applicationComponent
    }

}
package de.parndt.mydos.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import de.parndt.mydos.MydosApplication
import de.parndt.mydos.database.di.RepositoryModule
import de.parndt.mydos.database.di.RoomDatabaseModule
import javax.inject.Named
import javax.inject.Singleton

@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    PlatformTypesInjectorModule::class,
    MainActivityModule::class,
    RoomDatabaseModule::class,
    RepositoryModule::class,
    NetworkModule::class
])
@Singleton
interface ApplicationComponent:AndroidInjector<MydosApplication> {

    override fun inject(mydosApplication: MydosApplication)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun context(context:Context):Builder

        fun roomDatabaseModule(roomDatabaseModule: RoomDatabaseModule):Builder

        @BindsInstance
        fun networkModule(@Named("baseUrl")baseUrl:String):Builder

        fun build():ApplicationComponent
    }
}
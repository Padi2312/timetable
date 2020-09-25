package de.parndt.mydos.database.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import de.parndt.mydos.MydosApplication
import de.parndt.mydos.database.MydosDatabase
import javax.inject.Singleton


@Module
class RoomDatabaseModule(application: MydosApplication) {

    private var app = application
    private lateinit var mydosDatabase: MydosDatabase

    @Singleton
    @Provides
    fun provideDatabase(): MydosDatabase {
        mydosDatabase = Room.databaseBuilder(app, MydosDatabase::class.java, "mydos")
            .fallbackToDestructiveMigration().build()
        return mydosDatabase
    }


}
package de.parndt.mydos.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.database.models.todo.TodoDao
import javax.inject.Named
import javax.inject.Singleton

@Module
class ContextModule(private var context: Context) {

    @Provides
    fun provideContext(): Context {
        return this.context
    }


}
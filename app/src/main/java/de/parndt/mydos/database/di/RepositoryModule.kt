package de.parndt.mydos.database.di

import dagger.Module
import dagger.Provides
import de.parndt.mydos.database.MydosDatabase
import de.parndt.mydos.repository.TodoRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideTodoRepository(database: MydosDatabase): TodoRepository {
        return TodoRepository(database)
    }

}
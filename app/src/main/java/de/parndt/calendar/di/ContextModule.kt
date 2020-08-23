package de.parndt.calendar.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule {
    private lateinit var context: Context

    constructor(context: Context){
        this.context = context
    }

    @Provides
    fun provideContext():Context{
        return this.context
    }
}
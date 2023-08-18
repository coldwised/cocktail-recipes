package com.cocktailbar.di

import android.app.Application
import androidx.room.Room
import com.cocktailbar.data.local.CocktailDao
import com.cocktailbar.data.local.CocktailDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): CocktailDataBase {
        return Room.databaseBuilder(
            app, CocktailDataBase::class.java, CocktailDataBase.name
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideImageUrlDao(
        db: CocktailDataBase,
    ): CocktailDao {
        return db.fileDao
    }
}
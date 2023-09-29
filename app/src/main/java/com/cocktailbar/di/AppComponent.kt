package com.cocktailbar.di

import android.content.ContentResolver
import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.cocktailbar.CocktailDatabase
import com.cocktailbar.data.local.CocktailDataSource
import com.cocktailbar.data.local.CocktailDataSourceImpl
import com.cocktailbar.data.local.FileProviderImpl
import com.cocktailbar.data.repository.CocktailRepositoryImpl
import com.cocktailbar.domain.repository.CocktailRepository
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@Singleton
abstract class AppComponent(private val appContext: Context) {
    @Singleton
    @Provides
    fun CocktailRepositoryImpl.bind(): CocktailRepository = this

    @Singleton
    @Provides
    fun provideCocktailDataSource(contentResolver: ContentResolver): CocktailDataSource {
        return CocktailDataSourceImpl(
            db = CocktailDatabase(AndroidSqliteDriver(
                schema = CocktailDatabase.Schema,
                context = appContext,
                name = CocktailDataSource.NAME
            )),
            fileProvider = FileProviderImpl(appContext),
            contentResolver = contentResolver
        )
    }

    @Provides
    @Singleton
    fun provideContentResolver(): ContentResolver = appContext.contentResolver
}
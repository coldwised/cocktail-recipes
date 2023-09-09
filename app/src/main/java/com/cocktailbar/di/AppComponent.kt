package com.cocktailbar.di

import app.cash.sqldelight.db.SqlDriver
import com.cocktailbar.CocktailDatabase
import com.cocktailbar.data.local.CocktailDataSource
import com.cocktailbar.data.local.CocktailDataSourceImpl
import com.cocktailbar.data.repository.CocktailRepositoryImpl
import com.cocktailbar.domain.repository.CocktailRepository
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@Singleton
abstract class AppComponent(private val sqlDriver: SqlDriver) {
    @Singleton
    @Provides
    fun CocktailRepositoryImpl.bind(): CocktailRepository = this

    @Singleton
    @Provides
    fun provideCocktailDataSource(): CocktailDataSource {
        return CocktailDataSourceImpl(CocktailDatabase(sqlDriver))
    }
}
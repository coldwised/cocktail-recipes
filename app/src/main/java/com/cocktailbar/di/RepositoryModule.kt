package com.cocktailbar.di

import com.cocktailbar.data.repository.CocktailRepositoryImpl
import com.cocktailbar.domain.repository.CocktailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        repositoryImpl: CocktailRepositoryImpl
    ): CocktailRepository
}
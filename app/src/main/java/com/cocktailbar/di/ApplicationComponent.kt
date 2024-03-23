package com.cocktailbar.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.cocktailbar.data.local.AppDatabase
import com.cocktailbar.data.local.CocktailDataSource
import com.cocktailbar.data.local.CocktailDataSourceImpl
import com.cocktailbar.data.local.FileProviderImpl
import com.cocktailbar.data.repository.CocktailRepositoryImpl
import com.cocktailbar.domain.repository.CocktailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

typealias IoDispatcher = CoroutineDispatcher
typealias MainDispatcher = CoroutineDispatcher
typealias MainImmediateDispatcher = CoroutineDispatcher
typealias DefaultDispatcher = CoroutineDispatcher
typealias AppCoroutineScope = CoroutineScope

typealias ApplicationContext = Context

@Component
@Singleton
abstract class ApplicationComponent(private val applicationContext: Context) {
    companion object {
        private var applicationComponent: ApplicationComponent? = null

        fun getInstance(context: Context): ApplicationComponent {
            return applicationComponent
                ?: ApplicationComponent::class.create(context.applicationContext)
        }
    }

    @Singleton
    @Provides
    fun CocktailRepositoryImpl.bind(): CocktailRepository = this

    @Singleton
    @Provides
    fun provideApplicationContext(): ApplicationContext = applicationContext

    @Singleton
    @Provides
    fun provideDefaultDispatcher(): DefaultDispatcher = Dispatchers.Default

    @Singleton
    @Provides
    fun provideIoDispatcher(): IoDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideMainDispatcher(): MainDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun provideMainImmediateDispatcher(): MainImmediateDispatcher = Dispatchers.Main.immediate

    @Singleton
    @Provides
    fun provideApplicationCoroutineScope(defaultDispatcher: DefaultDispatcher): AppCoroutineScope {
        return CoroutineScope(SupervisorJob() + defaultDispatcher)
    }

    @Singleton
    @Provides
    fun provideCocktailDataSource(
        contentResolver: ContentResolver,
        ioDispatcher: IoDispatcher
    ): CocktailDataSource {
        return CocktailDataSourceImpl(
            cocktailDao = Room.databaseBuilder(
                context = applicationContext,
                AppDatabase::class.java,
                AppDatabase.NAME,
            )
                .createFromAsset("database/${AppDatabase.NAME}")
                .build()
                .cocktailDao,
            fileProvider = FileProviderImpl(applicationContext),
            contentResolver = contentResolver,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideContentResolver(): ContentResolver = applicationContext.contentResolver
}
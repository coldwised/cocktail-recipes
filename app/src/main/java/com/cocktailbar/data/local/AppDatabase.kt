package com.cocktailbar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cocktailbar.data.local.entity.CocktailEntity

@Database(
    entities =[CocktailEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract val cocktailDao: CocktailDao

    companion object {
        const val NAME = "app.db"
    }
}
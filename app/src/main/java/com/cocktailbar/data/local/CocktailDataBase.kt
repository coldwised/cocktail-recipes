package com.cocktailbar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CocktailEntity::class], version = 1)
abstract class CocktailDataBase : RoomDatabase() {
    abstract val fileDao: CocktailDao

    companion object {
        const val name = "local_db"
    }
}
package com.cocktailbar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CocktailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCocktail(cocktail: CocktailEntity)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getCocktails(): List<CocktailEntity>

    companion object {
        const val TABLE_NAME = "cocktail_db"
    }
}
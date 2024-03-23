package com.cocktailbar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cocktailbar.data.local.entity.CocktailEntity

@Dao
interface CocktailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCocktail(cocktail: CocktailEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :id")
    suspend fun deleteCocktail(id: Long)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getCocktails(): List<CocktailEntity>

    companion object {
        const val TABLE_NAME = "cocktail"
    }
}
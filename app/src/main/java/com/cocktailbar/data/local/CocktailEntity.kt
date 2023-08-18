package com.cocktailbar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CocktailDao.TABLE_NAME)
data class CocktailEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val recipe: String,
    val ingredients: String
)
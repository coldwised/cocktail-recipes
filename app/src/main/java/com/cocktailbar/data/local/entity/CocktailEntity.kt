package com.cocktailbar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cocktailbar.data.local.CocktailDao.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class CocktailEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val name: String,
    val description: String?,
    val recipe: String?,
    val ingredients: String?,
    val image: String?,
)

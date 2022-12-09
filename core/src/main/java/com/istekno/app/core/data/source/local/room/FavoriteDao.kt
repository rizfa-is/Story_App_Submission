package com.istekno.app.core.data.source.local.room

import androidx.room.*
import com.istekno.app.core.data.source.local.entity.Favorite

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorite: Favorite)

    @Delete
    fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite_table")
    fun getAllFavorite(favorite: Favorite): List<Favorite>
}
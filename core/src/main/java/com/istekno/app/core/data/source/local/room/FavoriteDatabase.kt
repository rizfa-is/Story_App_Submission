package com.istekno.app.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.istekno.app.core.data.source.local.entity.Favorite

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Favorite::class
    ]
)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteDao() : FavoriteDao
}
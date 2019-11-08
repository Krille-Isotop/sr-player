package com.example.playah

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ListItem::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ListItemDao

    companion object {
        private val instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return if (instance != null) instance else {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "list-item-database"
                ).build()
            }
        }
    }
}
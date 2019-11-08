package com.example.playah

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

open class BaseActivity : AppCompatActivity() {
    val db by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "list-item-database")
    }
}
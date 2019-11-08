package com.example.playah

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ListItemDao {
    @Insert
    fun insert(listItem: ListItem)

    @Query("DELETE FROM listitem WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM listitem WHERE id = :id")
    fun getById(id: Int): ListItem

    @Query("SELECT * FROM listitem")
    fun getAll(): List<ListItem>
}

package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDAO_UserDetails {
    @Query("SELECT * FROM user")
    fun getAll(): List<RoomEntity_UserDetails>

    @Query("SELECT * FROM user WHERE uId IN (:userIds)")
    fun findById(userIds: IntArray): List<RoomEntity_UserDetails>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): RoomEntity_UserDetails

    @Insert
    fun insertAll(users: RoomEntity_UserDetails)

    @Delete
    fun delete(user: RoomEntity_UserDetails)

    @Update
    fun update(user: RoomEntity_UserDetails)
}
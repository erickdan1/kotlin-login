package com.example.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [RoomEntity_UserDetails::class], version = 1)
abstract class RoomDB_UserDetails: RoomDatabase() {
    abstract fun userDao(): RoomDAO_UserDetails
}
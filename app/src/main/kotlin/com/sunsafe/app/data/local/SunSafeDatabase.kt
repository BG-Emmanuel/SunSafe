package com.sunsafe.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sunsafe.app.data.local.dao.*
import com.sunsafe.app.data.local.entity.*

@Database(
    entities = [UserEntity::class, ExposureEntity::class, SkinPhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SunSafeDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun exposureDao(): ExposureDao
    abstract fun skinPhotoDao(): SkinPhotoDao

    companion object {
        const val DATABASE_NAME = "sunsafe.db"
    }
}

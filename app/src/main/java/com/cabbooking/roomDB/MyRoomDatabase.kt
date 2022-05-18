package com.cabbooking.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cabbooking.models.CommonResponse

@Database(entities = [CommonResponse::class], version = 1)
abstract class MyRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "Cab Booking"

        @Volatile
        private var noteRoomInstance: MyRoomDatabase? = null

        internal fun getDatabase(
            context: Context
        ): MyRoomDatabase {
            if (noteRoomInstance == null) {
                synchronized(this) {
                    if (noteRoomInstance == null) {
                        noteRoomInstance = Room.databaseBuilder(
                            context.applicationContext,
                            MyRoomDatabase::class.java, DATABASE_NAME
                        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                    }
                }
            }
            return noteRoomInstance!!
        }

    }

}
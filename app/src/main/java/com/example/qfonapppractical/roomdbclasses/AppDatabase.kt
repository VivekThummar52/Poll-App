package com.example.qfonapppractical.roomdbclasses

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.qfonapppractical.data.Option

@Database(entities = [Poll::class, Option::class], version = 1, exportSchema = false)
@TypeConverters(OptionListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pollDao(): PollDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

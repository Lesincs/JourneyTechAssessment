package com.lesincs.journeytechassessment.common.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lesincs.journeytechassessment.comments.data.persistance.CommentDao
import com.lesincs.journeytechassessment.comments.data.persistance.CommentEntity
import com.lesincs.journeytechassessment.posts.data.persistance.PostDao
import com.lesincs.journeytechassessment.posts.data.persistance.PostEntity

@Database(entities = [PostEntity::class, CommentEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = "app_database",
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
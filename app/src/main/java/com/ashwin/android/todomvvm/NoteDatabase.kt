package com.ashwin.android.todomvvm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): NoteDatabase {
            return Room.databaseBuilder(context, NoteDatabase::class.java, "note_database")
                .addCallback(roomDatabaseCallback)
                .fallbackToDestructiveMigration()
                .build()
        }

        private val roomDatabaseCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val noteDao = instance?.noteDao()
                    noteDao?.insert(Note("Title 1", "Description 1", 1))
                    noteDao?.insert(Note("Title 2", "Description 2", 1))
                    noteDao?.insert(Note("Title 3", "Description 3", 2))
                    noteDao?.insert(Note("Title 4", "Description 4", 3))
                }
            }
        }
    }
}

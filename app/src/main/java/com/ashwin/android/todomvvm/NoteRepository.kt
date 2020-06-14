package com.ashwin.android.todomvvm

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteRepository(application: Application) {
    private val noteDao: NoteDao
    private val allNotes: LiveData<List<Note>>
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        val noteDatabase = NoteDatabase.getInstance(application)
        noteDao = noteDatabase.noteDao()
        allNotes = noteDao.getAll()
    }

    fun getAll(): LiveData<List<Note>> {
        return allNotes
    }

    fun insert(note: Note) {
        coroutineScope.launch {
            noteDao.insert(note)
        }
    }

    fun update(note: Note) {
        coroutineScope.launch {
            noteDao.update(note)
        }
    }

    fun delete(note: Note) {
        coroutineScope.launch {
            noteDao.delete(note)
        }
    }

    fun deleteAll() {
        coroutineScope.launch {
            noteDao.deleteAll()
        }
    }
}

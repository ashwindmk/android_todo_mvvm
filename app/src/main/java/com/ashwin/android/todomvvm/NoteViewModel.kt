package com.ashwin.android.todomvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteRepository: NoteRepository = NoteRepository(application)
    private val notes: LiveData<List<Note>> = noteRepository.getAll()

    fun getAll(): LiveData<List<Note>> {
        return notes
    }

    fun insert(note: Note) {
        noteRepository.insert(note)
    }

    fun update(note: Note) {
        noteRepository.update(note)
    }

    fun delete(note: Note) {
        noteRepository.delete(note)
    }

    fun deleteAll() {
        noteRepository.deleteAll()
    }
}
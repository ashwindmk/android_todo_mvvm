package com.ashwin.android.todomvvm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notes_recyclerview.layoutManager = LinearLayoutManager(this)
        notes_recyclerview.setHasFixedSize(true)

        val noteAdapter = NoteAdapter()
        notes_recyclerview.adapter = noteAdapter

        noteViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(NoteViewModel::class.java)

        noteViewModel.getAll().observe(this, Observer<List<Note>> {
            Log.d(DEBUG_TAG, "Notes list: $it")
            noteAdapter.submitList(it)
        })

        addnote_button.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            this@MainActivity.startActivityForResult(intent, AddEditNoteActivity.REQUEST_CODE_ADD_NOTE)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = noteAdapter.getNoteAt(position)
                noteViewModel.delete(note)
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_LONG).show()
            }
        }).attachToRecyclerView(notes_recyclerview)

        noteAdapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClicked(note: Note) {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                val extras = bundleOf(
                    Pair(AddEditNoteActivity.EXTRA_ID, note.id),
                    Pair(AddEditNoteActivity.EXTRA_TITLE, note.title),
                    Pair(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description),
                    Pair(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)
                )
                intent.putExtras(extras)
                startActivityIfNeeded(intent, AddEditNoteActivity.REQUEST_CODE_EDIT_NOTE)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAll()
                Toast.makeText(this@MainActivity, "All notes deleted", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddEditNoteActivity.REQUEST_CODE_ADD_NOTE && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description =  data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 5)
            if (title != null && description != null && priority != null) {
                val note = Note(title, description, priority)
                noteViewModel.insert(note)
                Toast.makeText(this, "Note saved", Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == AddEditNoteActivity.REQUEST_CODE_EDIT_NOTE && resultCode == Activity.RESULT_OK) {
            val id: Int = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1) ?: -1
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_LONG).show()
                return
            }
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description =  data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 5)
            if (title != null && description != null && priority != null) {
                val note = Note(title, description, priority)
                note.id = id
                noteViewModel.update(note)
                Toast.makeText(this, "Note updated", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_LONG).show()
        }
    }
}

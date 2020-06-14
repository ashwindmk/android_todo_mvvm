package com.ashwin.android.todomvvm

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.activity_add_edit_note.*

class AddEditNoteActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_ADD_NOTE = 1024
        const val REQUEST_CODE_EDIT_NOTE = 1025

        const val EXTRA_ID = "com.ashwin.android.todomvvm.EXTRA_ID"
        const val EXTRA_TITLE = "com.ashwin.android.todomvvm.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.ashwin.android.todomvvm.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "com.ashwin.android.todomvvm.EXTRA_PRIORITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        priority_numberpicker.minValue = 1
        priority_numberpicker.maxValue = 5

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"

            title_edittext.setText(intent.getStringExtra(EXTRA_TITLE))
            description_edittext.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            priority_numberpicker.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            title = "Add Note"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.addnote_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val id = intent.getIntExtra(EXTRA_ID, -1)
        val title = title_edittext.text.toString()
        val description = description_edittext.text.toString()
        val priority = priority_numberpicker.value

        if (title.isBlank() || description.isBlank()) {
            Toast.makeText(this, "Invalid title or description", Toast.LENGTH_LONG).show()
            return
        }

        val extras = bundleOf(
                Pair(EXTRA_TITLE, title),
                Pair(EXTRA_DESCRIPTION, description),
                Pair(EXTRA_PRIORITY, priority)
        )
        if (id != -1) {
            extras.putInt(EXTRA_ID, id)
        }

        val data = Intent()
        data.putExtras(extras)
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}

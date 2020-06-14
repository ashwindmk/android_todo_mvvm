package com.ashwin.android.todomvvm

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteViewHolder>(DIFF_UTIL) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return (oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.priority == newItem.priority)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(note: Note)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById<TextView>(R.id.title_textview)
        val descriptionTextView: TextView = itemView.findViewById<TextView>(R.id.description_textview)
        val priorityTextView: TextView = itemView.findViewById<TextView>(R.id.priority_textview)
        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClicked(getItem(adapterPosition))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        Log.d(DEBUG_TAG, "NoteAdapter: onCreateViewHolder(parent, $viewType)")
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        Log.d(DEBUG_TAG, "NoteAdapter: onBindViewHolder(NoteViewHolder, $position)")
        val note = getItem(position)
        holder.titleTextView.text = note.title
        holder.descriptionTextView.text = note.description
        holder.priorityTextView.text = note.priority.toString()
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}

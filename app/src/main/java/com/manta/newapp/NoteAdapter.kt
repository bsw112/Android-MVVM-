package com.manta.newapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*
import org.w3c.dom.Node

class NoteAdapter(var mDataset : List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    class NoteViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val mDescription: TextView = view.text_view_description;
        val mTitle: TextView = view.text_view_title;
        val mViewPrioirty : TextView = view.text_view_priority;

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false);
        return NoteViewHolder(view);
    }

    override fun getItemCount(): Int {
        return mDataset.size;
    }

    fun setDataset(dataset  : List<Note>){
        mDataset = dataset;
        notifyDataSetChanged();
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = mDataset[position]
        holder.mTitle.text = note.title;
        holder.mDescription.text = note.description;
        holder.mViewPrioirty.text = note.priority.toString();
    }


}
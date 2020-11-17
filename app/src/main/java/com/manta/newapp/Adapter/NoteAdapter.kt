package com.manta.newapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manta.newapp.Note
import com.manta.newapp.R
import kotlinx.android.synthetic.main.note_item.view.*
import java.util.*

class NoteAdapter(var mDataset : LinkedList<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(note : Note);
    }

    private var mOnClickListener : OnItemClickListener? = null;


    inner class NoteViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val mDescription: TextView = view.text_view_description;
        val mTitle: TextView = view.text_view_title;
        val mViewPrioirty : TextView = view.text_view_priority;

        init {
            view.setOnClickListener {
                if(adapterPosition != RecyclerView.NO_POSITION)
                    mOnClickListener?.onItemClick(mDataset[adapterPosition]);
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false);
        return NoteViewHolder(view);
    }


    override fun getItemCount(): Int {
        return mDataset.size;
    }

    fun setOnItemClickListener(listner : OnItemClickListener) {mOnClickListener = listner;}

    fun getNodeAt(pos : Int) : Note {
        return mDataset.get(pos);
    }

    fun moveItem(from : Int, to : Int) {
        Collections.swap(mDataset, from, to);
        notifyItemMoved(from, to);

    }
    fun setDataset(dataset  : LinkedList<Note>){
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
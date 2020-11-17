package com.manta.newapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.manta.newapp.Note
import com.manta.newapp.NoteViewModel
import com.manta.newapp.R
import kotlinx.android.synthetic.main.note_item.view.*
import java.util.*

class NoteAdapter() : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(){

    private var mDataset : List<Note> = LinkedList();

    class NoteDiffUtilCallback(private val oldList : List<Note>, private val newList : List<Note>) : DiffUtil.Callback(){
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id;
        }

        override fun getOldListSize(): Int {
            return oldList.size;
        }

        override fun getNewListSize(): Int {
            return newList.size;
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition];
        }

    }

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
                    mOnClickListener?.onItemClick(mDataset.get(adapterPosition));
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false);
        return NoteViewHolder(view);
    }



    fun setOnItemClickListener(listner : OnItemClickListener) {mOnClickListener = listner;}

    fun getNodeAt(pos : Int) : Note {
        return mDataset.get(pos);
    }

    fun moveItem(from : Int, to : Int) {
        Collections.swap(mDataset, from, to); // 터짐.. 이거 안되나봄
        notifyItemMoved(from, to);
    }

    fun setNotes(dataset : List<Note>){
        val result = DiffUtil.calculateDiff(NoteDiffUtilCallback(mDataset, dataset));
        mDataset = dataset;
        result.dispatchUpdatesTo(this);
    }


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = mDataset.get(position);
        holder.mTitle.text = note.title;
        holder.mDescription.text = note.description;
        holder.mViewPrioirty.text = note.priority.toString();
    }

    override fun getItemCount(): Int {
        return mDataset.size;
    }


}
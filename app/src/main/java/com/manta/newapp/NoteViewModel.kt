package com.manta.newapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.util.*

class NoteViewModel (application: Application) : ViewModel() {
    private var mRepository: NoteRepository = NoteRepository(application);
    private var mAllNotes : LiveData<List<Note>>;


    init {
        mAllNotes = mRepository.getAllNotes();
    }

    fun insert(note : Note){
        mRepository.insert(note);
    }

    fun update(note : Note){
        mRepository.update(note);
    }

    fun delete(note :Note){
        mRepository.delete(note);
    }

    fun deleteAllNotes(){
        mRepository.deleteAll();
    }

    fun    getAllNotes() : LiveData<List<Note>> = mAllNotes;

}
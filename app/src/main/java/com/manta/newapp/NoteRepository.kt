package com.manta.newapp

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Update

//데이터베이스가 여러 종류가 있을때, 그것들을 묶어주는 역할(추상화)
class NoteRepository(application : Application) {
    private lateinit var mNoteDao : NoteDao;
    private lateinit var mAllNotes : LiveData<List<Note>>;

    init {
        val noteDatabase = NoteDatabase.getInstance(application);
        mNoteDao = noteDatabase.noteDao();
        mAllNotes = mNoteDao.getAllnotes();
    }

    fun update(note : Note){
        UpdateNoteAsyncTask(mNoteDao).execute(note);
    }

    fun insert(note : Note){
        InsertNoteAsyncTask(mNoteDao).execute(note);
    }


    fun delete(note : Note){
        DeleteNoteAsyncTask(mNoteDao).execute(note);
    }


    fun deleteAll(){
        DeleteAlltNoteAsyncTask(mNoteDao).execute();
    }

    fun getAllNotes() : LiveData<List<Note>> = mAllNotes;


    class  UpdateNoteAsyncTask(private val noteDao : NoteDao) :  AsyncTask<Note, Void, Void>()
    {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.update(it) };
            return null;
        }
    }

    class  InsertNoteAsyncTask(private val noteDao : NoteDao) :  AsyncTask<Note, Void, Void>()
    {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.insert(it) };
            return null;
        }
    }

    class  DeleteNoteAsyncTask(private val noteDao : NoteDao) :  AsyncTask<Note, Void, Void>()
    {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.delete(it) };
            return null;
        }
    }

    class  DeleteAlltNoteAsyncTask(private val noteDao : NoteDao) :  AsyncTask<Void, Void, Void>()
    {
        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.deleteAll();
            return null;
        }
    }




}
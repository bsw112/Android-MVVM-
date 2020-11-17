package com.manta.newapp

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Update
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

//데이터베이스가 여러 종류가 있을때, 그것들을 묶어주는 역할(추상화)
class NoteRepository(application : Application) {
    private lateinit var mNoteDao : NoteDao;
    private lateinit var mAllNotes : LiveData<List<Note>>;
    //어플리케이션 종료시 자동으로 executor종료
    private val executor  = Executors.newSingleThreadExecutor(ThreadFactory { r->  Thread(r).apply { isDaemon = true; } });

    init {
        val noteDatabase = NoteDatabase.getInstance(application);
        mNoteDao = noteDatabase.noteDao();
        mAllNotes = mNoteDao.getAllnotes();
    }

    fun update(note : Note){
        executor.execute { mNoteDao.update(note);  }
        //UpdateNoteAsyncTask(mNoteDao).execute(note);
    }

    fun insert(note : Note){
        executor.execute{ mNoteDao.insert(note);}
        //InsertNoteAsyncTask(mNoteDao).execute(note);
    }


    fun delete(note : Note){
        executor.execute{mNoteDao.delete(note);}
        //DeleteNoteAsyncTask(mNoteDao).execute(note);
    }


    fun deleteAll(){
        executor.execute{mNoteDao.deleteAll();}
        //DeleteAlltNoteAsyncTask(mNoteDao).execute();
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
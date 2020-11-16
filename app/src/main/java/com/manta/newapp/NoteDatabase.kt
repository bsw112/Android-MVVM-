package com.manta.newapp

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    //RoomDatabase을 구현하는 하위클래스는 abstract 이여야한다.
    //databaseBuilder를 통해 생성된 객체는 이 메서드를 구현한다.
    //구현된 noteDao()는 NoteDao의 구현체를 반환한다. (NoteDao는 인터페이스였음을 기억)
    //이건 Room 라이브러리의 약속이다.
    abstract fun noteDao() : NoteDao;

    companion object {
        private var INSTANCE: NoteDatabase? = null;

        fun getInstance(context: Context): NoteDatabase =
            INSTANCE ?: synchronized(NoteDatabase::class){
                    INSTANCE =  Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
                    return INSTANCE as NoteDatabase;
                }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { PopulateDbAsyncTask(it).execute() }
            }
        }

    }


    private class PopulateDbAsyncTask(db: NoteDatabase) : AsyncTask<Void?, Void?, Void?>() {
        private val noteDao: NoteDao = db.noteDao()

        override fun doInBackground(vararg params: Void?): Void? {
            noteDao.insert(Note("Title 1", "Description 1", 1))
            noteDao.insert(Note("Title 2", "Description 2", 2))
            noteDao.insert(Note("Title 3", "Description 3", 3))
            return null
        }
    }


}
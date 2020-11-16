package com.manta.newapp

import androidx.lifecycle.LiveData
import androidx.room.*

//data access object

/*
@Dao(Data Access object)
클라이언트와 데이터베이스가 있을때, 클라이언트가 데이터베이스의 상세한 사항을 몰라도 되도록
인터페이스를 제공하는 객체. 이러한 분리는 단일 책임 원칙에 따른 것이다.
데이터베이스에 접근해서 실질적으로 insert, delete 등을 수행하는 메소드를 포함한다.
 */
@Dao
interface
NoteDao {

    @Insert
    fun insert(note : Note);

    @Update
    fun update(note : Note);

    @Delete
    fun delete(node :Note);

    @Query("DELETE FROM note_table")
    fun deleteAll();

    //내림차순정렬
    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllnotes() : LiveData<List<Note>>;
}
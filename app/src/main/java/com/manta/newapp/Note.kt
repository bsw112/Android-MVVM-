package com.manta.newapp

import androidx.room.Entity
import androidx.room.PrimaryKey


/*
 @Entity
 Database 안에 있는 테이블을 Java나 Kotlin 클래스로 나타낸 것이다. 데이터 모델 클래스라고 볼 수 있다.
 */
@Entity(tableName = "note_table")
data class Note(val title: String, val description: String, val priority: Int) {

    //private 이여도, val 이여도 안된다.
    //유니크한 아이디 (자동생성)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0;


}

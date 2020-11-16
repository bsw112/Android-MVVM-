package com.manta.newapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mNoteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NoteViewModel(application) as T;
            }
        }).get(NoteViewModel::class.java);
    }

    private val mNotes = ArrayList<Note>();
    private val REQUEST_ADD_NOTE = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_notes.layoutManager = LinearLayoutManager(this);
        rv_notes.setHasFixedSize(true);

        val noteAdapter = NoteAdapter(mNotes);
        rv_notes.adapter = noteAdapter;



        mNoteViewModel.getAllNotes().observe(this, Observer<List<Note>> { t -> t?.let { noteAdapter.setDataset(it); }; });

        button_add_note.setOnClickListener {
            Intent(this, AddNoteActivity::class.java).let {
                startActivityForResult(it, REQUEST_ADD_NOTE);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_NOTE) {
            val title = data?.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            val description = data?.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            val priority = data?.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            mNoteViewModel.insert(Note(title!!, description!!, priority!!));
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
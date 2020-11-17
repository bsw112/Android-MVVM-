package com.manta.newapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manta.newapp.Adapter.NoteAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mNoteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NoteViewModel(application) as T;
            }
        }).get(NoteViewModel::class.java);
    }

    private val REQUEST_ADD_NOTE = 0;
    private val REQUEST_MODIFY_NOTE = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_notes.layoutManager = LinearLayoutManager(this);
        rv_notes.setHasFixedSize(true);

        val noteAdapter = NoteAdapter();
        rv_notes.adapter = noteAdapter;
        noteAdapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                Intent(this@MainActivity, AddNoteActivity::class.java).apply {
                    putExtra(AddNoteActivity.EXTRA_ID, note.id);
                    putExtra(AddNoteActivity.EXTRA_TITLE, note.title);
                    putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.description);
                    putExtra(AddNoteActivity.EXTRA_PRIORITY, note.priority);
                    startActivityForResult(this, REQUEST_MODIFY_NOTE);
                }
            }
        })

        mNoteViewModel.getAllNotes().observe(this, Observer<List<Note>> { t -> t?.let { noteAdapter.submitList(it); }; });

        button_add_note.setOnClickListener {
            Intent(this, AddNoteActivity::class.java).let {
                startActivityForResult(it, REQUEST_ADD_NOTE);
            }
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                noteAdapter.moveItem(viewHolder.adapterPosition, target.adapterPosition);
                return false;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mNoteViewModel.delete(noteAdapter.getNodeAt(viewHolder.adapterPosition));
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(rv_notes);


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_note -> {
                mNoteViewModel.deleteAllNotes();
                Toast.makeText(this@MainActivity, "All note deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
            else -> return super.onOptionsItemSelected(item);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_NOTE) {
            val title = data?.getStringExtra(AddNoteActivity.EXTRA_TITLE) ?: return;
            val description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION) ?: return;
            val priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0)

            mNoteViewModel.insert(Note(title, description, priority));
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_MODIFY_NOTE) {
            data ?: return;
            val id = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1);
            if (id < 0) {
                Toast.makeText(this, "Can't apply changes", Toast.LENGTH_SHORT).show();
                return;
            }
            val title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE) ?: return;
            val description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION) ?: return;
            val priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0);
            
            val updated = Note(title, description, priority);
            updated.id = id;
            //기본키인 id를 기준으로 조회해서 업데이트
            mNoteViewModel.update(updated);
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.manta.newapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addnode.*
import kotlinx.android.synthetic.main.activity_addnode.view.*
import kotlinx.android.synthetic.main.note_item.*

class AddNoteActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "com.manta.newapp.EXTRA_TITLE";
        const val EXTRA_DESCRIPTION = "com.manta.newapp.EXTRA_DESCRIPTION";
        const val EXTRA_PRIORITY = "com.manta.newapp.EXTRA_PRIORITY";

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnode)

        number_picker_priority.minValue = 0;
        number_picker_priority.maxValue = 10;

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        setTitle("Add Node");
    }

    private fun saveNode() {
        val title = edit_text_title.text.toString();
        val description = edit_text_description.text.toString();
        val priority = number_picker_priority.value;
        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent().apply {
            putExtra(EXTRA_TITLE, title);
            putExtra(EXTRA_DESCRIPTION, description);
            putExtra(EXTRA_PRIORITY, priority);
            setResult(RESULT_OK, this);
        }
        finish();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_node, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_node -> {
                saveNode(); true;
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
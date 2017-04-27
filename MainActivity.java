package com.sneedmiller.juliet.plainolnotes;

import android.app.ListActivity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sneedmiller.juliet.com.sneedmiller.juliet.plainolnotes.data.NoteItem;
import com.sneedmiller.juliet.plainolnotes.noteEditorActivity;
import com.sneedmiller.juliet.com.sneedmiller.juliet.plainolnotes.data.NotesDataSource;

import java.util.List;

import static com.sneedmiller.juliet.plainolnotes.R.*;
import static com.sneedmiller.juliet.plainolnotes.R.menu.main;

public class MainActivity extends ListActivity {

    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    private static final int MENU_DELETE_ID = 1002;
    private int currentNoteId;
    private NotesDataSource datasource;
    List<NoteItem> notesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerForContextMenu(getListView());

        datasource = new com.sneedmiller.juliet.com.sneedmiller.juliet.plainolnotes.data.NotesDataSource(this);

        refreshDisplay();


    }

    private void refreshDisplay() {
        notesList = datasource.findAll();
        ArrayAdapter<NoteItem> adapter =
                new ArrayAdapter<NoteItem>(this, layout.list_item_layout, notesList);
        setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create) {
            createNote();

        }

        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        NoteItem note = NoteItem.getNew();
        Intent intent = new Intent(this, noteEditorActivity.class);
        intent.putExtra("key", note.getKey());
        intent.putExtra("text", note.getText());
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        NoteItem note = notesList.get(position);
        Intent intent = new Intent(this, noteEditorActivity.class);
        intent.putExtra("key", note.getKey());
        intent.putExtra("text", note.getText());
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK){
            NoteItem note = new NoteItem();
            note.setKey(data.getStringExtra("key"));
            note.setText(data.getStringExtra("text"));
            datasource.update(note);
            refreshDisplay();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentNoteId = (int) info.id;
        menu.add(0, MENU_DELETE_ID, 0, "Delete");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getItemId() == MENU_DELETE_ID){
            NoteItem note = notesList.get(currentNoteId);
            datasource.remove(note);
            refreshDisplay();
        }
        return super.onContextItemSelected(item);
    }
}



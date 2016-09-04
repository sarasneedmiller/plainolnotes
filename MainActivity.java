package com.sneedmiller.juliet.plainolnotes;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sneedmiller.juliet.com.sneedmiller.juliet.plainolnotes.data.NoteItem;
import com.sneedmiller.juliet.com.sneedmiller.juliet.plainolnotes.data.NotesDataSource;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotesDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new com.sneedmiller.juliet.com.sneedmiller.juliet.plainolnotes.data.NotesDataSource(this);
        List<NoteItem> notes = datasource.findAll();
        NoteItem note = notes.get(0);
        note.setText("Updated!");
        datasource.update(note);

//        notes = datasource.findAll();
//        note = notes.get(0);

        Log.i("NOTES", note.getKey() + ": " + note.getText());
    }
}

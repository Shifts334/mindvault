package com.example.mindvault.ui.notes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.Note;
import com.example.mindvault.data.NoteDao;

public class CreateNote extends AppCompatActivity {

    private AppDatabase db;
    private NoteDao noteDao;
    private EditText editTitle, editContent;
    private FrameLayout btnSave;
    private Integer editingId = null;
    private final int padding = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left + padding, bars.top + padding, bars.right + padding, bars.bottom + padding);
            return insets;
        });

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "mindvault-db").allowMainThreadQueries().build();
        noteDao = db.noteDao();

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.buttonSave);

        if (getIntent().hasExtra("note_id")) {
            editingId = getIntent().getIntExtra("note_id", -1);
            Note n = noteDao.getNoteById(editingId);
            if (n != null) {
                editTitle.setText(n.title);
                editContent.setText(n.content);
            }
        }

        btnSave.setOnClickListener(v -> saveOrUpdate());
    }

    private void saveOrUpdate() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingId == null) {
            noteDao.insert(new Note(title, content));
        } else {
            Note n = new Note(title, content);
            n.id = editingId;
            noteDao.update(n);
        }
        setResult(RESULT_OK);
        finish();
    }

    public void handleButtonNotesPage(View v) {
        finish();
    }
}
package com.example.mindvault.ui.notes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.Note;
import com.example.mindvault.data.NoteDao;

public class CreateNoteFragment extends Fragment {
    private NoteDao noteDao;
    private EditText editTitle, editContent;
    private FrameLayout btnSave;
    private FrameLayout btnBack;
    private Integer editingId = null;
    private static final String ARG_NOTE_ID = "note_id";

    public static CreateNoteFragment newInstance(Integer id) {
        Bundle args = new Bundle();
        if (id != null) args.putInt(ARG_NOTE_ID, id);
        CreateNoteFragment f = new CreateNoteFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        AppDatabase db = Room.databaseBuilder(requireContext(), AppDatabase.class, "mindvault-db")
                .allowMainThreadQueries()
                .build();

        noteDao = db.noteDao();
        editTitle = root.findViewById(R.id.editTitle);
        editContent = root.findViewById(R.id.editContent);
        btnSave = root.findViewById(R.id.buttonSave);
        btnBack = root.findViewById(R.id.buttonBack);

        readArguments();
        loadNoteIfEditing();

        btnBack.setOnClickListener(v -> handleBackButton());
        btnSave.setOnClickListener(v -> saveNote());
    }

    private void readArguments() {
        if (getArguments() != null && getArguments().containsKey(ARG_NOTE_ID)) {
            int id = getArguments().getInt(ARG_NOTE_ID, -1);
            if (id != -1) editingId = id;
        }
    }

    private void loadNoteIfEditing() {
        if (editingId == null) return;

        Note note = noteDao.getNoteById(editingId);
        if (note != null) {
            editTitle.setText(note.title);
            editContent.setText(note.content);
        }
    }

    private void saveNote() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            return;
        }

        if (editingId == null) {
            noteDao.insert(new Note(title, content));
        } else {
            Note n = new Note(title, content);
            n.id = editingId;
            noteDao.update(n);
        }

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void handleBackButton() {
        requireActivity()
                .getSupportFragmentManager()
                .popBackStack();
    }
}

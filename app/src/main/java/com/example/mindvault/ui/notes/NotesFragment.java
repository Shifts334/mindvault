package com.example.mindvault.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.NoteDao;

public class NotesFragment extends Fragment {

    private AppDatabase db;
    private NoteDao noteDao;
    private NoteAdapter adapter;
    private RecyclerView recycler;
    private View emptyState;

    public NotesFragment() {
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        db = Room.databaseBuilder(requireContext(),
                        AppDatabase.class,
                        "mindvault-db")
                .allowMainThreadQueries()
                .build();
        noteDao = db.noteDao();

        recycler = root.findViewById(R.id.recyclerNotes);
        emptyState = root.findViewById(R.id.emptyState);

        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        adapter = new NoteAdapter(
                noteDao.getAllNotes(),
                note -> {
                    Intent i = new Intent(requireContext(), CreateNote.class);
                    i.putExtra("note_id", note.id);
                    startActivity(i);
                });
        recycler.setAdapter(adapter);

        root.findViewById(R.id.buttonCreateNotesPage)
                .setOnClickListener(v ->
                        startActivity(new Intent(requireContext(), CreateNote.class)));

        updateVisibility();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setNotes(noteDao.getAllNotes());
        updateVisibility();
    }

    private void updateVisibility() {
        boolean hasNotes = adapter.getItemCount() > 0;
        recycler.setVisibility(hasNotes ? View.VISIBLE : View.GONE);
        emptyState.setVisibility(hasNotes ? View.GONE : View.VISIBLE);
    }
}

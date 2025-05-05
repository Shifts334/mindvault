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
import com.example.mindvault.data.Note;
import com.example.mindvault.data.NoteDao;
import com.example.mindvault.ui.main.MainActivity;

public class NotesFragment extends Fragment {

    private NoteDao noteDao;
    private NoteAdapter adapter;
    private RecyclerView recycler;
    private View emptyState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        AppDatabase db = Room.databaseBuilder(requireContext(), AppDatabase.class, "mindvault-db")
                .allowMainThreadQueries()
                .build();

        noteDao = db.noteDao();
        recycler = root.findViewById(R.id.recyclerNotes);
        emptyState = root.findViewById(R.id.emptyState);

        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        adapter = new NoteAdapter(
                noteDao.getAllNotes(),
                note -> openFragment(CreateNoteFragment.newInstance(note.id)),
                note -> confirmDelete(note)
        );
        recycler.setAdapter(adapter);

        root.findViewById(R.id.buttonCreateNotesPage)
                .setOnClickListener(v -> openFragment(CreateNoteFragment.newInstance(null)));

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

    private void openFragment(Fragment f) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, f)
                .addToBackStack(null)
                .commit();
    }

    private void confirmDelete(Note note) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete this note?")
                .setMessage("This action can’t be undone.")
                .setPositiveButton("Delete", (d, w) -> {
                    noteDao.delete(note);
                    adapter.setNotes(noteDao.getAllNotes());
                    updateVisibility();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

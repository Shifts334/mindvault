package com.example.mindvault.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.mindvault.R;
import com.example.mindvault.data.AppDatabase;
import com.example.mindvault.data.NoteDao;
import com.example.mindvault.ui.notes.NotesFragment;
import com.example.mindvault.ui.pomodoro.PomodoroFragment;

public class HomeFragment extends Fragment {
    private TextView activeNotes;
    private LinearLayout addNote;
    private ImageView pomodoro;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View root, Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        AppDatabase db = Room.databaseBuilder(requireContext(), AppDatabase.class, "mindvault-db")
                .allowMainThreadQueries()
                .build();

        NoteDao noteDao = db.noteDao();
        activeNotes = root.findViewById(R.id.activeNotes);
        addNote = root.findViewById(R.id.buttonAddNote);
        pomodoro = root.findViewById(R.id.pomodoro);

        pomodoro.setOnClickListener(v ->
                openFragment(new PomodoroFragment()));
        addNote.setOnClickListener(v ->
                openFragment(new NotesFragment()));

        int count = noteDao.getAllNotes().size();
        activeNotes.setText(count + " active");
    }

    private void openFragment(Fragment f) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, f)
                .addToBackStack(null)
                .commit();
    }
}

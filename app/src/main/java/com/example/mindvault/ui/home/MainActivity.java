package com.example.mindvault.ui.home;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.mindvault.ui.pomodoro.PomodoroFragment;
import com.example.mindvault.R;
import com.example.mindvault.ui.main.HomeFragment;
import com.example.mindvault.ui.notes.NotesFragment;
import com.example.mindvault.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    LinearLayout navHome;
    LinearLayout navNotes;
    LinearLayout navPlanner;
    LinearLayout navFlashcards;
    LinearLayout navProfile;
    LinearLayout pomodoro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));

        navHome = findViewById(R.id.navHome);
        navNotes = findViewById(R.id.navNotes);
        navPlanner = findViewById(R.id.navPlanner);
        navFlashcards = findViewById(R.id.navFlashcards);
        navProfile = findViewById(R.id.navProfile);
        pomodoro = findViewById(R.id.navPlanner);

        navHome.setOnClickListener(v -> loadFragment(new HomeFragment()));
        navNotes.setOnClickListener(v -> loadFragment(new NotesFragment()));
        pomodoro.setOnClickListener(v -> loadFragment(new PomodoroFragment()));

        loadFragment(new HomeFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}

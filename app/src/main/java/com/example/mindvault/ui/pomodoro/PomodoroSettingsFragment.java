package com.example.mindvault.ui.pomodoro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.example.mindvault.R;

public class PomodoroSettingsFragment extends Fragment {
    private EditText focusLengthEditText;
    private EditText pomodorosUntilLongBreakEditText;
    private EditText shortBreakLengthEditText;
    private EditText longBreakLengthEditText;
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomodoro_settings, container, false);

        focusLengthEditText = view.findViewById(R.id.focusLengthEditText);
        pomodorosUntilLongBreakEditText = view.findViewById(R.id.pomodorosUntilLongBreakEditText);
        shortBreakLengthEditText = view.findViewById(R.id.shortBreakLengthEditText);
        longBreakLengthEditText = view.findViewById(R.id.longBreakLengthEditText);
        saveButton = view.findViewById(R.id.saveButton);

        // Load current settings (you might want to store these in SharedPreferences)
        focusLengthEditText.setText("25");
        pomodorosUntilLongBreakEditText.setText("4");
        shortBreakLengthEditText.setText("5");
        longBreakLengthEditText.setText("15");

        saveButton.setOnClickListener(v -> saveSettings());

        return view;
    }

    private void saveSettings() {
        // Save settings to SharedPreferences or ViewModel
        // Then navigate back
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
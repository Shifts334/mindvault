package com.example.mindvault.ui.pomodoro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.mindvault.R;

public class PomodoroFragment extends Fragment {
    private TextView timerText;
    private ImageButton modeButton;
    private ImageButton playPauseButton;
    private ImageButton skipButton;
    private ImageButton settingsButton;

    private CountDownTimer timer;
    private boolean isRunning = false;
    private PomodoroMode currentMode = PomodoroMode.FOCUS;
    private int pomodoroCount = 0;

    // Default values
    private long focusTime = 25 * 60 * 1000; // 25 minutes
    private long shortBreakTime = 5 * 60 * 1000; // 5 minutes
    private long longBreakTime = 15 * 60 * 1000; // 15 minutes
    private int pomodorosUntilLongBreak = 4;

    private long timeLeftInMillis = 25 * 60 * 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        timerText = view.findViewById(R.id.timerText);
        modeButton = view.findViewById(R.id.modeButton);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        skipButton = view.findViewById(R.id.skipButton);
        settingsButton = view.findViewById(R.id.settingsButton);

        // Set initial mode
        setMode(PomodoroMode.FOCUS);

        playPauseButton.setOnClickListener(v -> {
            if (isRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        modeButton.setOnClickListener(v -> switchToNextMode());

        skipButton.setOnClickListener(v -> {
            if (isRunning) {
                timer.cancel();
                completeCurrentMode();
            }
        });

        settingsButton.setOnClickListener(v -> openSettings());

        updateTimerText();
        return view;
    }

    private void openSettings() {
        // Navigate to settings fragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new PomodoroSettingsFragment())
                .addToBackStack(null)
                .commit();
    }

    private void setMode(PomodoroMode mode) {
        currentMode = mode;
        switch (mode) {
            case FOCUS:
                timeLeftInMillis = focusTime;
                break;
            case SHORT_BREAK:
                timeLeftInMillis = shortBreakTime;
                break;
            case LONG_BREAK:
                timeLeftInMillis = longBreakTime;
                break;
        }
        updateTimerText();
    }

    private void switchToNextMode() {
        if (isRunning) {
            timer.cancel();
        }

        if (currentMode == PomodoroMode.FOCUS) {
            pomodoroCount++;
            if (pomodoroCount % pomodorosUntilLongBreak == 0) {
                setMode(PomodoroMode.LONG_BREAK);
            } else {
                setMode(PomodoroMode.SHORT_BREAK);
            }
        } else {
            setMode(PomodoroMode.FOCUS);
        }

        if (isRunning) {
            startTimer();
        }
    }

    private void completeCurrentMode() {
        if (currentMode == PomodoroMode.FOCUS) {
            pomodoroCount++;
            if (pomodoroCount % pomodorosUntilLongBreak == 0) {
                setMode(PomodoroMode.LONG_BREAK);
            } else {
                setMode(PomodoroMode.SHORT_BREAK);
            }
        } else {
            setMode(PomodoroMode.FOCUS);
        }
        startTimer();
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                completeCurrentMode();
            }
        }.start();

        isRunning = true;
        playPauseButton.setImageResource(R.drawable.ic_pause);
    }

    private void pauseTimer() {
        timer.cancel();
        isRunning = false;
        playPauseButton.setImageResource(R.drawable.ic_play);
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeLeftFormatted);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private enum PomodoroMode {
        FOCUS, SHORT_BREAK, LONG_BREAK
    }
}
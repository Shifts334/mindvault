package com.example.mindvault.ui.pomodoro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.mindvault.R;

public class PomodoroFragment extends Fragment {
    private TextView timerText;
    private Button modeButton;
    private Button playPauseButton;
    private Button skipButton;

    private CountDownTimer timer;
    private boolean isRunning = false;
    private boolean isWorkTime = true;
    private long timeLeftInMillis = 25 * 60 * 1000; // 25 minutes work time by default
    private final long workTime = 25 * 60 * 1000; // 25 minutes
    private final long breakTime = 5 * 60 * 1000; // 5 minutes

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        timerText = view.findViewById(R.id.timerText);
        modeButton = view.findViewById(R.id.modeButton);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        skipButton = view.findViewById(R.id.skipButton);

        updateTimerText();

        playPauseButton.setOnClickListener(v -> {
            if (isRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        modeButton.setOnClickListener(v -> switchMode());

        skipButton.setOnClickListener(v -> skipTimer());

        return view;
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
                isWorkTime = !isWorkTime;
                timeLeftInMillis = isWorkTime ? workTime : breakTime;
                updateTimerText();
                startTimer(); // Auto-start next session
            }
        }.start();

        isRunning = true;
        playPauseButton.setText("Pause");
    }

    private void pauseTimer() {
        timer.cancel();
        isRunning = false;
        playPauseButton.setText("Start");
    }

    private void switchMode() {
        if (isRunning) {
            timer.cancel();
        }
        isWorkTime = !isWorkTime;
        timeLeftInMillis = isWorkTime ? workTime : breakTime;
        updateTimerText();
        if (isRunning) {
            startTimer();
        }
    }

    private void skipTimer() {
        if (isRunning) {
            timer.cancel();
        }
        isWorkTime = !isWorkTime;
        timeLeftInMillis = isWorkTime ? workTime : breakTime;
        updateTimerText();
        if (isRunning) {
            startTimer();
        }
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeLeftFormatted);

        modeButton.setText(isWorkTime ? "Switch to Break" : "Switch to Work");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
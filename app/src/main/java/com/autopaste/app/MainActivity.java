package com.autopaste.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private SharedPreferences prefs;
    private EditText textInput, intervalInput, repeatInput;
    private TextView status;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("autopaste", MODE_PRIVATE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(28, 32, 28, 24);
        layout.setBackgroundColor(0xFF101812);

        TextView title = new TextView(this);
        title.setText("AUTO PASTE");
        title.setTextSize(25);
        title.setTextColor(0xFF65F5A0);
        title.setPadding(0, 0, 0, 20);
        layout.addView(title);

        textInput = field("Teks yang akan ditempel");
        textInput.setMinLines(3);
        textInput.setText(prefs.getString("text", "Indonesia"));
        layout.addView(textInput);

        intervalInput = field("Interval (ms) — manual pause reminder");
        intervalInput.setInputType(2);
        intervalInput.setText(prefs.getString("interval", "1000"));
        layout.addView(intervalInput);

        repeatInput = field("Repeat target (note only)");
        repeatInput.setInputType(2);
        repeatInput.setText(prefs.getString("repeat", "10"));
        layout.addView(repeatInput);

        TextView note = new TextView(this);
        note.setText("No clipboard required. One paste per activation. No automatic comments.");
        note.setTextColor(0xFFB9C9BE);
        note.setTextSize(14);
        note.setPadding(0, 16, 0, 16);
        layout.addView(note);

        Button save = button("SAVE TEXT & SETTINGS");
        layout.addView(save);
        save.setOnClickListener(v -> saveSettings());

        Button accessibility = button("ENABLE ACCESSIBILITY SERVICE");
        layout.addView(accessibility);
        accessibility.setOnClickListener(v ->
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));

        Button arm = button("PREPARE 1x PASTE");
        layout.addView(arm);
        arm.setOnClickListener(v -> {
            saveSettings();
            prefs.edit().putBoolean("armed", true).apply();
            Toast.makeText(this, "Ready. Move to the target application and focus the text field.", Toast.LENGTH_LONG).show();
        });

        status = new TextView(this);
        status.setTextColor(0xFF65F5A0);
        status.setPadding(0, 18, 0, 0);
        status.setText("Mode: paste manual satu kali");
        layout.addView(status);

        setContentView(layout);
    }

    private EditText field(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextColor(0xFFFFFFFF);
        e.setHintTextColor(0xFF91A398);
        e.setTextSize(16);
        e.setSingleLine(false);
        e.setPadding(12, 10, 12, 10);
        e.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF65F5A0));
        return e;
    }

    private Button button(String label) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(0xFF102016);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF65F5A0));
        return b;
    }

    private void saveSettings() {
        String interval = intervalInput.getText().toString().trim();
        String repeat = repeatInput.getText().toString().trim();
        try {
            long ms = Long.parseLong(interval);
            int count = Integer.parseInt(repeat);
            if (ms < 1000 || count < 1 || count > 1000) throw new NumberFormatException();
            prefs.edit()
                .putString("text", textInput.getText().toString())
                .putString("interval", interval)
                .putString("repeat", repeat)
                .apply();
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Minimum interval 1000 ms and repetition 1–1000.", Toast.LENGTH_LONG).show();
        }
    }
}

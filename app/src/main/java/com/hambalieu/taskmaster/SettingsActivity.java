package com.hambalieu.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {

    public String TAG = "SettingsActivity";
    public static final String USER_NICKNAME_TAG = "userNickname";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setUserNickname();
        saveUserNickname();
    }

    public void setUserNickname()
    {
        String userNickname = preferences.getString(USER_NICKNAME_TAG, "");
        if (!userNickname.isEmpty())
        {
            EditText userNicknameEditText = (EditText) findViewById(R.id.editTextNicknameInputSettingsActivity);
            userNicknameEditText.setText(userNickname);
        }
    }

    public void saveUserNickname()
    {
        Button saveNicknameButton = (Button) findViewById(R.id.buttonSaveNicknameSettingsActivity);
        saveNicknameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences.Editor preferenceEditor = preferences.edit();
                EditText userNicknameEditText = (EditText) findViewById(R.id.editTextNicknameInputSettingsActivity);
                String inputUserNickname = userNicknameEditText.getText().toString();
                preferenceEditor.putString(USER_NICKNAME_TAG, inputUserNickname);
                preferenceEditor.apply();

                Snackbar.make(findViewById(R.id.textViewSavedSettingsActivity), "Saved", Snackbar.LENGTH_LONG).show();
            }
        });
    }

}
package com.hambalieu.taskmaster.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.google.android.material.snackbar.Snackbar;
import com.hambalieu.taskmaster.R;

import java.util.ArrayList;



public class SettingsActivity extends AppCompatActivity {
    SharedPreferences preferences;
    public String TAG = "SettingsActivity";
    public static final String USER_NICKNAME_TAG = "userNickname";
    public static final String CHOOSE_TEAM_TAG = "Choose Team";
    Spinner teamSettingsPageSpinner = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUserNickname();
        saveUserNickname();
        setUpTeamSpinner();


    }

    public void setUserNickname()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userNickname = preferences.getString(USER_NICKNAME_TAG, "");
        if (!userNickname.isEmpty())
        {
            EditText userNicknameEditText =findViewById(R.id.editTextNicknameInputSettingsActivity);
            userNicknameEditText.setText(userNickname);
        }
//        teamPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String team = teamPreferences.getString(CHOOSE_TEAM_TAG,"");
//        if (!team.isEmpty()){
//            Spinner teamsettingsPageSpinner = findViewById(R.id.settingsPageSpinner);
//        }
//

    }


    public void setUpTeamSpinner(){

        teamSettingsPageSpinner = findViewById(R.id.settingsPageSpinner);

        Amplify.API.query(
                ModelQuery.list(Team.class),
                success ->
                {
                    Log.i(TAG, "Read teams successfully!");
                    ArrayList<String> teamNames = new ArrayList<>();
                    ArrayList<Team>  teams = new ArrayList<>();

                    for (Team team  : success.getData())
                    {
                        teams.add(team);
                        teamNames.add(team.getTeamName());

                    }

                    runOnUiThread(() ->
                            teamSettingsPageSpinner.setAdapter(new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    teamNames)));
                },
                failure -> Log.i(TAG, "Did not add team names successfully!")
        );

    }

    public void saveUserNickname()
    {
        Button saveNicknameButton = findViewById(R.id.buttonSaveNicknameSettingsActivity);
        saveNicknameButton.setOnClickListener(view -> {
            SharedPreferences.Editor preferenceEditor = preferences.edit();
            EditText userNicknameEditText =  findViewById(R.id.editTextNicknameInputSettingsActivity);
            String inputUserNickname = userNicknameEditText.getText().toString();
            preferenceEditor.putString(USER_NICKNAME_TAG, inputUserNickname);

            String teamNameSelected = teamSettingsPageSpinner.getSelectedItem().toString();
            preferenceEditor.putString(CHOOSE_TEAM_TAG, teamNameSelected);

            preferenceEditor.apply();

            Snackbar.make(findViewById(R.id.textViewSavedSettingsActivity), "Saved", Snackbar.LENGTH_LONG).show();
//                Intent goToHomePage = new Intent(SettingsActivity.this , MainActivity.class );
//                startActivity(goToHomePage);

        });
    }

}
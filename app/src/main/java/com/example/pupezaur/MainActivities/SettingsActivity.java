package com.example.pupezaur.MainActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pupezaur.R;

import java.util.Locale;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.settings_activity);

        String settings = getResources().getString(R.string.settings);
        getSupportActionBar().setTitle(settings);
    }

    public void changeLanguage(View view) {
        CharSequence options[] = new CharSequence[]{
                "English",
                "Română"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String selectLang = getResources().getString(R.string.selectLanguage);
        builder.setTitle(selectLang);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                String restartApp = getResources().getString(R.string.restartApp);
                if (j == 0) {
                    setAppLocale("en");
                    Toast.makeText(SettingsActivity.this, restartApp, Toast.LENGTH_SHORT).show();
                } else {
                    setAppLocale("ro");
                    Toast.makeText(SettingsActivity.this, restartApp, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    public void setAppLocale (String toLoad) {
        Locale locale = new Locale(toLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale= locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_lang", toLoad);
        editor.apply();
    }
    public void loadLocale (){
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_lang", "");
        setAppLocale(language);
    }
}

package com.hackatoncivico.rankingpolitico;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hackatoncivico.rankingpolitico.utils.Utils;


public class LogrosActivity extends AppCompatActivity {

    public static final String ID_CANDIDATO = "ID_CANDIDATO";

    private String idCandidato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_activity_logros));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        idCandidato = sharedPref.getString(Utils.SELECTED_CANDIDATE, "");
    }

}

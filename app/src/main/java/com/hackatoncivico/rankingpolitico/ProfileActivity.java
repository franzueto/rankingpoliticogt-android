package com.hackatoncivico.rankingpolitico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by franz on 7/11/2015.
 */
public class ProfileActivity extends AppCompatActivity {

    public static final String ID_CANDIDATO = "ID_CANDIDATO";

    private String idCandidato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_profile_activity));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idCandidato = extras.getString(ID_CANDIDATO);

            TextView text = (TextView) findViewById(R.id.profile_text);
            text.setText(idCandidato);
        }
    }
}

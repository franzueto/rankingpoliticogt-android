package com.hackatoncivico.rankingpolitico;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.Criterio;
import com.hackatoncivico.rankingpolitico.models.CriterioCandidato;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidato;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidatos;
import com.hackatoncivico.rankingpolitico.utils.ApiAccess;
import com.hackatoncivico.rankingpolitico.utils.Utils;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by franz on 7/11/2015.
 */
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    public static final String ID_CANDIDATO = "ID_CANDIDATO";

    private String idCandidato;

    private Candidato candidato;

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

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        idCandidato = sharedPref.getString(Utils.SELECTED_CANDIDATE, "");

        GetCandidato data = new GetCandidato();
        data.execute();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (candidato != null) {
            handleCandidato(candidato);
        }
    }

    private void handleCandidato(final Candidato candidato){
        this.candidato = candidato;

        Button btn_logros = (Button) findViewById(R.id.btn_logros);
        btn_logros.setOnClickListener(Utils.setNextScreenListener(this, LogrosActivity.class, LogrosActivity.ID_CANDIDATO, String.valueOf(candidato.id)));

        Button btn_criterios = (Button) findViewById(R.id.btn_criterios);
        btn_criterios.setOnClickListener(Utils.setNextScreenListener(this, CriteriosActivity.class, CriteriosActivity.ID_CANDIDATO, String.valueOf(candidato.id)));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //progressBar.setVisibility(View.GONE);
                ImageView avatar = (ImageView) findViewById(R.id.profile_avatar);

                Picasso.with(getBaseContext())
                        .load(ApiAccess.DOMINIO_URL + candidato.foto)
                        .placeholder(R.drawable.avatar)
                        .into(avatar);

                TextView full_name = (TextView) findViewById(R.id.profile_full_name);
                full_name.setText(candidato.nombres + " " + candidato.apellidos);

                TextView logros = (TextView) findViewById(R.id.profile_logros_count);
                logros.setText(String.valueOf(candidato.logros.size()));

                int criterios_count = 0;

                for (int i = 0; i < candidato.criterios.size(); i++) {
                    CriterioCandidato criterioCandidato = candidato.criterios.get(i);
                    Criterio criterio = criterioCandidato.criterio;
                    try{
                        criterios_count = criterios_count + Integer.parseInt(criterio.puntuacion);
                    } catch (NumberFormatException nfe){
                        nfe.printStackTrace();
                    }
                }

                TextView criterios = (TextView) findViewById(R.id.profile_criterios_count);
                criterios.setText(String.valueOf(criterios_count));
            }
        });
    }


    private class GetCandidato extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(ApiAccess.CANDIDATOS_URL + '/' + idCandidato);

                //Perform the request and check the status code
                HttpResponse response = client.execute(get);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        //Read the server response and attempt to parse it as JSON
                        Reader reader = new InputStreamReader(content);

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        //gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                        Gson gson = gsonBuilder.create();
                        //List<Candidato> posts = new ArrayList<Candidato>();
                        //posts = Arrays.asList(gson.fromJson(reader, Candidato[].class));
                        RegistroCandidato registroCandidato = gson.fromJson(reader, RegistroCandidato.class);

                        content.close();

                        handleCandidato(registroCandidato.registros);
                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                        //failedLoadingPosts();
                    }
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
                    //failedLoadingPosts();
                }
            } catch(Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
                //failedLoadingPosts();
            }
            return null;
        }
    }

}

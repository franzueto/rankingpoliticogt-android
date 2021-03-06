package com.hackatoncivico.rankingpolitico;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackatoncivico.rankingpolitico.adapters.RVCriteriosAdapter;
import com.hackatoncivico.rankingpolitico.adapters.RVLogrosAdapter;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidato;
import com.hackatoncivico.rankingpolitico.utils.ApiAccess;
import com.hackatoncivico.rankingpolitico.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class CriteriosActivity extends AppCompatActivity {

    private static final String TAG = "CriteriosActivity";

    public static final String ID_CANDIDATO = "ID_CANDIDATO";

    private String idCandidato;

    private RecyclerView rv_criterios;
    private RVCriteriosAdapter adapter;

    private Candidato candidato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criterios);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_activity_logros));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        idCandidato = sharedPref.getString(Utils.SELECTED_CANDIDATE, "");

        rv_criterios = (RecyclerView) findViewById(R.id.rv_criterios);
        rv_criterios.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_criterios.setLayoutManager(llm);

        GetCriterios data = new GetCriterios();
        data.execute();
    }

    private void handleCandidato(final Candidato candidato){
        this.candidato = candidato;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new RVCriteriosAdapter(getBaseContext(), candidato.criterios);
                rv_criterios.setAdapter(adapter);
            }
        });
    }

    private class GetCriterios extends AsyncTask<String, Void, Void> {

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

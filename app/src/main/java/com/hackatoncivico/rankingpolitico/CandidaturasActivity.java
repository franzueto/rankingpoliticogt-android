package com.hackatoncivico.rankingpolitico;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackatoncivico.rankingpolitico.adapters.RVRankingAdapter;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.Candidatura;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidatos;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidaturas;
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
import java.util.List;


public class CandidaturasActivity extends AppCompatActivity {

    private static final String TAG = "CandidaturasActivity";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidaturas);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_activity_candidaturas));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Utils.removePreference(this, Utils.SELECTED_CANDIDATURA);
        Utils.removePreference(this, Utils.SELECTED_ORGANIZACION);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_candidaturas);

        GetCandidaturas data = new GetCandidaturas();
        data.execute();
    }

    private void handleCandidaturasList(final List<Candidatura> candidadaturas){

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String[] values = new String[candidadaturas.size()];

                for (int i = 0; i < candidadaturas.size() ; i++) {
                    values[i] = candidadaturas.get(i).titulo;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, values);

                listView.setAdapter(adapter);

                // ListView Item Click Listener
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(getBaseContext(), OrganizacionesActivity.class);
                        //intent.putExtra(OrganizacionesActivity.ID_CANDIDATURA, String.valueOf(candidadaturas.get(position).id));
                        Utils.savePreference(getBaseContext(), Utils.SELECTED_CANDIDATURA, String.valueOf(candidadaturas.get(position).id));
                        startActivity(intent);
                    }

                });
            }
        });
    }

    private class GetCandidaturas extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(ApiAccess.CANDIDATURAS_URL);

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
                        RegistroCandidaturas registroCandidaturas = gson.fromJson(reader, RegistroCandidaturas.class);

                        content.close();

                        handleCandidaturasList(registroCandidaturas.registros);
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

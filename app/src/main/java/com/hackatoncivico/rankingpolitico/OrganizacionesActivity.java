package com.hackatoncivico.rankingpolitico;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackatoncivico.rankingpolitico.adapters.RVOrganizacionesAdapter;
import com.hackatoncivico.rankingpolitico.adapters.RVRankingAdapter;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.Organizacion;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidatos;
import com.hackatoncivico.rankingpolitico.models.RegistroOrganizaciones;
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


public class OrganizacionesActivity extends AppCompatActivity {

    private static final String TAG = "OrganizacionesActivity";

    public static final String ID_CANDIDATURA = "ID_CANDIDATURA";

    private String idCandidatura;

    private RecyclerView rv_organizaciones;
    private RVOrganizacionesAdapter adapter;

    List<Organizacion> organizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizaciones);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_activity_organizaciones));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get parameters
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        idCandidatura = sharedPref.getString(Utils.SELECTED_CANDIDATURA, "");

        rv_organizaciones = (RecyclerView) findViewById(R.id.rv_organizaciones);
        rv_organizaciones.setHasFixedSize(true);

        rv_organizaciones.setLayoutManager(new GridLayoutManager(this, 2));

        GetOrganizaciones data = new GetOrganizaciones();
        data.execute();
    }

    private void handleOrganizacionesList(final List<Organizacion> organizaciones){
        this.organizaciones = organizaciones;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new RVOrganizacionesAdapter(getBaseContext(), organizaciones, idCandidatura);
                rv_organizaciones.setAdapter(adapter);
            }
        });
    }

    private class GetOrganizaciones extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(ApiAccess.ORGANIZACIONES_URL);

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
                        RegistroOrganizaciones registroOrganizaciones = gson.fromJson(reader, RegistroOrganizaciones.class);

                        content.close();

                        Log.e(TAG, registroOrganizaciones.registros.size()+"");

                        handleOrganizacionesList(registroOrganizaciones.registros);
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

package com.hackatoncivico.rankingpolitico;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackatoncivico.rankingpolitico.adapters.RVCandidaturasPartidosAdapter;
import com.hackatoncivico.rankingpolitico.adapters.RVLogrosAdapter;
import com.hackatoncivico.rankingpolitico.adapters.RVRankingAdapter;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidato;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidatos;
import com.hackatoncivico.rankingpolitico.utils.ApiAccess;
import com.hackatoncivico.rankingpolitico.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CandidaturasPartidosActivity extends AppCompatActivity {

    private static final String TAG = "CPActivity";

    public static final String ID_CANDIDATURA = "ID_CANDIDATURA";
    public static final String ID_ORGANIZACION = "ID_ORGANIZACION";

    private String idCandidatura;
    private String idOrganizacion;

    private RecyclerView rv_candidaturas_partidos;
    private RVCandidaturasPartidosAdapter adapter;

    private List<Candidato> candidatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidaturas_partidos);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_activity_candidaturas_partidos));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get parameters
            idCandidatura = extras.getString(ID_CANDIDATURA);
            idOrganizacion = extras.getString(ID_ORGANIZACION);

            rv_candidaturas_partidos = (RecyclerView) findViewById(R.id.rv_candidaturas_partidos);
            rv_candidaturas_partidos.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(this);
            rv_candidaturas_partidos.setLayoutManager(llm);

            GetRankingPolitico data = new GetRankingPolitico();
            data.execute();
        }
    }

    private void handleCandidatos(final List<Candidato> candidatos){
        /*this.candidatos = candidatos;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new RVCandidaturasPartidosAdapter(getBaseContext(), candidatos);
                rv_candidaturas_partidos.setAdapter(adapter);
            }
        });*/
    }

    private class GetRankingPolitico extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("idcandidatura", idCandidatura);
                jsonObject.accumulate("idorganizacion", idOrganizacion);

                String jsonParams = jsonObject.toString();
                StringEntity se = new StringEntity(jsonParams);

                HttpPost post = new HttpPost(ApiAccess.CANDIDATURAS_PARTIDOS_URL);

                post.setEntity(se);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-type", "application/json");

                //Perform the request and check the status code
                HttpResponse response = client.execute(post);
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
                        RegistroCandidatos registroCandidatos = gson.fromJson(reader, RegistroCandidatos.class);

                        content.close();

                        handleCandidatos(registroCandidatos.registros);
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

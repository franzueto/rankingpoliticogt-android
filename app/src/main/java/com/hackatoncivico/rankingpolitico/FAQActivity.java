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
import com.hackatoncivico.rankingpolitico.adapters.RVFAQAdapter;
import com.hackatoncivico.rankingpolitico.adapters.RVLogrosAdapter;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.FAQ;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidato;
import com.hackatoncivico.rankingpolitico.models.RegistroFAQs;
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
import java.util.ArrayList;
import java.util.List;


public class FAQActivity extends AppCompatActivity {

    private static final String TAG = "FAQActivity";

    private RecyclerView rv_faqs;
    private RVFAQAdapter adapter;

    private List<FAQ> faqs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.title_activity_faqs));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_faqs = (RecyclerView) findViewById(R.id.rv_faqs);
        rv_faqs.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_faqs.setLayoutManager(llm);

        /* TODO: UNCOMMENT AND CHANGE THIS WHEN FAQ FROM API BECOMES READY */
        GetFAQs data = new GetFAQs();
        data.execute();
        //initializeData();
    }

    private void initializeData(){
        faqs = new ArrayList<FAQ>();
        faqs.add(new FAQ(getString(R.string.pregunta_1), getString(R.string.respuesta_1)));
        faqs.add(new FAQ(getString(R.string.pregunta_2), getString(R.string.respuesta_2)));
        faqs.add(new FAQ(getString(R.string.pregunta_3), getString(R.string.respuesta_3)));
        adapter = new RVFAQAdapter(faqs);
        rv_faqs.setAdapter(adapter);
    }

    private void handleFAQs(final List<FAQ> faqs){
        this.faqs = faqs;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new RVFAQAdapter(faqs);
                rv_faqs.setAdapter(adapter);
            }
        });
    }

    private class GetFAQs extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(ApiAccess.FAQS_URL);

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
                        RegistroFAQs registroFAQs = gson.fromJson(reader, RegistroFAQs.class);

                        content.close();

                        handleFAQs(registroFAQs.registros);
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

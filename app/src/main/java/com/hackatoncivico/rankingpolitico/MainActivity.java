package com.hackatoncivico.rankingpolitico;

import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackatoncivico.rankingpolitico.adapters.RVRankingAdapter;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidato;
import com.hackatoncivico.rankingpolitico.utils.ApiAccess;

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


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private List<Candidato> candidatos;

    private RVRankingAdapter adapter;
    private RecyclerView rv_ranking;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.ranking_progress_bar);

        setUpDrawer(toolbar);

        rv_ranking = (RecyclerView)findViewById(R.id.rv_ranking);
        rv_ranking.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv_ranking.setLayoutManager(llm);

        progressBar.setVisibility(View.VISIBLE);
        //Get Data from API
        Log.e(TAG, "HERE:2 ");
        GetRankingPolitico data = new GetRankingPolitico();
        data.execute();
        Log.e(TAG, "HERE: 3 ");
    }

    private void setUpDrawer(Toolbar toolbar){
        // Configure Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_left);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        };

        drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        //Navigation Listener
        NavigationView main_drawer_navigation = (NavigationView) findViewById(R.id.main_drawer_navigation);
        main_drawer_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                /*TODO FIX WITH RIGHT ACTIONS*/
                switch (menuItem.getItemId()) {
                    case R.id.ranking:
                        //Intent mainIntent = new Intent(MainActivity.this, ProfileActivity.class);
                        //startActivity(mainIntent);
                        break;
                    case R.id.partidos_politicos:
                        //Intent faqIntent = new Intent(MainActivity.this, FAQActivity.class);
                        //startActivity(faqIntent);
                        break;
                    default:
                        Log.w("TAG", "Do nothing");
                        break;
                }

                return true;
            }
        });
    }

    private void handleCandidatosList(final List<Candidato> candidatos){
        this.candidatos = candidatos;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

                adapter = new RVRankingAdapter(getBaseContext(), candidatos);
                rv_ranking.setAdapter(adapter);
            }
        });
    }

    private class GetRankingPolitico extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.e(TAG, "HERE: ");
            try {
                //Create an HTTP client
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(ApiAccess.CANDIDATOS_URL);

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

                        Log.e(TAG, "" + registroCandidato.registros.size());

                        handleCandidatosList(registroCandidato.registros);
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

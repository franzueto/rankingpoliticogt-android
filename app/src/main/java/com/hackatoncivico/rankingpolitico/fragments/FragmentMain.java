package com.hackatoncivico.rankingpolitico.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hackatoncivico.rankingpolitico.R;
import com.hackatoncivico.rankingpolitico.adapters.RVRankingAdapter;
import com.hackatoncivico.rankingpolitico.models.Candidato;
import com.hackatoncivico.rankingpolitico.models.RegistroCandidatos;
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

/**
 * Created by franz on 7/12/2015.
 */
public class FragmentMain extends Fragment {

    private static final String TAG = "MainFragment";

    private List<Candidato> candidatos;

    private RVRankingAdapter adapter;
    private RecyclerView rv_ranking;

    private ProgressBar progressBar;

    public static FragmentMain newInstance() { return new FragmentMain(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.removePreference(getActivity(), Utils.SELECTED_CANDIDATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //progressBar = (ProgressBar) findViewById(R.id.ranking_progress_bar);

        rv_ranking = (RecyclerView) view.findViewById(R.id.rv_ranking);
        rv_ranking.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv_ranking.setLayoutManager(llm);

        progressBar = (ProgressBar) view.findViewById(R.id.ranking_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        //Get Data from API
        GetRankingPolitico data = new GetRankingPolitico();
        data.execute();

        return view;
    }

    private void handleCandidatosList(final List<Candidato> candidatos){
        this.candidatos = candidatos;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

                adapter = new RVRankingAdapter(getActivity(), candidatos);
                rv_ranking.setAdapter(adapter);
            }
        });
    }

    private class GetRankingPolitico extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
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
                        RegistroCandidatos registroCandidatos = gson.fromJson(reader, RegistroCandidatos.class);

                        content.close();

                        handleCandidatosList(registroCandidatos.registros);
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

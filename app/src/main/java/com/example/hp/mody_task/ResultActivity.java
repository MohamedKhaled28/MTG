package com.example.hp.mody_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.Adapter3;
import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;
import Model.artifact_search;

public class ResultActivity extends Home_activity  {
    ListView listView;
    String contect = null;
    List<artifact_search> artifactsList;
    String user_id2;
   Adapter3 adapter;
    artifact_search artifact;
    Boolean checkconnect;
    private TextToSpeech textToSpeech;
    SharedPreferences myPrefs;
    String search_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        checkconnect = CheckConnectivity.isNetworkAvaliable(ResultActivity.this);
        if(checkconnect.equals(true)) {
            getsearchdata();

        }else {
            finish();
            Toast.makeText(ResultActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkconnect = CheckConnectivity.isNetworkAvaliable(ResultActivity.this);
        if (checkconnect.equals(true)) {
            getsearchdata();

        } else {
            finish();
            Toast.makeText(ResultActivity.this, getString(R.string.valid_connection), Toast.LENGTH_LONG).show();
        }
    }

    public  void getsearchdata(){
        myPrefs = getSharedPreferences("my pref", 0);
        user_id2 = myPrefs.getString("ID",null);
        listView = (ListView) findViewById(R.id.list);
        Intent intent2 = getIntent();
        search_key = (String) intent2.getSerializableExtra("artifact_name");
       // setTitle(search_key);
        checkconnect = CheckConnectivity.isNetworkAvaliable(ResultActivity.this);
        if(checkconnect.equals(true)) {
        background back_ground = new background();
        back_ground.execute("http://192.168.1.3/MTG/api_search.php", user_id2, search_key);
        }else {
            finish();
            Toast.makeText(ResultActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();

        }

    }


    public Bitmap getBitmapfromurl (String src){
        try{
            URL url = new URL(src);
            HttpURLConnection connection =  (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap mybitmap = BitmapFactory.decodeStream(input);
            return mybitmap;


        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void appearsearch(View view) {
        int ID = view.getId();
        if(ID == R.id.btnsearch){
            TextView tv = (TextView) findViewById(R.id.name_museum);
            TextView tv1 = (TextView) findViewById(R.id.description_name);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
            tv.setVisibility(View.VISIBLE);


        }else if (ID == R.id.btnsearch2){
            TextView tv = (TextView) findViewById(R.id.name_museum);
            TextView tv1 = (TextView) findViewById(R.id.description_name);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
            tv1.setVisibility(View.VISIBLE);
            final String lang = myPrefs.getString("Lang",null);
            final String tospeak = artifact.getDescription().toString();
            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        switch (lang){
                            case "English":
                                textToSpeech.setLanguage(Locale.ENGLISH);
                                break;
                            case "Deutsche":
                                textToSpeech.setLanguage(Locale.GERMAN);

                        }
                        textToSpeech.speak(search_key+tospeak,TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            });

        }
    }


    private class background extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            contect = DBconnection.getdata(params);
            return contect;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("")) {
                finish();
                Toast.makeText(ResultActivity.this,getString(R.string.valid_text),Toast.LENGTH_LONG).show();

            }
            else {

                artifactsList = new ArrayList<artifact_search>();
                JSONArray array = JsonParser.parseJson(s);
                for (int i = 0; i < array.length(); i++) {

                    try {
                        JSONObject object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                        artifact = new artifact_search();
                        artifact.setImage(object.getString("artifact_image"));
                        artifact.setName(object.getString("artifact_name"));
                        artifact.setMuesum_name(object.getString("museum_name"));
                        artifact.setDescription(object.getString("description"));
                        artifactsList.add(artifact);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                adapter = new Adapter3(ResultActivity.this , 2 , artifactsList);
                listView.setAdapter(adapter);
                TextView Name_Museum = (TextView) findViewById(R.id.name_museum);
                TextView Description = (TextView) findViewById(R.id.description_name);
                Name_Museum.setText(artifactsList.get(0).getMuesum_name());
                Description.setText(artifactsList.get(0).getDescription());
                setTitle(artifactsList.get(0).getName());


                ImageView img = (ImageView) findViewById(R.id.muteimg);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(textToSpeech!=null) {
                            textToSpeech.shutdown();
                            textToSpeech.stop();
                        }
                    }
                });
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.finish();
    }
}

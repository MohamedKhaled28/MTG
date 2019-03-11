package com.example.hp.mody_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import Model.Adapter6;
import Model.AllArtifactsclass;
import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;

public class Allartifacts extends Home_activity {
    SharedPreferences sharedPreferences;
    String museum_name , userid ;
    Boolean Checkconnect;
   List<AllArtifactsclass> artifactslist;
    Adapter6 adapter6;
    ListView listview;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allartifacts);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Checkconnect = CheckConnectivity.isNetworkAvaliable(Allartifacts.this);
        if(Checkconnect.equals(true)) {
            getAllart();
        }else{
           finish();
            Toast.makeText(Allartifacts.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
    }
    public  void  getAllart(){
        sharedPreferences = getApplicationContext().getSharedPreferences("my pref",0);
        museum_name = sharedPreferences.getString("art_name",null);
        setTitle(museum_name);
        Checkconnect = CheckConnectivity.isNetworkAvaliable(Allartifacts.this);
        if(Checkconnect.equals(true)) {
           artifactstask newdesc = new artifactstask();
            newdesc.execute("http://192.168.1.3/MTG/select_artifacts.php", userid, museum_name);
        }else{
            finish();
            Toast.makeText(Allartifacts.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
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
    private List<AllArtifactsclass> parsingg(String context) {
        AllArtifactsclass artifact =null;
        artifactslist = new ArrayList<>();
        JSONArray array = JsonParser.parseJson(context);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(String.valueOf(array.getJSONObject(i)));

                artifact = new AllArtifactsclass();
                artifact.setID(object.getInt("ID"));
                artifact.setName(object.getString("Name"));
                artifact.setImage(object.getString("Image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            artifactslist.add(artifact);

        }
        return artifactslist;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Checkconnect = CheckConnectivity.isNetworkAvaliable(Allartifacts.this);
        if(Checkconnect.equals(true)) {
            getAllart();
        }else{
            finish();
            Toast.makeText(Allartifacts.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
    }
    private class artifactstask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            String context = null;
            context = DBconnection.getdata(params);
            artifactslist = parsingg(context);
            return context;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (artifactslist.size() == 0) {
                finish();
                Toast.makeText(Allartifacts.this, getString(R.string.valid_text), Toast.LENGTH_LONG).show();
            } else {
                adapter6 = new Adapter6(Allartifacts.this, 2, artifactslist);
                listview = (ListView) findViewById(R.id.listallart);
                listview.setAdapter(adapter6);
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String artifactselected = String.valueOf(artifactslist.get(position).getID());
                        String artselect = artifactslist.get(position).getName();
                        intent = new Intent(Allartifacts.this, ArtifactdescActivity.class);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("artifactselect", artifactselected);
                        editor.putString("art_header", artselect);
                        editor.commit();
                        startActivity(intent);
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

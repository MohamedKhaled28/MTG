package com.example.hp.mody_task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Model.Adapter4;
import Model.ArtifactDescription;
import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;

public class ArtifactdescActivity extends Home_activity {

    static List<ArtifactDescription> desclist ;
    ListView ls;
    Adapter4 adapter4;
    SharedPreferences mypref;
    Boolean checkconnect;
    TextToSpeech textToSpeech;
    String artifactitem;
    String art_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifactdesc);
        checkconnect = CheckConnectivity.isNetworkAvaliable(ArtifactdescActivity.this);
        if(checkconnect.equals(true)) {
            get_art_desc();
        }else{
            finish();
            Toast.makeText(ArtifactdescActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }

    }
    public void get_art_desc(){


        mypref = getApplicationContext().getSharedPreferences("my pref",0);
        art_name = mypref.getString("art_header",null);
        setTitle(art_name);
        String user_id = mypref.getString("ID",null);
        artifactitem = mypref.getString("artifactselect",null);
        checkconnect = CheckConnectivity.isNetworkAvaliable(ArtifactdescActivity.this);
        if(checkconnect.equals(true)) {
            placedesctask newdesc = new placedesctask();
            newdesc.execute("http://192.168.1.3/MTG/artifact_desc.php", user_id, artifactitem);
        }else{
            finish();
            Toast.makeText(ArtifactdescActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        checkconnect = CheckConnectivity.isNetworkAvaliable(ArtifactdescActivity.this);
        if(checkconnect.equals(true)) {
            get_art_desc();
        }else{
            finish();
            Toast.makeText(ArtifactdescActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
    }

    public static List<ArtifactDescription>  setti(String var){
        ArtifactDescription art =null;
        desclist = new ArrayList<>();
        JSONArray array = JsonParser.parseJson(var);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                art = new ArtifactDescription();
                art.setBrief_description(object.getString("Brief_description"));
                art.setImage(object.getString("image"));

            } catch (JSONException e) {

                e.printStackTrace();
            }

            desclist.add(art);
        }
        return desclist;
    }

    public void appear(View view) {
        TextView text = (TextView) findViewById(R.id.txtviw11);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        text.setVisibility(View.VISIBLE);


        final String lang = mypref.getString("Lang",null);
        final String tospeak = desclist.get(0).getBrief_description();
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
                    textToSpeech.speak(art_name+tospeak,TextToSpeech.QUEUE_FLUSH,null);

                }
            }
        });
    }

    private class placedesctask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {

            String context = null;
            context = DBconnection.getdata(params);
            desclist = setti(context);
            return context;
        }
        @Override
        protected void onPostExecute(String s) {
            if(desclist.size()==0){
                finish();
                Toast.makeText(ArtifactdescActivity.this,getString(R.string.valid_text),Toast.LENGTH_LONG).show();
            }
                adapter4 = new Adapter4(ArtifactdescActivity.this, 2, desclist);
                ls = (ListView) findViewById(R.id.artifactlist);
                ls.setAdapter(adapter4);
            TextView tv = (TextView) findViewById(R.id.txtviw11);
            tv.setText(desclist.get(0).getBrief_description());



            ImageView img = (ImageView) findViewById(R.id.muteimgdeart);
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
    @Override
    public void onBackPressed() {
        super.finish();
    }
}


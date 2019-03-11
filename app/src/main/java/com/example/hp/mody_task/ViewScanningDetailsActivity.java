package com.example.hp.mody_task;

import android.content.Context;
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

import Model.Adapter5;
import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;
import Model.ScanningDetails;

public class ViewScanningDetailsActivity extends Home_activity {
    static List<ScanningDetails> artifactlist ;
    Context cont;
    ListView ls;
    Adapter5 adapter;
    Intent intent;
    ImageView iv;
    Bitmap bitmap;
    Boolean checkconnect;
    TextToSpeech textToSpeech;
    SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scanning_details);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        Bundle b = getIntent().getExtras();
        myPrefs = getSharedPreferences("my pref", 0);
        String user_id = myPrefs.getString("ID",null);
        String code = b.getString("code");
        checkconnect = CheckConnectivity.isNetworkAvaliable(ViewScanningDetailsActivity.this);
        if(checkconnect.equals(true)) {
            QRtask newQR = new QRtask();
            newQR.execute("http://192.168.1.3/MTG/scanQRcode.php", code, user_id);
        }else {
            finish();
            Toast.makeText(ViewScanningDetailsActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
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
    public static List<ScanningDetails>  parse(String var){
        ScanningDetails art =null;
        List<ScanningDetails> artifactlist = new ArrayList<>();
        JSONArray array = JsonParser.parseJson(var);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                art = new ScanningDetails();
                art.setName(object.getString("Name"));
                art.setDescription(object.getString("Description"));
                art.setImage(object.getString("image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            artifactlist.add(art);
        }
        return artifactlist;
    }

    public void appearscan(View view) {
        TextView tv = (TextView) findViewById(R.id.description);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        tv.setVisibility(View.VISIBLE);
        final String lang = myPrefs.getString("Lang",null);
        final String tospeak = artifactlist.get(0).getDescription();
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
                    textToSpeech.speak((artifactlist.get(0).getName())+tospeak,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

    }

    private class QRtask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String context = null;

            context = DBconnection.getdata(params);
            artifactlist = parse(context);
            return context;
        }

        @Override
        protected void onPostExecute(String s) {
            if(artifactlist.size() == 0){
                finish();
                Toast.makeText(ViewScanningDetailsActivity.this,getString(R.string.valid_qr),Toast.LENGTH_LONG).show();
            }
            else {
                adapter = new Adapter5(ViewScanningDetailsActivity.this, 2, artifactlist);
                ls = (ListView) findViewById(R.id.ScanningDetails);
                ls.setAdapter(adapter);
                TextView Description = (TextView)findViewById(R.id.description);
                Description.setText(artifactlist.get(0).getDescription());
                setTitle(artifactlist.get(0).getName());

                ImageView img = (ImageView) findViewById(R.id.muteimgsc);
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

        startActivity(new Intent(ViewScanningDetailsActivity.this,Home_activity.class));
    }
}

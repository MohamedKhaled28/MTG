package com.example.hp.mody_task;

import android.content.Context;
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

import Model.Adapter2;
import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;
import Model.Places;

public class PlaceActivity extends Home_activity {
    static List<Places> placelist ;
    static Context cont;
    ListView ls;
    Adapter2 adapter2;
    Intent intent;
    SharedPreferences mypref;
    Boolean checkconnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        checkconnect = CheckConnectivity.isNetworkAvaliable(PlaceActivity.this);
        if(checkconnect.equals(true)) {
            get_place();
        }else {
            finish();
            Toast.makeText(PlaceActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }


    }
    public  void get_place(){
         mypref= getApplicationContext().getSharedPreferences("my pref",0);
        String user_id = mypref.getString("ID",null);
        String selectitemm = mypref.getString("selectitem",null);
        String tourname = mypref.getString("tourname",null);
        setTitle(tourname);
        checkconnect = CheckConnectivity.isNetworkAvaliable(PlaceActivity.this);
        if(checkconnect.equals(true)) {
            placetask newplace = new placetask();
            newplace.execute("http://192.168.1.3/MTG/rate_place.php", user_id, selectitemm);
        }else {
            finish();
            Toast.makeText(PlaceActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
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


    @Override
    protected void onResume() {
        super.onResume();
        checkconnect = CheckConnectivity.isNetworkAvaliable(PlaceActivity.this);
        if(checkconnect.equals(true)) {
            get_place();
        }else {
            finish();
            Toast.makeText(PlaceActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }

    }



    public static List<Places>  setti(String var) {
        Places art = null;
        List<Places> placelist = new ArrayList<>();
        JSONArray array = JsonParser.parseJson(var);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = null;
                try {
                    object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                    art = new Places();
                    art.setName(object.getString("Name"));
                    art.setID(object.getInt("ID"));
                    art.setMark(object.getDouble("Mark"));
                    art.setImage(object.getString("Image"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                placelist.add(art);
            }


            return placelist;
        }



    private class placetask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            String context = null;
            context = DBconnection.getdata(params);


            placelist = setti(context);

            return context;
        }

        @Override
        protected void onPostExecute(String s) {
            if (placelist.size() == 0) {
              finish();
                Toast.makeText(PlaceActivity.this,getString(R.string.valid_text),Toast.LENGTH_LONG).show();
            } else {
                adapter2 = new Adapter2(PlaceActivity.this, 2, placelist);
                ls = (ListView) findViewById(R.id.placelist);
                ls.setAdapter(adapter2);
                ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectitem = String.valueOf(placelist.get(position).getID());
                        String placename = placelist.get(position).getName();
                        intent = new Intent(PlaceActivity.this, PlacedetailsActivity.class);
                        SharedPreferences.Editor editor = mypref.edit();
                        editor.putString("placeitem",selectitem);
                        editor.putString("placename",placename);
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

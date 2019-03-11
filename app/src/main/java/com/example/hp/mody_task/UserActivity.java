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

import Model.Adapter;
import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;
import Model.TourismTypes;

public class UserActivity extends Home_activity {
    static List<TourismTypes> tourismlist;
    Context cont;
    ListView ls;
    Adapter adapter;
    Intent intent;
    SharedPreferences mypref;
    Boolean checkconnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle(getString(R.string.tourheader));
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        checkconnect = CheckConnectivity.isNetworkAvaliable(UserActivity.this);
        if(checkconnect.equals(true)) {
            get_tourism_types();
        }else {
            finish();
            Toast.makeText(UserActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
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
        checkconnect = CheckConnectivity.isNetworkAvaliable(UserActivity.this);
        if(checkconnect.equals(true)) {
            get_tourism_types();
        }else {
            finish();
            Toast.makeText(UserActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
    }

    public void get_tourism_types() {
        mypref= getApplicationContext().getSharedPreferences("my pref", 0);
        String user_id = mypref.getString("ID", null);
        checkconnect = CheckConnectivity.isNetworkAvaliable(UserActivity.this);
        if(checkconnect.equals(true)) {
            mytask newtask = new mytask();
            newtask.execute("http://192.168.1.3/MTG/select_tourism.php", user_id);
        }else {
            finish();
            Toast.makeText(UserActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }

    }



    public static List<TourismTypes> setti(String var) {
        TourismTypes art = null;
        List<TourismTypes> tourismlist = new ArrayList<>();
        JSONArray array = JsonParser.parseJson(var);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                art = new TourismTypes();
                art.setName(object.getString("Name"));
                art.setImage(object.getString("Image"));
                art.setID(object.getInt("ID"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tourismlist.add(art);
        }
        return tourismlist;
    }


    private class mytask extends AsyncTask<String, String, String> {

        JSONArray arr = null;
        @Override
        protected String doInBackground(String... params) {

            String context = null;

            context = DBconnection.getdata(params);

            tourismlist = setti(context);

            return context;
        }

        @Override
        protected void onPostExecute(String s) {
            if (tourismlist.size() == 0) {
                finish();
                Toast.makeText(UserActivity.this,getString(R.string.valid_text),Toast.LENGTH_LONG).show();
                } else {
                adapter = new Adapter(UserActivity.this, 2, tourismlist);
                ls = (ListView) findViewById(R.id.lst);
                ls.setAdapter(adapter);
                ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectitem = String.valueOf(tourismlist.get(position).getID());
                        String tourname = tourismlist.get(position).getName();
                        intent = new Intent(UserActivity.this, PlaceActivity.class);
                        SharedPreferences.Editor editor = mypref.edit();
                        editor.putString("selectitem",selectitem);
                        editor.putString("tourname",tourname);
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

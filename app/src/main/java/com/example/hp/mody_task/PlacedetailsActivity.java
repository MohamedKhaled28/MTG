package com.example.hp.mody_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;
import Model.PlacesDetails;

public class PlacedetailsActivity extends Home_activity {
    static List<PlacesDetails> placedetailslist ;
    Intent intent;
    static PlacesDetails art =null;
    SharedPreferences mypref;
    Boolean checkconnect;
    TextToSpeech textToSpeech;
    String placename;
    Drawable img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placedetails);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
       }
        checkconnect = CheckConnectivity.isNetworkAvaliable(PlacedetailsActivity.this);
        if(checkconnect.equals(true)) {
            get_placedetail();
        }else {
            finish();
            Toast.makeText(PlacedetailsActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }


    }
    public void get_placedetail(){
         mypref= getApplicationContext().getSharedPreferences("my pref",0);
        String user_id = mypref.getString("ID",null);
        String selectitemm = mypref.getString("placeitem",null);
        placename = mypref.getString("placename",null);
        setTitle(placename);
        checkconnect = CheckConnectivity.isNetworkAvaliable(PlacedetailsActivity.this);
        if(checkconnect.equals(true)) {
            placedetailstask newplacedetails = new placedetailstask();
            newplacedetails.execute("http://192.168.1.3/MTG/select_artifact.php", user_id, selectitemm);
        }else {
            finish();
            Toast.makeText(PlacedetailsActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
    }

    }




    @Override
    protected void onResume() {
        super.onResume();
        checkconnect = CheckConnectivity.isNetworkAvaliable(PlacedetailsActivity.this);
        if(checkconnect.equals(true)) {
            get_placedetail();
        }else {
            finish();
            Toast.makeText(PlacedetailsActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }

    }
    public static List<PlacesDetails>  setti(String var) {
        List<PlacesDetails> placedetailslist = new ArrayList<>();
        JSONArray array = JsonParser.parseJson(var);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                art = new PlacesDetails();

                art.setNamme(object.getString("namme"));
                art.setAddress(object.getString("address"));
                art.setTourism_type(object.getString("tourism_type"));
                art.setDescription(object.getString("description"));
                art.setImage(object.getString("image"));


                if (object.length() == 8) {
                    art.setTicket_Price(object.getString("Ticket_Price"));
                    art.setKind(object.getString("Kind"));
                  //  art.setName(object.getString("Name"));
                    art.setID(object.getInt("ID"));

                } else if (object.length() == 7) {
                    art.setWeather_type(object.getString("weather_type"));
                    art.setArea(object.getString("area"));
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

            placedetailslist.add(art);
        }
        return placedetailslist;
    }

    public Bitmap getbitmapforimage(String src){
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

    public void edit(View view) {
        TextView text = (TextView) findViewById(R.id.txtviw5);
        TextView text2 = (TextView) findViewById(R.id.txtviw6);
        TextView text3 = (TextView) findViewById(R.id.txtviw7);
        TextView text4 = (TextView) findViewById(R.id.txtviw8);
        TextView text5 = (TextView) findViewById(R.id.txtviw9);
        //TextView text6 = (TextView) findViewById(R.id.txtviw10);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text4.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text5.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
       // text6.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text.setVisibility(View.VISIBLE);


    }

    public void edit1(View view) {
        TextView text = (TextView) findViewById(R.id.txtviw5);
        TextView text2 = (TextView) findViewById(R.id.txtviw6);
        TextView text3 = (TextView) findViewById(R.id.txtviw7);
        TextView text4 = (TextView) findViewById(R.id.txtviw8);
        TextView text5 = (TextView) findViewById(R.id.txtviw9);
       // TextView text6 = (TextView) findViewById(R.id.txtviw10);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text4.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text5.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
       // text6.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);

    }

    public void edit2(View view) {
        TextView text = (TextView) findViewById(R.id.txtviw5);
        TextView text2 = (TextView) findViewById(R.id.txtviw6);
        TextView text3 = (TextView) findViewById(R.id.txtviw7);
        TextView text4 = (TextView) findViewById(R.id.txtviw8);
        TextView text5 = (TextView) findViewById(R.id.txtviw9);
       // TextView text6 = (TextView) findViewById(R.id.txtviw10);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        text4.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text5.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
       // text6.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        final String lang = mypref.getString("Lang",null);
        final String tospeak = art.getDescription();
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
                    textToSpeech.speak(placename+tospeak,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });


    }

    public void edit3(View view) {
        TextView text = (TextView) findViewById(R.id.txtviw5);
        TextView text2 = (TextView) findViewById(R.id.txtviw6);
        TextView text3 = (TextView) findViewById(R.id.txtviw7);
        TextView text4 = (TextView) findViewById(R.id.txtviw8);
        TextView text5 = (TextView) findViewById(R.id.txtviw9);
       // TextView text6 = (TextView) findViewById(R.id.txtviw10);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text4.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
        text5.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
       // text6.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);

    }

    public void edit4(View view) {
        TextView text = (TextView) findViewById(R.id.txtviw5);
        TextView text2 = (TextView) findViewById(R.id.txtviw6);
        TextView text3 = (TextView) findViewById(R.id.txtviw7);
        TextView text4 = (TextView) findViewById(R.id.txtviw8);
        TextView text5 = (TextView) findViewById(R.id.txtviw9);
        //TextView text6 = (TextView) findViewById(R.id.txtviw10);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text2.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text3.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text4.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);
        text5.setTextSize(TypedValue.COMPLEX_UNIT_SP,23);
       // text6.setTextSize(TypedValue.COMPLEX_UNIT_SP,0);

    }


    private class placedetailstask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {

            String context = null;
            context = DBconnection.getdata(params);
            return context;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            RatingBar rate_place = (RatingBar) findViewById(R.id.rate_placee);
            if(rate_place.getRating() == 0.0){
                placedetailslist = setti(s);
               // TextView Name=null;
                TextView address = (TextView) findViewById(R.id.txtviw5);
                TextView tourism_type = (TextView) findViewById(R.id.txtviw6);
                TextView description = (TextView) findViewById(R.id.txtviw7);
                ImageView iv = (ImageView) findViewById(R.id.imageView);
                rate_place.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("my pref",0);
                        String var = String.valueOf(rating);
                        String name_place = art.getNamme();
                        String ID = sharedPreferences.getString("ID",null);
                        checkconnect = CheckConnectivity.isNetworkAvaliable(PlacedetailsActivity.this);
                        if(checkconnect.equals(true)) {
                            placedetailstask task = new placedetailstask();
                            task.execute("http://192.168.1.3/MTG/rate.php", var, name_place, ID);
                       }else {
                            finish();
                            Toast.makeText(PlacedetailsActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                    }
                    }
                });
                String image_url = art.getImage();
                Bitmap image = getbitmapforimage(image_url);
                img = new BitmapDrawable(getResources(),image);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    iv.setBackground(img);
                }


                address.setText(art.getAddress());
                tourism_type.setText(art.getTourism_type());
                description.setText(art.getDescription());
                if (!(art.getWeather_type() == null)) {
                    TextView weather_type = (TextView) findViewById(R.id.txtviw8);
                    TextView area = (TextView) findViewById(R.id.txtviw9);
                    TextView ticket = (TextView) findViewById(R.id.ticket);
                    TextView kind = (TextView) findViewById(R.id.kind);
                    TextView arti = (TextView) findViewById(R.id.artifact);
                    Button btn = (Button) findViewById(R.id.btn6);
                    btn.setVisibility(View.INVISIBLE);
                    ticket.setText(getString(R.string.weather_type));
                    kind.setText(getString(R.string.area));
                    arti.setText("");
                    weather_type.setText(art.getWeather_type());
                    area.setText(art.getArea());

                } else {
                    TextView Ticket_Price = (TextView) findViewById(R.id.txtviw8);
                    TextView Kind = (TextView) findViewById(R.id.txtviw9);
                    //Name = (TextView) findViewById(R.id.txtviw10);
                    Ticket_Price.setText(art.getTicket_Price());
                    Kind.setText(art.getKind());
                   // Name.setText(art.getName());

                }

                Button btn = (Button) findViewById(R.id.btn6);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // String museum = String.valueOf(art.getID());
                        String museumname = art.getNamme();
                        intent = new Intent(PlacedetailsActivity.this,Allartifacts.class);
                        SharedPreferences.Editor editor = mypref.edit();
                      //  editor.putString("artifactitem",museum);
                        editor.putString("art_name",museumname);
                        editor.commit();
                        startActivity(intent);
                    }
                });
            }else {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(PlacedetailsActivity.this);
                AlertDialog alert;
                alert = alertdialog.create();
                alert.setMessage(getString(R.string.rate));
                alert.show();



            }

            final ImageView img = (ImageView) findViewById(R.id.muteimgde);
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


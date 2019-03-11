package com.example.hp.mody_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.hp.mody_task.R.string.share;

public class Home_activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    Intent intent , intent3;
    String ctx;
    JSONArray array;
    String context = null;
    Boolean checkconnect;
    private ImageView image1 , iconsearch;
    private int[] imageArray;
    private int currentIndex;
    private int startIndex;
    private int endIndex;
    Drawable img;
    private static String[] arr_artifact = new String[] {"The small coffin of Tutankhamun" , "Der kleine Sarg von Tutanchamun",
    "A pair of golden bracelets","Ein Paar goldene Armbänder","Ra Hutub and Nefert","Ra Hutub und Nefert", "restored wooden mihrab",
    "Restaurierte hölzerne Mihrab","Mus-haf","Mus-haf","The key to the Kaaba","Der Schlüssel zur Kaaba",
    "Parshail painted the twelve students","Parshail malte die zwölf Studenten","Neilos and Harpocrates",
    "Neilos and Harpocrates","Wedding garlands","Hochzeitsgirlanden"};
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activity);

        iconsearch = (ImageView) findViewById(R.id.iconsearch);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto);

        ArrayAdapter<String> autoAdapter = new ArrayAdapter<String>(this , R.layout.support_simple_spinner_dropdown_item , arr_artifact);
        autoCompleteTextView.setAdapter(autoAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkconnect =  CheckConnectivity.isNetworkAvaliable(Home_activity.this);
                if(checkconnect.equals(true)) {
                    String artifact_name = autoCompleteTextView.getText().toString();
                    intent3 = new Intent(Home_activity.this, ResultActivity.class);
                    intent3.putExtra("artifact_name", artifact_name);
                    startActivity(intent3);
                }else {
                        Toast.makeText(Home_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();

                }
            }
        });

        iconsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artifact_name = autoCompleteTextView.getText().toString();
                if(artifact_name.equals("")){
                   Toast.makeText(Home_activity.this,getString(R.string.valid_search),Toast.LENGTH_LONG).show();              }
                else {
                    checkconnect =  CheckConnectivity.isNetworkAvaliable(Home_activity.this);
                    if(checkconnect.equals(true)) {
                        intent3 = new Intent(Home_activity.this, ResultActivity.class);
                        intent3.putExtra("artifact_name", artifact_name);
                        startActivity(intent3);
                    }else {
                        Toast.makeText(Home_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        image1 = (ImageView)findViewById(R.id.img);
        imageArray = new int[10];
        imageArray[0] = R.drawable.s1;
        imageArray[1] = R.drawable.s2;
        imageArray[2] = R.drawable.s3;
        imageArray[3] = R.drawable.s4;
        imageArray[4] = R.drawable.s5;
        imageArray[5] = R.drawable.s6;
        imageArray[6] = R.drawable.s7;
        imageArray[7] = R.drawable.s8;
        imageArray[8] = R.drawable.s9;
        imageArray[9] = R.drawable.gouna;


        startIndex = 0;
        endIndex = 9;
        nextImage();

    }
    public void nextImage(){
       // image1.setImageResource(imageArray[currentIndex]);
        img = getResources().getDrawable(imageArray[currentIndex]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            image1.setBackground(img);
        }
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        image1.startAnimation(rotateimage);
        currentIndex++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex>endIndex){
                    currentIndex--;
                    previousImage();
                }else{
                    nextImage();
                }

            }
        },2000); // here 1000(1 second) interval to change from current  to next image

    }
    public void previousImage(){
        //image1.setImageResource(imageArray[currentIndex]);
        img = getResources().getDrawable(imageArray[currentIndex]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            image1.setBackground(img);
        }
        Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        image1.startAnimation(rotateimage);
        currentIndex--;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex<startIndex){
                    currentIndex++;
                    nextImage();
                }else{
                    previousImage(); // here 1000(1 second) interval to change from current  to previous image
                }
            }
        },3000);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);


        ActionMenuView bottomBar = (ActionMenuView) findViewById(R.id.bottom_toolbar);
        Menu bottomMenu = bottomBar.getMenu();
            getMenuInflater().inflate(R.menu.bottom_menu, bottomMenu);
        bottomBar.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int ID = item.getItemId();
                if(ID == R.id.select){
                    checkconnect =  CheckConnectivity.isNetworkAvaliable(Home_activity.this);
                    if(checkconnect.equals(true)) {
                        startActivity(new Intent(Home_activity.this, UserActivity.class));
                    }else {

                        Toast.makeText(Home_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                    }
                }else if (ID == R.id.scanQR){
                    checkconnect =  CheckConnectivity.isNetworkAvaliable(Home_activity.this);
                    if(checkconnect.equals(true)) {
                    mScannerView = new ZXingScannerView(Home_activity.this);
                    setContentView(mScannerView);
                    mScannerView.setResultHandler(Home_activity.this);
                    mScannerView.startCamera();
                    }else {

                        Toast.makeText(Home_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                    }
                }else if (ID == R.id.Hoome){
                    startActivity(new Intent(getApplicationContext(),Home_activity.class));


                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change) {
            Intent intent = new Intent(Home_activity.this, Change_activity.class);
            startActivity(intent);
        }
        else if(id == R.id.id_share){
            SharedPreferences myPrefs = getSharedPreferences("my pref", 0);
            String user_id = myPrefs.getString("ID",null);

           checkconnect =  CheckConnectivity.isNetworkAvaliable(Home_activity.this);
            if(checkconnect.equals(true)) {
                sharetask share = new sharetask();
                share.execute("http://192.168.1.3/MTG/video.php", user_id);
            }else {
                finish();
                Toast.makeText(Home_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
            }
        }
        else if(id==R.id.rate_app){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/")));

        }
        else if(id == R.id.face){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MTG-1353513331430037/"));
            startActivity(intent);
        } else if(id == R.id.insta){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/mobile_tour_guide/"));
            startActivity(intent);
        } else if(id == R.id.twit){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MTG272017"));
            startActivity(intent);

        }
        else if(id == R.id.help){
            startActivity(new Intent(Home_activity.this,HelpActivity.class));
        }
        return true;
    }
    public void parsing(String x){
        /*  JSONObject jsonObject = new JSONObject(x);
          if (jsonObject.getBoolean("Success")) {
              if (jsonObject.has("item")) {*/
        array = JsonParser.parseJson(x);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                ctx = object.getString("video");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "MTG");
        String sAux = "\n"+getString(R.string.saux)+"\n\n";
        sAux = sAux + ctx+" \n\n";
        intent.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(intent, getString(share)));


    }

    @Override
    public void handleResult(Result result) {
        checkconnect =  CheckConnectivity.isNetworkAvaliable(Home_activity.this);
        if(checkconnect.equals(true)) {
        String code = result.getText().toString();
        intent = new Intent(Home_activity.this, ViewScanningDetailsActivity.class);
        Bundle bund = new Bundle();
        bund.putString("code" , code);
        intent.putExtras(bund);
        startActivity(intent);
        mScannerView.resumeCameraPreview(this);
            //mScannerView.startCamera();
        }else {
            finish();
            Toast.makeText(Home_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
    }

    private class sharetask extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... params) {


            context = DBconnection.getdata(params);
            return context;
        }


        @Override
        protected void onPostExecute(String s) {
            parsing(context);

        }
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}

package com.example.hp.mody_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;
import Model.PlayerConfig;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class HelpActivity extends YouTubeBaseActivity implements ZXingScannerView.ResultHandler {
    YouTubePlayerView youTubePlayerView;
    Button play;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    static String video;
    Toolbar tb;
    private ZXingScannerView mScannerView;
    Boolean checkconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle(getString(R.string.helpheader));
        tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle("MTG");
        //tb.setTitleTextColor(000000);
        tb.inflateMenu(R.menu.menu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.change) {
                    Intent intent = new Intent(HelpActivity.this, Change_activity.class);
                    startActivity(intent);
                }
                else if(id == R.id.id_share){
                    SharedPreferences myPrefs = getSharedPreferences("my pref", 0);
                    String user_id = myPrefs.getString("ID",null);
                    checkconnect = CheckConnectivity.isNetworkAvaliable(HelpActivity.this);
                    if(checkconnect.equals(true)) {
                        GetVideo videoo = new GetVideo();
                        videoo.execute("http://192.168.1.3/MTG/video.php", user_id);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "MTG");
                        String sAux = "\n" + getString(R.string.saux) + "\n\n";
                        sAux = sAux + video + " \n\n";
                        intent.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(intent, getString(R.string.share)));
                    }else {
                        Toast.makeText(HelpActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                    }
                }
                else if(id==R.id.rate_app){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/")));

                }
                else if(id == R.id.face){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MTG-1353513331430037"));
                    startActivity(intent);
                } else if(id == R.id.insta){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/mobile_tour_guide/"));
                    startActivity(intent);
                } else if(id == R.id.twit){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MTG272017"));
                    startActivity(intent);

                }
                else if(id == R.id.help){

                }
                return true;

            }
        });
        ActionMenuView ac = (ActionMenuView) findViewById(R.id.bottom_toolbar);
        getMenuInflater().inflate(R.menu.bottom_menu,ac.getMenu());
        ac.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int ID = item.getItemId();
                if(ID == R.id.select){
                    checkconnect =  CheckConnectivity.isNetworkAvaliable(HelpActivity.this);
                    if(checkconnect.equals(true)) {
                    startActivity(new Intent(HelpActivity.this,UserActivity.class));
                    }else {

                        Toast.makeText(HelpActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                    }
                }else if (ID == R.id.scanQR){
                    checkconnect =  CheckConnectivity.isNetworkAvaliable(HelpActivity.this);
                    if(checkconnect.equals(true)) {
                    mScannerView = new ZXingScannerView(HelpActivity.this);
                    setContentView(mScannerView);
                    mScannerView.setResultHandler(HelpActivity.this);
                    mScannerView.startCamera();
                    }else {

                        Toast.makeText(HelpActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                    }
                }else if (ID == R.id.Hoome){
                    startActivity(new Intent(HelpActivity.this,Home_activity.class));


                }
                return false;

            }
        });
        play = (Button) findViewById(R.id.btn_help);
        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_player_view);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //youTubePlayer.loadVideo("3LiubyYpEUk");
                youTubePlayer.loadVideo(video);
                youTubePlayer.play();
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };


       play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkconnect = CheckConnectivity.isNetworkAvaliable(HelpActivity.this);
                if(checkconnect.equals(true)) {
                    GetVideo getVideo = new GetVideo();
                    getVideo.execute("http://192.168.1.3/MTG/api_help.php");
                    youTubePlayerView.initialize(PlayerConfig.API_KEY, onInitializedListener);
               }else {

                    Toast.makeText(HelpActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
           }
            }
        });
    }

    @Override
    public void handleResult(Result result) {
        checkconnect = CheckConnectivity.isNetworkAvaliable(HelpActivity.this);
        if(checkconnect.equals(true)) {
            String code = result.getText().toString();
            Intent intent = new Intent(HelpActivity.this, ViewScanningDetailsActivity.class);
            Bundle bund = new Bundle();
            bund.putString("code", code);
            intent.putExtras(bund);
            startActivity(intent);
            mScannerView.resumeCameraPreview(this);
        } else {
                finish();
                Toast.makeText(HelpActivity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
            }

    }


    private class GetVideo extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            String result = DBconnection.getdata(params);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONArray array = JsonParser.parseJson(s);

            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                    video = object.getString("video");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            // return video;
        }
    }
}

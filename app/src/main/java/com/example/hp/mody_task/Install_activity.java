package com.example.hp.mody_task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;

public class Install_activity extends AppCompatActivity {
    String contect=null;
    Spinner Lang;
    EditText Mail;
    String Txt_mail;
    String Language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_activity);
        Boolean isfirsttime = getSharedPreferences("my pref",MODE_PRIVATE).getBoolean("isfirstrun",true);
        if(isfirsttime){
            getSharedPreferences("my pref",MODE_PRIVATE).edit().putBoolean("isfirstrun",false).commit();

        }else
        {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("my pref",0);
            String ID = sharedPreferences.getString("ID",null);
            if(ID == null){


            } else {
                Intent intent = new Intent(Install_activity.this, Home_activity.class);
                startActivity(intent);
            }

        }


    }



    public void Add_User(View view) {
        Lang = (Spinner) findViewById(R.id.spinner);
        Mail = (EditText) findViewById(R.id.Txt_mail);
        Txt_mail = Mail.getText().toString();
        Language = Lang.getSelectedItem().toString();
        Boolean var = isValidEmail(Txt_mail);
        if (Txt_mail.equals("") || Language.equals("")) {

            Toast.makeText(Install_activity.this,getString(R.string.valid_fill_form),Toast.LENGTH_LONG).show();
        } else if (var.equals(true)) {

            Boolean checkconnect = CheckConnectivity.isNetworkAvaliable(Install_activity.this);
            if (checkconnect.equals(true)) {
                languagetask task = new languagetask();
                task.execute("http://192.168.1.3/Ahmed_task/insert_user.php", Txt_mail, Language);
            } else {
                finish();
                Toast.makeText(Install_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
        }
            else {

            Toast.makeText(Install_activity.this,getString(R.string.valid_email),Toast.LENGTH_LONG).show();
            }
        }


    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    public void Func(String var) {
        String ID = null;
        JSONArray array = JsonParser.parseJson(var);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = null;
            try {
                object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                ID = object.getString("Id");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("my pref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent intent = new Intent(Install_activity.this,Home_activity.class);
        editor.putString("ID",ID);
        editor.putString("Lang",Language);
        editor.commit();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log,menu);
        return super.onCreateOptionsMenu(menu);

    }

    private class languagetask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            contect = DBconnection.getdata(params);

            return contect;
        }

        @Override
        protected void onPostExecute(String s) {

            Func(s);

        }


    }

}

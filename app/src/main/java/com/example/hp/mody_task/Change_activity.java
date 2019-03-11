package com.example.hp.mody_task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.CheckConnectivity;
import Model.DBconnection;
import Model.JsonParser;

public class Change_activity extends Home_activity {
    String ID;
    Boolean checkconnect;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_activity);
        setTitle(getString(R.string.changeheader));
        sharedPreferences = getApplicationContext().getSharedPreferences("my pref",0);
        ID = sharedPreferences.getString("ID",null);
        checkconnect = CheckConnectivity.isNetworkAvaliable(Change_activity.this);
        if(checkconnect.equals(true)) {
            languagetask task = new languagetask();
            task.execute("http://192.168.1.3/Ahmed_task/get_language.php", ID);
        }else {
            finish();
            Toast.makeText(Change_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
        }
    }



    private class languagetask extends AsyncTask<String , String , String>{

        @Override
        protected String doInBackground(String... params) {
            String context = DBconnection.getdata(params);
            return context;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String language = null;
            JSONArray array = JsonParser.parseJson(s);
            if (array == null) {

            }
            else {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(String.valueOf(array.getJSONObject(i)));
                        language = object.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                final Spinner spinner = (Spinner) findViewById(R.id.spinner_change);
                String[] spinnerarray = new String[0];
                if (language.equals("English")) {
                    spinnerarray = new String[]{language, "Deutsche"};
                } else if (language.equals("Deutsche")) {
                    spinnerarray = new String[]{language, "English"};

                }
                ArrayAdapter adapter = new ArrayAdapter(Change_activity.this, android.R.layout.simple_spinner_item, spinnerarray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String Lang_changed = spinner.getSelectedItem().toString();
                        sharedPreferences = getApplicationContext().getSharedPreferences("my pref",0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Lang",Lang_changed);
                        editor.commit();
                        checkconnect = CheckConnectivity.isNetworkAvaliable(Change_activity.this);
                        if (checkconnect.equals(true)){
                            languagetask task = new languagetask();
                        task.execute("http://192.168.1.3/Ahmed_task/Change_lang.php", Lang_changed, ID);
                    }else {

                            Toast.makeText(Change_activity.this,getString(R.string.valid_connection),Toast.LENGTH_LONG).show();
                             }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
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

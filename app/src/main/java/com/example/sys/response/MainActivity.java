package com.example.sys.response;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private List<JobListItem> jobListItems;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        ArrayList<ChatBean> list = new ArrayList<>();

        jobListItems = new ArrayList<>();

        String url = "https://api.androidhive.info/contacts/";

        new ResponseAsync().execute(url);


    }

    private class ResponseAsync extends AsyncTask <String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(MainActivity.this);
            progressDialog.setTitle( "Get response Tutorial" );
            progressDialog.setMessage( "Loading.." );
            progressDialog.setIndeterminate( false );
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String respURL = strings[0];
            try {
                // Download Image from URL

                URL url = new URL(respURL);
                InputStream is = url.openConnection().getInputStream();

                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                Log.e("My data ", buffer.toString());
                return buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("Response data", s);

            try {
                JSONObject object= new JSONObject( s );
                JSONArray array = object.getJSONArray("contacts");

                for (int i=0;i<array.length();i++)
                {
                    JobListItem item=new JobListItem();
                    JSONObject list_obj = array.getJSONObject(i);
                    item.setid(list_obj.getString("id"));
                    item.setName(list_obj.getString("name"));
                    item.setEmail(list_obj.getString("email"));
                    item.setAddress(list_obj.getString("address"));
                    item.setGender(list_obj.getString("gender"));
                    JSONObject ph = list_obj.getJSONObject( "phone" );
                    item.setMobile( ph.getString( "mobile" ) );
                    item.setHome( ph.getString( "home" ) );
                    item.setOffice( ph.getString( "office" ) );
                    jobListItems.add( item );
                }
                recyclerView.setAdapter( new StudentAdapter( MainActivity.this,jobListItems ) );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
}

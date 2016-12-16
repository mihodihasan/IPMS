package com.aqib.www.ipms;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FilteredActivity extends AppCompatActivity {

    private String jasonString;
    String jasonString2;

    String jsonResponse2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered);

        String link=getIntent().getStringExtra("link");
        new GetAllPatientsDetailsJson(this).execute(link);

    }

    public class GetAllPatientsDetailsJson extends AsyncTask<String, Void, String> {
        Context ctx;
        String jsonUrl;

        public GetAllPatientsDetailsJson(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            //Here u got the json result, do something
            jsonResponse2 = result;
            makeList(result);
//            processJson();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jasonString2 = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jasonString2 + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String r = stringBuilder.toString().trim();
                Log.e("json", r);
                return r;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Unsuccessful";
        }
    }
    private void makeList(String result) {
        ListView patitentLV = (ListView) findViewById(R.id.patientList);
        List<Info> list = new ArrayList<Info>();
        Info info = new Info("ID", "Name", "Contact");
        list.add(info);

        InfoListAdapter infoListAdapter = new InfoListAdapter(FilteredActivity.this, list);

        patitentLV.setAdapter(infoListAdapter);

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("tests");
            int count = 0;
            String id, Name, contact;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                id = jo.getString("id");
                Name = jo.getString("name");
                contact = jo.getString("contact");

//                Log.e("data",test_id+"\t"+testName+"\t"+price+"\t"+room);
//                Details details = new Call.Details(post, user_name, time);
//                adapter.add(details);
                Info info1 = new Info(id, Name, contact);
                list.add(info1);

                count++;
            }
            infoListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class Info {

        String ID, Name, Contact;

        public Info(String ID, String name, String contact) {
            this.ID = ID;
            Name = name;
            Contact = contact;
        }

        public String getID() {
            return ID;
        }

        public String getName() {
            return Name;
        }

        public String getContact() {
            return Contact;
        }
    }
}

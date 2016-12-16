package com.aqib.www.ipms;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class SearchActivity extends AppCompatActivity {

    ArrayList<Patient> data;
    ListView listView;
    ListAdapter listAdapter;

    EditText searchInput;
    Button search;
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = (Button) findViewById(R.id.search);
        searchInput = (EditText) findViewById(R.id.searchInput);

        listView = (ListView) findViewById(R.id.searchListView);
        data = new ArrayList<>();
        data.add(new Patient("Name", "Quantity", "Price"));
        listAdapter = new ListAdapter(this, data);
        listView.setAdapter(listAdapter);

        Toast.makeText(this, getIntent().getStringExtra("modifier"), Toast.LENGTH_SHORT).show();
    }

    public void search(View view) {
        data.clear();
        data.add(new Patient("Name","Quantity","Price"));
        String searchType = getIntent().getStringExtra("modifier");
        new Search(this).execute(searchType, searchInput.getText().toString());


    }

    private void processJson() {
        try {
            Log.d("json", jsonResponse);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject test = new JSONObject(MainActivity.jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("searchresults");
            JSONArray testJsonArray = test.getJSONArray("tests");
            int count = 0;
            String id, name, age, sex, mobile, date, ref_by, test_id, amount, admin_id;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                id = jo.getString("id");
                name = jo.getString("name");
                age = jo.getString("age");
                sex = jo.getString("sex");
                mobile = jo.getString("mobile");
                date = jo.getString("date");
                ref_by = jo.getString("ref_by");
                test_id = jo.getString("test_id");
                amount = jo.getString("amount");
                admin_id = jo.getString("admin_id");

                Patient patient = new Patient(name, amount, findPrice(test_id));
                data.add(patient);
                Log.e("data", test_id + "\t");
//                Details details = new Call.Details(post, user_name, time);
//                adapter.add(details);
                count++;
            }

            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("BUG", "ERROR IN HERE");
        }
    }

    private String findPrice(String test_id) {
        String s="";
        try {
            JSONObject test = new JSONObject(MainActivity.jsonResponse);
            JSONArray testJsonArray = test.getJSONArray("tests");
            int count=0;
            while (count<testJsonArray.length()){
                JSONObject object=testJsonArray.getJSONObject(count);
                if(object.getString("test_id").equals(test_id)){
                    s= object.getString("price");
                    break;
                }
                count++;
            }
        } catch (JSONException e) {
            s= "0";
        }

        return s;
    }


    public class Search extends AsyncTask<String, Void, String> {
        Context ctx;
        String jsonUrl;
        private String jasonString;

        public Search(Context ctx) {
//            Log.d("tst","kjhvcgfxcgvhj");
            this.ctx = ctx;
        }

        @Override
        protected void onPostExecute(String result) {
            //Here u got the json result, do something
            jsonResponse = result;
            processJson();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("params", "" + params[0] + params[1] + "");
            jsonUrl = "http://tasin.eu.pn/search.php?type=" + params[0] + "&&searchkey=" + params[1];//+"&&searchkey="+params[1];
            try {
                URL url = new URL(jsonUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((jasonString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(jasonString + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                String r = stringBuilder.toString().trim();
                return r;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Unsuccessful";
        }
    }
}

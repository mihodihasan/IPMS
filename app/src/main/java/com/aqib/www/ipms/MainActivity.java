package com.aqib.www.ipms;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public static String jsonResponse;
    private String jasonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setSupportActionBar(toolbar);
        initNavigationDrawer();

//        new GetAllPatientsDetailsJson(this).execute("http://tasin.eu.pn/showpatients.php");

        new GetTestDetailsJson(this).execute();
    }

    public void initNavigationDrawer() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                Intent i=null;
                switch (id) {
                    case R.id.mri:
                        i=new Intent(MainActivity.this,FilteredActivity.class);
                        i.putExtra("link","http://tasin.eu.pn/showpatients.php?key=MRI");
                        startActivity(i);
//                        new GetAllPatientsDetailsJson(MainActivity.this).execute();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.xray:
                        i=new Intent(MainActivity.this,FilteredActivity.class);
                        i.putExtra("link","http://tasin.eu.pn/showpatients.php?key=XRAY");
                        startActivity(i);
//                        new GetAllPatientsDetailsJson(MainActivity.this).execute("http://tasin.eu.pn/showpatients.php?key=XRAY");
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.ultrasonography:
                        i=new Intent(MainActivity.this,FilteredActivity.class);
                        i.putExtra("link","http://tasin.eu.pn/showpatients.php?key=ULTRA");
                        startActivity(i);
//                        new GetAllPatientsDetailsJson(MainActivity.this).execute("http://tasin.eu.pn/showpatients.php?key=ULTRA");
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.lipid:
                        i=new Intent(MainActivity.this,FilteredActivity.class);
                        i.putExtra("link","http://tasin.eu.pn/showpatients.php?key=LIPID");
                        startActivity(i);
//                        new GetAllPatientsDetailsJson(MainActivity.this).execute("http://tasin.eu.pn/showpatients.php?key=LIPID");
                        drawerLayout.closeDrawers();
                    case R.id.memmogram:
                        i=new Intent(MainActivity.this,FilteredActivity.class);
                        i.putExtra("link","http://tasin.eu.pn/showpatients.php?key=MEMO");
                        startActivity(i);
//                        new GetAllPatientsDetailsJson(MainActivity.this).execute("http://tasin.eu.pn/showpatients.php?key=MEMO");
                        drawerLayout.closeDrawers();

                }
                return true;
            }
        });
        View header = navigationView.getHeaderView(0);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            new GetTestDetailsJson(this).execute();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetTestDetailsJson extends AsyncTask<String, Void, String> {
        Context ctx;
        String jsonUrl;

        public GetTestDetailsJson(Context ctx) {
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
        protected void onPreExecute() {
            jsonUrl = "http://tasin.eu.pn/show_test.php";
        }

        @Override
        protected String doInBackground(String... params) {
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







    private void processJson() {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("tests");
            int count = 0;
            String test_id, testName, price, room;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                test_id = jo.getString("test_id");
                testName = jo.getString("test_name");
                price = jo.getString("price");
                room = jo.getString("room");

                Log.e("data", test_id + "\t" + testName + "\t" + price + "\t" + room);
//                Details details = new Call.Details(post, user_name, time);
//                adapter.add(details);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}